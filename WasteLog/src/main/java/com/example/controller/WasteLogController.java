//package com.example.controller;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.dto.VehicleAndWorkerResponseDTO;
//import com.example.dto.WasteLogRequestDTO;
//import com.example.dto.WasteLogResponseDTO;
//import com.example.dto.WorkerAssignmentResponseDTO;
//import com.example.service.PdfReportService;
//import com.example.service.WasteLogService;
//
//import io.swagger.v3.oas.annotations.Parameter;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequestMapping("/api/v1/urbanclean")
//@RequiredArgsConstructor
//@CrossOrigin(
//	    origins = "http://localhost:3000",
//	    allowedHeaders = {"Authorization", "Content-Type"},
//	    //methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
//	    allowCredentials = "true"
//	)
//public class WasteLogController {
//
//
//    private final WasteLogService wasteLogService;
//    private final PdfReportService pdfReportService;
//
//    @GetMapping("worker/{workerId}")
//    public ResponseEntity<WorkerAssignmentResponseDTO> fetchWorkerDetails(@Valid @PathVariable Long workerId){
//    	return ResponseEntity.ok(wasteLogService.fetchWorkerDetails(workerId));
//    }
//
//    @PostMapping("/admin/waste-logs")    
//    public ResponseEntity<WasteLogResponseDTO> logWasteCollection(
//            @Parameter(description = "Waste collection details", required = true)
//            @Valid @RequestBody WasteLogRequestDTO requestDTO) {
//        WasteLogResponseDTO responseDTO = wasteLogService.createWasteLog(requestDTO);
//        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
//    }
//    
//    @PutMapping("/worker/waste-logs/{workerId}")
//    public ResponseEntity<WasteLogResponseDTO> enterLogData(@Valid @PathVariable Long workerId,@Valid @RequestParam Double weightCollected,@Valid @RequestParam LocalDateTime collectionTime){
//    	return ResponseEntity.ok(wasteLogService.enterLogData(workerId,weightCollected,collectionTime));
//    }
//
//    @GetMapping("/admin/reports/waste-collection")
//    public ResponseEntity<Page<WasteLogResponseDTO>> getWasteCollectionReport(
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//            @RequestParam(required = false) Long zoneId,
//            @RequestParam(required = false) Long vehicleId,
//            @RequestParam(required = false) Long workerId,
//            @RequestParam(defaultValue = "0") int page, // Default page is 0
//            @RequestParam(defaultValue = "10") int size, // Default size is 10
//            @RequestParam(defaultValue = "zoneId") String sort // Default sorting
//    ) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
//        Page<WasteLogResponseDTO> reportData = wasteLogService.getWasteCollectionReport(startDate, endDate, zoneId, vehicleId, workerId, pageable);
//        
//        return new ResponseEntity<>(reportData, HttpStatus.OK);
//    }
//
//    @GetMapping("/worker/reports/{id}")
//    public ResponseEntity<Page<WasteLogResponseDTO>> getWasteCollectionByWorkerId(
//            @PathVariable Long id,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//            @RequestParam(required = false) Long zoneId,
//            @RequestParam(required = false) Long vehicleId,
//            @RequestParam(defaultValue = "0") int page, // Default page is 0
//            @RequestParam(defaultValue = "10") int size, // Default size is 10
//            @RequestParam(defaultValue = "zoneId") String sort // Default sorting
//    ) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
//        Page<WasteLogResponseDTO> reportData = wasteLogService.getWasteCollectionReportByWorker(id, startDate, endDate, zoneId, vehicleId, pageable);
//
//        return new ResponseEntity<>(reportData, HttpStatus.OK);
//    }
//
//    @GetMapping("/admin/reports/waste-collection/pdf")
//    public ResponseEntity<byte[]> downloadWasteCollectionReportPdf(
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//            @RequestParam(required = false) Long zoneId,
//            @RequestParam(required = false) Long vehicleId,
//            @RequestParam(required = false) Long workerId) {
//
//        List<WasteLogResponseDTO> reportData = pdfReportService.getWasteCollectionReportPdf(startDate, endDate, zoneId, vehicleId, workerId);
//
//        byte[] pdfBytes = pdfReportService.generateWasteLogReport(reportData);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("attachment", "WasteCollectionReport.pdf");
//
//        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);    
//    }
//    @GetMapping("/worker/reports/waste-collection/{w_id}/pdf")
//    public ResponseEntity<byte[]> downloadWasteCollectionReportByWorkerPdf(
//    		@PathVariable Long w_id,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//            @RequestParam(required = false) Long zoneId,
//            @RequestParam(required = false) Long vehicleId
//            ) {
//
//        List<WasteLogResponseDTO> reportData = pdfReportService.getWasteCollectionReportPdf(startDate, endDate, zoneId, vehicleId, w_id);
//
//        byte[] pdfBytes = pdfReportService.generateWasteLogReport(reportData);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("attachment", "WasteCollectionReport.pdf");
// 
//        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);    
//    }
//    
//    @PostMapping("/admin/store")
//    public void storeVehicleIdAndWorkers(@RequestBody VehicleAndWorkerResponseDTO vehicleAndWorkerResponseDTO){
//    	wasteLogService.storeVehicleIdAndWorkers(vehicleAndWorkerResponseDTO);
//    }
//    @DeleteMapping("/admin/vehicle/{vehicleId}")
//    public List<Long> deleteLogDataOfWorkerAndVehicleId(@PathVariable Long vehicleId) {
//    	return wasteLogService.deleteLogDataOfWorkerAndVehicleId(vehicleId);
//    }
//	@DeleteMapping("/admin/zones/{zoneId}")
//	public void deleteLogDataOfWorkerByZoneId(@PathVariable Long zoneId) {
//		wasteLogService.deleteLogDataOfWorkerByZoneId(zoneId);
//	}
//	@PostMapping("/worker/vehicle/{vehicleId}/updatestatus")
//	public void updateVehicleLiveStatus(@PathVariable Long vehicleId) {
//		wasteLogService.updateVehicleLiveStatus(vehicleId);
//	}
//}

