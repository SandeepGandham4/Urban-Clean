package com.example.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dto.WasteLogResponseDTO;
import com.example.exception.InvalidRequestException;
import com.example.exception.MissingDateRangeException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.WasteLog;
import com.example.repository.WasteLogRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue; // Import for UnitValue

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PdfReportService {

    private final WasteLogRepository wasteLogRepository;

    public List<WasteLogResponseDTO> getWasteCollectionReportPdf(LocalDate startDate, LocalDate endDate,
                                                                   Long zoneId, Long vehicleId, Long workerId) {

        log.info("Fetching waste collection report without pagination - StartDate: {}, EndDate: {}, ZoneId: {}, VehicleId: {}, WorkerId: {}",
                startDate, endDate, zoneId, vehicleId, workerId);

        if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
            log.warn("Invalid date range: Only one of StartDate or EndDate is provided.");
            throw new MissingDateRangeException("Both startDate and endDate must be provided together.");
        }

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            log.warn("Invalid date range: StartDate ({}) is after EndDate ({})", startDate, endDate);
            throw new InvalidRequestException("Start date must be before end date.");
        }

        List<WasteLog> wasteLogs;

        // Existing filtering logic remains the same
        if (zoneId != null && vehicleId != null && workerId != null) {
            wasteLogs = wasteLogRepository.findByWorkerIdAndZoneIdAndVehicleId(workerId ,zoneId, vehicleId);
            }
        if (startDate != null && endDate != null && zoneId != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            wasteLogs = wasteLogRepository.findByCollectionTimeBetweenAndZoneId(startDateTime, endDateTime, zoneId);
        } else if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            wasteLogs = wasteLogRepository.findByCollectionTimeBetween(startDateTime, endDateTime);
        } else if (zoneId != null && vehicleId != null) {
            wasteLogs = wasteLogRepository.findByZoneIdAndVehicleId(zoneId, vehicleId);
        } else if (zoneId != null) {
            wasteLogs = wasteLogRepository.findByZoneId(zoneId);
        } else if (vehicleId != null && workerId != null) {
            wasteLogs = wasteLogRepository.findByVehicleIdAndWorkerId(vehicleId, workerId);
        } else if (vehicleId != null) {
            wasteLogs = wasteLogRepository.findByVehicleId(vehicleId);
        } else if (workerId != null) {
            wasteLogs = wasteLogRepository.findByWorkerId(workerId);
        } else {
            wasteLogs = wasteLogRepository.findAll();
        }

        if (wasteLogs.isEmpty()) {
            log.warn("No waste collection records found for the given filters.");
            throw new ResourceNotFoundException("No waste collection records found for the given filters.");
        }

        log.info("Successfully fetched {} waste collection records", wasteLogs.size());

        List<WasteLogResponseDTO> responseList = wasteLogs.stream()
                .filter(wasteLog -> wasteLog.getCollectionTime() != null && wasteLog.getWeightCollected() != null)
                .map(wasteLog -> {
                    WasteLogResponseDTO response = new WasteLogResponseDTO();
                    response.setLogId(wasteLog.getLogId());
                    response.setZoneId(wasteLog.getZoneId());
                    response.setVehicleId(wasteLog.getVehicleId());
                    response.setWorkerId(wasteLog.getWorkerId());
                    response.setWeightCollected(wasteLog.getWeightCollected());
                    response.setCollectionTime(wasteLog.getCollectionTime());
                    return response;
                })
                .collect(Collectors.toList());

        return responseList;
    }

  

    public byte[] generateWasteLogReport(List<WasteLogResponseDTO> wasteLogs) {
        log.info("Generating PDF report for {} waste logs", wasteLogs.size());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Add title
        document.add(new Paragraph("Waste Collection Report").setBold().setFontSize(16));

        // Add table headers
        // Increased columnWidths to accommodate the S.No. column
        // Added UnitValue.createPercent(X) for more flexible column widths,
        // or you can define fixed points. Using fixed points for simplicity here.
        float[] columnWidths = {30, 50, 50, 50, 50, 50}; // Added 30 for S.No.
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100)); // Make table take 100% width

        table.addHeaderCell("S.No."); // New: Add S.No. header
        table.addHeaderCell("Zone ID");
        table.addHeaderCell("Vehicle ID");
        table.addHeaderCell("Worker ID");
        table.addHeaderCell("Weight Collected");
        table.addHeaderCell("Collection Time");

        // Add data rows
        int sNo = 1; // Initialize serial number
        for (WasteLogResponseDTO log : wasteLogs) {
            table.addCell(String.valueOf(sNo++)); // New: Add serial number
            table.addCell(log.getZoneId() != null ? log.getZoneId().toString() : "N/A");
            table.addCell(log.getVehicleId() != null ? log.getVehicleId().toString() : "N/A");
            table.addCell(log.getWorkerId() != null ? log.getWorkerId().toString() : "N/A");
            table.addCell(log.getWeightCollected() != null ? log.getWeightCollected().toString() : "N/A");
            table.addCell(log.getCollectionTime().toString());
        }

        document.add(table);
        document.close();

        log.info("PDF report generated successfully.");
        return outputStream.toByteArray();
    }
}