package com.example.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.VehicleAndWorkerResponseDTO;
import com.example.dto.WasteLogRequestDTO;
import com.example.dto.WasteLogResponseDTO;
import com.example.dto.WorkerAssignmentResponseDTO;
import com.example.service.PdfReportService;
import com.example.service.WasteLogService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/urbanclean/wastelog")
@RequiredArgsConstructor
//@CrossOrigin(
//	origins = "http://localhost:3000",
//	allowedHeaders = {"Authorization", "Content-Type"},
//	//methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
//	allowCredentials = "true"
//)
public class WasteLogController {


	private final WasteLogService wasteLogService;
	private final PdfReportService pdfReportService;

	@GetMapping("/worker/{workerId}")
	public ResponseEntity<WorkerAssignmentResponseDTO> fetchWorkerDetails(@Valid @PathVariable Long workerId){
		return ResponseEntity.ok(wasteLogService.fetchWorkerDetails(workerId));
	}
	@GetMapping("/worker/email")
	public ResponseEntity<Long> fetchWorkerDetailsByEmail(@RequestParam String email) {
	    return ResponseEntity.ok(wasteLogService.fetchWorkerDetailsByEmail(email));
	}


	@PostMapping("/admin/waste-logs")
	public ResponseEntity<WasteLogResponseDTO> logWasteCollection(
			@Parameter(description = "Waste collection details", required = true)
			@Valid @RequestBody WasteLogRequestDTO requestDTO) {
		WasteLogResponseDTO responseDTO = wasteLogService.createWasteLog(requestDTO);
		return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
	}

	@PutMapping("/worker/waste-logs/{workerId}")
	public ResponseEntity<WasteLogResponseDTO> enterLogData(@Valid @PathVariable Long workerId,@Valid @RequestParam Double weightCollected,@Valid @RequestParam LocalDateTime collectionTime){
		return ResponseEntity.ok(wasteLogService.enterLogData(workerId,weightCollected,collectionTime));
	}

	@GetMapping("/admin/reports/waste-collection")
	public ResponseEntity<Page<WasteLogResponseDTO>> getWasteCollectionReport(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false) Long zoneId,
			@RequestParam(required = false) Long vehicleId,
			@RequestParam(required = false) Long workerId,
			@RequestParam(defaultValue = "0") int page, // Default page is 0
			@RequestParam(defaultValue = "10") int size, // Default size is 10
			@RequestParam(defaultValue = "collectionTime") String sort // Changed default sort to collectionTime
	) {
		// Ensure the sort parameter matches a field in WasteLogResponseDTO
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		Page<WasteLogResponseDTO> reportData = wasteLogService.getWasteCollectionReport(startDate, endDate, zoneId, vehicleId, workerId, pageable);

		return new ResponseEntity<>(reportData, HttpStatus.OK);
	}

	@GetMapping("/worker/reports/{id}")
	public ResponseEntity<Page<WasteLogResponseDTO>> getWasteCollectionByWorkerId(
			@PathVariable Long id,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false) Long zoneId,
			@RequestParam(required = false) Long vehicleId,
			@RequestParam(defaultValue = "0") int page, // Default page is 0
			@RequestParam(defaultValue = "10") int size, // Default size is 10
			@RequestParam(defaultValue = "collectionTime") String sort // Changed default sort to collectionTime
	) {
		// Ensure the sort parameter matches a field in WasteLogResponseDTO
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
		Page<WasteLogResponseDTO> reportData = wasteLogService.getWasteCollectionReportByWorker(id, startDate, endDate, zoneId, vehicleId, pageable);

		return new ResponseEntity<>(reportData, HttpStatus.OK);
	}

	@GetMapping("/admin/reports/waste-collection/pdf")
	public ResponseEntity<byte[]> downloadWasteCollectionReportPdf(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false) Long zoneId,
			@RequestParam(required = false) Long vehicleId,
			@RequestParam(required = false) Long workerId) {

		List<WasteLogResponseDTO> reportData = pdfReportService.getWasteCollectionReportPdf(startDate, endDate, zoneId, vehicleId, workerId);

		byte[] pdfBytes = pdfReportService.generateWasteLogReport(reportData);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("attachment", "WasteCollectionReport.pdf");

		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}
	@GetMapping("/worker/reports/waste-collection/{w_id}/pdf")
	public ResponseEntity<byte[]> downloadWasteCollectionReportByWorkerPdf(
			@PathVariable Long w_id,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false) Long zoneId,
			@RequestParam(required = false) Long vehicleId
			) {

		List<WasteLogResponseDTO> reportData = pdfReportService.getWasteCollectionReportPdf(startDate, endDate, zoneId, vehicleId, w_id);

		byte[] pdfBytes = pdfReportService.generateWasteLogReport(reportData);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("attachment", "WasteCollectionReport.pdf");

		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}

	@PostMapping("/admin/store")
	public void storeVehicleIdAndWorkers(@RequestBody VehicleAndWorkerResponseDTO vehicleAndWorkerResponseDTO){
		wasteLogService.storeVehicleIdAndWorkers(vehicleAndWorkerResponseDTO);
	}
	@DeleteMapping("/admin/vehicle/{vehicleId}")
	ResponseEntity<List<Long>> deleteLogDataOfWorkerAndVehicleId(@PathVariable Long vehicleId) {
		return ResponseEntity.ok(wasteLogService.deleteLogDataOfWorkerAndVehicleId(vehicleId));
	}
	
	@PutMapping("/admin/vehicle/{prevVehicleId}/updateto/{newVehicleId}")
	ResponseEntity<List<Long>> updateLogDataOfWorkerAndVehicleId(@PathVariable("prevVehicleId") Long prevvehicleId,@PathVariable("newVehicleId") Long newvehicleId){
		return ResponseEntity.ok(wasteLogService.updateLogDataOfWorkerAndVehicleId(prevvehicleId,newvehicleId));
	}
	
	@DeleteMapping("/admin/zones/{zoneId}")
	public void deleteLogDataOfWorkerByZoneId(@PathVariable Long zoneId) {
		wasteLogService.deleteLogDataOfWorkerByZoneId(zoneId);
	}
	@PostMapping("/worker/vehicle/{vehicleId}/updatestatus")
	public String updateVehicleLiveStatus(@PathVariable Long vehicleId) {
		return wasteLogService.updateVehicleLiveStatus(vehicleId);
	}
	@GetMapping("/worker/vehicle/{vehicleId}/getstatus")
	public String presentVehicleStatus(@PathVariable Long vehicleId) {
		return wasteLogService.presentVehicleStatus(vehicleId);
	}
 
}
