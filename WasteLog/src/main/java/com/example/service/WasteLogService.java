////package com.example.service;
////
////import java.time.LocalDate;
////import java.time.LocalDateTime;
////import java.util.ArrayList;
////import java.util.List;
////import java.util.stream.Collectors;
////
////import org.springframework.data.domain.Page;
////import org.springframework.data.domain.PageImpl;
////import org.springframework.data.domain.Pageable;
////import org.springframework.stereotype.Service;
////
////import com.example.dto.AssignmentResponseDTO;
////import com.example.dto.VehicleAndWorkerResponseDTO;
////import com.example.dto.WasteLogRequestDTO;
////import com.example.dto.WasteLogResponseDTO;
////import com.example.dto.WorkerAssignmentResponseDTO;
////import com.example.exception.InvalidRequestException;
////import com.example.exception.MissingDateRangeException;
////import com.example.exception.ResourceNotFoundException;
////import com.example.feign.VehicleServiceFeignClient;
////import com.example.model.WasteLog;
////import com.example.repository.WasteLogRepository;
////
////import jakarta.validation.Valid;
////import lombok.RequiredArgsConstructor;
////import lombok.extern.slf4j.Slf4j;
////
////@Service
////@RequiredArgsConstructor
////@Slf4j
////public class WasteLogService {
////
////	private final WasteLogRepository wasteLogRepository;
////
////	private final VehicleServiceFeignClient vehicleServiceFeignClient;
////
////	public WasteLogResponseDTO createWasteLog(WasteLogRequestDTO requestDTO) {
////		log.info("Received request to create a waste log: {}", requestDTO);
////
////		if (requestDTO.getCollectionTime() == null) {
////			requestDTO.setCollectionTime(LocalDateTime.now().withNano(0));
////		}
////
////		WasteLog wasteLog = new WasteLog();
////		wasteLog.setZoneId(requestDTO.getZoneId());
////		wasteLog.setVehicleId(requestDTO.getVehicleId());
////		wasteLog.setWorkerId(requestDTO.getWorkerId());
////		wasteLog.setWeightCollected(requestDTO.getWeightCollected());
////		wasteLog.setCollectionTime(requestDTO.getCollectionTime());
////
////		WasteLog savedWasteLog = wasteLogRepository.save(wasteLog);
////
////		log.info("Successfully created waste log with ID: {}", savedWasteLog.getLogId());
////		WasteLogResponseDTO response = new WasteLogResponseDTO();
////		response.setLogId(savedWasteLog.getLogId());
////		response.setZoneId(savedWasteLog.getZoneId());
////		response.setVehicleId(savedWasteLog.getVehicleId());
////		response.setWorkerId(savedWasteLog.getWorkerId());
////		response.setWeightCollected(savedWasteLog.getWeightCollected());
////		response.setCollectionTime(savedWasteLog.getCollectionTime());
////		return response;
////	}
////
////	public Page<WasteLogResponseDTO> getWasteCollectionReport(LocalDate startDate, LocalDate endDate, Long zoneId,
////			Long vehicleId, Long workerId, Pageable pageable) {
////
////		log.info(
////				"Fetching waste collection report with filters - StartDate: {}, EndDate: {}, ZoneId: {}, VehicleId: {}, WorkerId: {}",
////				startDate, endDate, zoneId, vehicleId, workerId);
////
////		if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
////			log.warn("Invalid date range: Only one of StartDate or EndDate is provided.");
////			throw new MissingDateRangeException("Both startDate and endDate must be provided together.");
////		}
////
////		if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
////			log.warn("Invalid date range: StartDate ({}) is after EndDate ({})", startDate, endDate);
////			throw new InvalidRequestException("Start date must be before end date.");
////		}
////
////		Page<WasteLog> wasteLogs;
////
////		if (startDate != null && endDate != null && zoneId != null) {
////			LocalDateTime startDateTime = startDate.atStartOfDay();
////			LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
////			wasteLogs = wasteLogRepository.findByCollectionTimeBetweenAndZoneId(startDateTime, endDateTime, zoneId,
////					pageable);
////		} else if (startDate != null && endDate != null) {
////			LocalDateTime startDateTime = startDate.atStartOfDay();
////			LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
////			wasteLogs = wasteLogRepository.findByCollectionTimeBetween(startDateTime, endDateTime, pageable);
////		} else if (zoneId != null && vehicleId != null) {
////			wasteLogs = wasteLogRepository.findByZoneIdAndVehicleId(zoneId, vehicleId, pageable);
////		} else if (zoneId != null) {
////			wasteLogs = wasteLogRepository.findByZoneId(zoneId, pageable);
////		} else if (vehicleId != null && workerId != null) {
////			wasteLogs = wasteLogRepository.findByVehicleIdAndWorkerId(vehicleId, workerId, pageable);
////		} else if (vehicleId != null) {
////			wasteLogs = wasteLogRepository.findByVehicleId(vehicleId, pageable);
////		} else if (workerId != null) {
////			wasteLogs = wasteLogRepository.findByWorkerId(workerId, pageable);
////		} else {
////			wasteLogs = wasteLogRepository.findAll(pageable);
////		}
////
////// Filter out records where collectionTime or weightCollected are null
////		List<WasteLogResponseDTO> filteredResponses = wasteLogs.stream()
////				.filter(wasteLog -> wasteLog.getCollectionTime() != null && wasteLog.getWeightCollected() != null)
////				.map(wasteLog -> {
////					WasteLogResponseDTO response = new WasteLogResponseDTO();
////					response.setLogId(wasteLog.getLogId());
////					response.setZoneId(wasteLog.getZoneId());
////					response.setVehicleId(wasteLog.getVehicleId());
////					response.setWorkerId(wasteLog.getWorkerId());
////					response.setWeightCollected(wasteLog.getWeightCollected());
////					response.setCollectionTime(wasteLog.getCollectionTime());
////					return response;
////				}).collect(Collectors.toList());
////
////		if (filteredResponses.isEmpty()) {
////			log.warn(
////					"No waste collection records found for the given filters with non-null collection time and weight collected.");
////			throw new ResourceNotFoundException("No waste collection records found for the given filters.");
////		}
////
////// Reconstruct Page object with filtered content
////		Page<WasteLogResponseDTO> resultPage = new PageImpl<>(filteredResponses, pageable, filteredResponses.size());
////
////		log.info(
////				"Successfully fetched {} waste collection records (after filtering for non-null collection time and weight collected)",
////				resultPage.getTotalElements());
////
////		return resultPage;
////	}
////
////	public Page<WasteLogResponseDTO> getWasteCollectionReportByWorker(Long workerId, LocalDate startDate,
////			LocalDate endDate, Long zoneId, Long vehicleId, Pageable pageable) {
////
////		log.info(
////				"Fetching waste collection report for worker ID: {} with filters - StartDate: {}, EndDate: {}, ZoneId: {}, VehicleId: {}",
////				workerId, startDate, endDate, zoneId, vehicleId);
////
////		if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
////			log.warn("Invalid date range: Only one of StartDate or EndDate is provided.");
////			throw new MissingDateRangeException("Both startDate and endDate must be provided together.");
////		}
////
////		if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
////			log.warn("Invalid date range: StartDate ({}) is after EndDate ({})", startDate, endDate);
////			throw new InvalidRequestException("Start date must be before end date.");
////		}
////
////		Page<WasteLog> workerLogs;
////
////		if (workerId != null && zoneId != null && vehicleId != null) {
////			workerLogs = wasteLogRepository.findByWorkerIdAndZoneIdAndVehicleId(workerId, zoneId, vehicleId, pageable);
////		}
////
////		if (startDate != null && endDate != null) {
////			LocalDateTime startDateTime = startDate.atStartOfDay();
////			LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
////
////			if (zoneId != null && vehicleId != null) {
////				workerLogs = wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndZoneIdAndVehicleId(workerId,
////						startDateTime, endDateTime, zoneId, vehicleId, pageable);
////			} else if (zoneId != null) {
////				workerLogs = wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndZoneId(workerId, startDateTime,
////						endDateTime, zoneId, pageable);
////			} else if (vehicleId != null) {
////				workerLogs = wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndVehicleId(workerId,
////						startDateTime, endDateTime, vehicleId, pageable);
////			} else {
////				workerLogs = wasteLogRepository.findByWorkerIdAndCollectionTimeBetween(workerId, startDateTime,
////						endDateTime, pageable);
////			}
////		} else {
////			workerLogs = wasteLogRepository.findByWorkerId(workerId, pageable);
////		}
////
////// Filter out records where collectionTime or weightCollected are null
////		List<WasteLogResponseDTO> filteredResponses = workerLogs.stream()
////				.filter(wasteLog -> wasteLog.getCollectionTime() != null && wasteLog.getWeightCollected() != null)
////				.map(wasteLog -> {
////					WasteLogResponseDTO response = new WasteLogResponseDTO();
////					response.setLogId(wasteLog.getLogId());
////					response.setZoneId(wasteLog.getZoneId());
////					response.setVehicleId(wasteLog.getVehicleId());
////					response.setWorkerId(wasteLog.getWorkerId());
////					response.setWeightCollected(wasteLog.getWeightCollected());
////					response.setCollectionTime(wasteLog.getCollectionTime());
////					return response;
////				}).collect(Collectors.toList());
////
////		if (filteredResponses.isEmpty()) {
////			log.warn(
////					"No waste collection records found for worker ID: {} with the given filters and non-null collection time and weight collected.",
////					workerId);
////			throw new ResourceNotFoundException("No waste collection records found for the given filters.");
////		}
////
////// Reconstruct Page object with filtered content
////		Page<WasteLogResponseDTO> resultPage = new PageImpl<>(filteredResponses, pageable, filteredResponses.size());
////
////		log.info(
////				"Successfully fetched {} waste collection records for worker ID: {} (after filtering for non-null collection time and weight collected)",
////				resultPage.getTotalElements(), workerId);
////
////		return resultPage;
////	}
////
////	public void storeVehicleIdAndWorkers(VehicleAndWorkerResponseDTO vehicleAndWorkerResponseDTO) {
////		System.out.println(vehicleAndWorkerResponseDTO.getListOfWorkersAssigned());
////		List<AssignmentResponseDTO> workerAssignment = vehicleAndWorkerResponseDTO.getListOfWorkersAssigned();
////		if (!workerAssignment.isEmpty()) {
////			for (AssignmentResponseDTO assignments : workerAssignment) {
////				WasteLog wasteLog = new WasteLog();
////				wasteLog.setVehicleId(vehicleAndWorkerResponseDTO.getVehicleId());
////				wasteLog.setWorkerId(assignments.getWorkerId());
////				wasteLog.setZoneId(assignments.getZoneId());
////				wasteLogRepository.save(wasteLog);
////			}
////		}
////	}
////
////	public WasteLogResponseDTO enterLogData(Long workerId, Double weightCollected, LocalDateTime collectionTime) {
////		WasteLog getLogDetail = wasteLogRepository.findByWorkerIdAndWeightCollectedIsNUll(workerId);
////		getLogDetail.setCollectionTime(collectionTime);
////		getLogDetail.setWeightCollected(weightCollected);
////		wasteLogRepository.save(getLogDetail);
////		return new WasteLogResponseDTO(getLogDetail.getLogId(), getLogDetail.getZoneId(), getLogDetail.getVehicleId(),
////				getLogDetail.getWorkerId(), getLogDetail.getWeightCollected(), getLogDetail.getCollectionTime());
////	}
////	public WorkerAssignmentResponseDTO fetchWorkerDetails(@Valid Long workerId) {
////		List<WasteLog> logdata=wasteLogRepository.findByWorkerId(workerId);
////		if(logdata.isEmpty()) {
////			throw new ResourceNotFoundException("No waste collection records found for the given worker.");
////		}
////		
////		WorkerAssignmentResponseDTO worker=new WorkerAssignmentResponseDTO();
////		worker.setWorkerId(workerId);
////		worker.setZoneId(logdata.get(logdata.size()-1).getZoneId());
////		worker.setVehicleId(logdata.get(logdata.size()-1).getVehicleId());
////		worker.setWeightCollected(logdata.get(logdata.size()-1).getWeightCollected());
////		
////		return worker;
////	}
////	public List<Long> deleteLogDataOfWorkerAndVehicleId(Long vehicleId) {
////		List<WasteLog> getLogDetail = wasteLogRepository.findByVehicleIdAndWeightCollectedIsNUll(vehicleId);
////		List<Long> l = new ArrayList<>();
////		for (WasteLog w : getLogDetail) {
////			l.add(w.getWorkerId());
////			wasteLogRepository.deleteById(w.getLogId());
////		}
////		return l;
////	}
////
////	public void deleteLogDataOfWorkerByZoneId(Long zoneId) {
////		wasteLogRepository.deleteAllByZoneIdAndWeightCollectedIsNull(zoneId);
////	}
////
////	public void updateVehicleLiveStatus(Long VehicleId) {
////		vehicleServiceFeignClient.updateVehicleStatus(VehicleId);
////	}
////
////}
//package com.example.service;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import com.example.dto.AssignmentResponseDTO;
//import com.example.dto.VehicleAndWorkerResponseDTO;
//import com.example.dto.WasteLogRequestDTO;
//import com.example.dto.WasteLogResponseDTO;
//import com.example.dto.WorkerAssignmentResponseDTO;
//import com.example.exception.InvalidRequestException;
//import com.example.exception.MissingDateRangeException;
//import com.example.exception.ResourceNotFoundException;
//import com.example.feign.VehicleServiceFeignClient;
//import com.example.model.WasteLog;
//import com.example.repository.WasteLogRepository;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class WasteLogService {
//
//	private final WasteLogRepository wasteLogRepository;
//
//	private final VehicleServiceFeignClient vehicleServiceFeignClient;
//
//	public WasteLogResponseDTO createWasteLog(WasteLogRequestDTO requestDTO) {
//		log.info("Received request to create a waste log: {}", requestDTO);
//
//		if (requestDTO.getCollectionTime() == null) {
//			requestDTO.setCollectionTime(LocalDateTime.now().withNano(0));
//		}
//
//		WasteLog wasteLog = new WasteLog();
//		wasteLog.setZoneId(requestDTO.getZoneId());
//		wasteLog.setVehicleId(requestDTO.getVehicleId());
//		wasteLog.setWorkerId(requestDTO.getWorkerId());
//		wasteLog.setWeightCollected(requestDTO.getWeightCollected());
//		wasteLog.setCollectionTime(requestDTO.getCollectionTime());
//
//		WasteLog savedWasteLog = wasteLogRepository.save(wasteLog);
//
//		log.info("Successfully created waste log with ID: {}", savedWasteLog.getLogId());
//		WasteLogResponseDTO response = new WasteLogResponseDTO();
//		response.setLogId(savedWasteLog.getLogId());
//		response.setZoneId(savedWasteLog.getZoneId());
//		response.setVehicleId(savedWasteLog.getVehicleId());
//		response.setWorkerId(savedWasteLog.getWorkerId());
//		response.setWeightCollected(savedWasteLog.getWeightCollected());
//		response.setCollectionTime(savedWasteLog.getCollectionTime());
//		return response;
//	}
//
//	public Page<WasteLogResponseDTO> getWasteCollectionReport(LocalDate startDate, LocalDate endDate, Long zoneId,
//			Long vehicleId, Long workerId, Pageable pageable) {
//
//		log.info(
//				"Fetching waste collection report with filters - StartDate: {}, EndDate: {}, ZoneId: {}, VehicleId: {}, WorkerId: {}",
//				startDate, endDate, zoneId, vehicleId, workerId);
//
//		if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
//			log.warn("Invalid date range: Only one of StartDate or EndDate is provided.");
//			throw new MissingDateRangeException("Both startDate and endDate must be provided together.");
//		}
//
//		if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
//			log.warn("Invalid date range: StartDate ({}) is after EndDate ({})", startDate, endDate);
//			throw new InvalidRequestException("Start date must be before end date.");
//		}
//
//		Page<WasteLog> wasteLogs;
//
//		if (startDate != null && endDate != null && zoneId != null) {
//			LocalDateTime startDateTime = startDate.atStartOfDay();
//			LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
//			wasteLogs = wasteLogRepository.findByCollectionTimeBetweenAndZoneId(startDateTime, endDateTime, zoneId,
//					pageable);
//		} else if (startDate != null && endDate != null) {
//			LocalDateTime startDateTime = startDate.atStartOfDay();
//			LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
//			wasteLogs = wasteLogRepository.findByCollectionTimeBetween(startDateTime, endDateTime, pageable);
//		} else if (zoneId != null && vehicleId != null) {
//			wasteLogs = wasteLogRepository.findByZoneIdAndVehicleId(zoneId, vehicleId, pageable);
//		} else if (zoneId != null) {
//			wasteLogs = wasteLogRepository.findByZoneId(zoneId, pageable);
//		} else if (vehicleId != null && workerId != null) {
//			wasteLogs = wasteLogRepository.findByVehicleIdAndWorkerId(vehicleId, workerId, pageable);
//		} else if (vehicleId != null) {
//			wasteLogs = wasteLogRepository.findByVehicleId(vehicleId, pageable);
//		} else if (workerId != null) {
//			wasteLogs = wasteLogRepository.findByWorkerId(workerId, pageable);
//		} else {
//			wasteLogs = wasteLogRepository.findAll(pageable);
//		}
//
//		// Filter out records where collectionTime or weightCollected are null
//		List<WasteLogResponseDTO> filteredResponses = wasteLogs.stream()
//				.filter(wasteLog -> wasteLog.getCollectionTime() != null && wasteLog.getWeightCollected() != null)
//				.map(wasteLog -> {
//					WasteLogResponseDTO response = new WasteLogResponseDTO();
//					response.setLogId(wasteLog.getLogId());
//					response.setZoneId(wasteLog.getZoneId());
//					response.setVehicleId(wasteLog.getVehicleId());
//					response.setWorkerId(wasteLog.getWorkerId());
//					response.setWeightCollected(wasteLog.getWeightCollected());
//					response.setCollectionTime(wasteLog.getCollectionTime());
//					return response;
//				}).collect(Collectors.toList());
//
//		// IMPORTANT FIX: Reconstruct Page object with filtered content,
//		// but use the total elements from the original page (wasteLogs.getTotalElements())
//		// to ensure correct totalPages calculation for pagination.
//		Page<WasteLogResponseDTO> resultPage = new PageImpl<>(filteredResponses, pageable, wasteLogs.getTotalElements());
//
//		if (resultPage.isEmpty() && wasteLogs.getTotalElements() > 0) {
//			log.warn("No waste collection records found on this page after filtering for non-null collection time and weight collected, but total elements exist.");
//			// It's generally better to return an empty page here rather than throwing an exception
//			// if there might be data on other pages or if the current page just happened to have all nulls.
//		} else if (resultPage.isEmpty() && wasteLogs.getTotalElements() == 0) {
//			log.warn("No waste collection records found for the given filters.");
//			throw new ResourceNotFoundException("No waste collection records found for the given filters.");
//		}
//
//
//		log.info(
//				"Successfully fetched {} waste collection records (after filtering for non-null collection time and weight collected) out of total {} matching records.",
//				resultPage.getNumberOfElements(), resultPage.getTotalElements());
//
//		return resultPage;
//	}
//
//	public Page<WasteLogResponseDTO> getWasteCollectionReportByWorker(Long workerId, LocalDate startDate,
//			LocalDate endDate, Long zoneId, Long vehicleId, Pageable pageable) {
//
//		log.info(
//				"Fetching waste collection report for worker ID: {} with filters - StartDate: {}, EndDate: {}, ZoneId: {}, VehicleId: {}",
//				workerId, startDate, endDate, zoneId, vehicleId);
//
//		if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
//			log.warn("Invalid date range: Only one of StartDate or EndDate is provided.");
//			throw new MissingDateRangeException("Both startDate and endDate must be provided together.");
//		}
//
//		if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
//			log.warn("Invalid date range: StartDate ({}) is after EndDate ({})", startDate, endDate);
//			throw new InvalidRequestException("Start date must be before end date.");
//		}
//
//		Page<WasteLog> workerLogs;
//
//		// Re-evaluating the logic for combining filters to ensure correct repository calls
//		if (startDate != null && endDate != null) {
//			LocalDateTime startDateTime = startDate.atStartOfDay();
//			LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
//
//			if (zoneId != null && vehicleId != null) {
//				workerLogs = wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndZoneIdAndVehicleId(workerId,
//						startDateTime, endDateTime, zoneId, vehicleId, pageable);
//			} else if (zoneId != null) {
//				workerLogs = wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndZoneId(workerId, startDateTime,
//						endDateTime, zoneId, pageable);
//			} else if (vehicleId != null) {
//				workerLogs = wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndVehicleId(workerId,
//						startDateTime, endDateTime, vehicleId, pageable);
//			} else {
//				workerLogs = wasteLogRepository.findByWorkerIdAndCollectionTimeBetween(workerId, startDateTime,
//						endDateTime, pageable);
//			}
//		} else if (zoneId != null && vehicleId != null) {
//			workerLogs = wasteLogRepository.findByWorkerIdAndZoneIdAndVehicleId(workerId, zoneId, vehicleId, pageable);
//		} else if (zoneId != null) {
//			workerLogs = wasteLogRepository.findByWorkerIdAndZoneId(workerId, zoneId, pageable);
//		} else if (vehicleId != null) {
//			workerLogs = wasteLogRepository.findByWorkerIdAndVehicleId(workerId, vehicleId, pageable);
//		} else {
//			workerLogs = wasteLogRepository.findByWorkerId(workerId, pageable);
//		}
//
//
//		// Filter out records where collectionTime or weightCollected are null
//		List<WasteLogResponseDTO> filteredResponses = workerLogs.stream()
//				.filter(wasteLog -> wasteLog.getCollectionTime() != null && wasteLog.getWeightCollected() != null)
//				.map(wasteLog -> {
//					WasteLogResponseDTO response = new WasteLogResponseDTO();
//					response.setLogId(wasteLog.getLogId());
//					response.setZoneId(wasteLog.getZoneId());
//					response.setVehicleId(wasteLog.getVehicleId());
//					response.setWorkerId(wasteLog.getWorkerId());
//					response.setWeightCollected(wasteLog.getWeightCollected());
//					response.setCollectionTime(wasteLog.getCollectionTime());
//					return response;
//				}).collect(Collectors.toList());
//
//		// IMPORTANT FIX: Reconstruct Page object with filtered content,
//		// but use the total elements from the original page (workerLogs.getTotalElements())
//		// to ensure correct totalPages calculation for pagination.
//		Page<WasteLogResponseDTO> resultPage = new PageImpl<>(filteredResponses, pageable, workerLogs.getTotalElements());
//
//		if (resultPage.isEmpty() && workerLogs.getTotalElements() > 0) {
//			log.warn("No waste collection records found on this page for worker ID: {} after filtering for non-null collection time and weight collected, but total elements exist.", workerId);
//		} else if (resultPage.isEmpty() && workerLogs.getTotalElements() == 0) {
//			log.warn("No waste collection records found for worker ID: {} with the given filters.", workerId);
//			throw new ResourceNotFoundException("No waste collection records found for the given filters.");
//		}
//
//
//		log.info(
//				"Successfully fetched {} waste collection records for worker ID: {} (after filtering for non-null collection time and weight collected) out of total {} matching records.",
//				resultPage.getNumberOfElements(), resultPage.getTotalElements(), workerId);
//
//		return resultPage;
//	}
//
//	public void storeVehicleIdAndWorkers(VehicleAndWorkerResponseDTO vehicleAndWorkerResponseDTO) {
//		System.out.println(vehicleAndWorkerResponseDTO.getListOfWorkersAssigned());
//		List<AssignmentResponseDTO> workerAssignment = vehicleAndWorkerResponseDTO.getListOfWorkersAssigned();
//		if (!workerAssignment.isEmpty()) {
//			for (AssignmentResponseDTO assignments : workerAssignment) {
//				WasteLog wasteLog = new WasteLog();
//				wasteLog.setVehicleId(vehicleAndWorkerResponseDTO.getVehicleId());
//				wasteLog.setWorkerId(assignments.getWorkerId());
//				wasteLog.setZoneId(assignments.getZoneId());
//				wasteLogRepository.save(wasteLog);
//			}
//		}
//	}
//
//	public WasteLogResponseDTO enterLogData(Long workerId, Double weightCollected, LocalDateTime collectionTime) {
//		WasteLog getLogDetail = wasteLogRepository.findByWorkerIdAndWeightCollectedIsNUll(workerId);
//		getLogDetail.setCollectionTime(collectionTime);
//		getLogDetail.setWeightCollected(weightCollected);
//		wasteLogRepository.save(getLogDetail);
//		return new WasteLogResponseDTO(getLogDetail.getLogId(), getLogDetail.getZoneId(), getLogDetail.getVehicleId(),
//				getLogDetail.getWorkerId(), getLogDetail.getWeightCollected(), getLogDetail.getCollectionTime());
//	}
//	public WorkerAssignmentResponseDTO fetchWorkerDetails(@Valid Long workerId) {
//		List<WasteLog> logdata=wasteLogRepository.findByWorkerId(workerId);
//		if(logdata.isEmpty()) {
//			throw new ResourceNotFoundException("No waste collection records found for the given worker.");
//		}
//
//		WorkerAssignmentResponseDTO worker=new WorkerAssignmentResponseDTO();
//		worker.setWorkerId(workerId);
//		worker.setZoneId(logdata.get(logdata.size()-1).getZoneId());
//		worker.setVehicleId(logdata.get(logdata.size()-1).getVehicleId());
//		worker.setWeightCollected(logdata.get(logdata.size()-1).getWeightCollected());
//
//		return worker;
//	}
//	public List<Long> deleteLogDataOfWorkerAndVehicleId(Long vehicleId) {
//		List<WasteLog> getLogDetail = wasteLogRepository.findByVehicleIdAndWeightCollectedIsNUll(vehicleId);
//		List<Long> l = new ArrayList<>();
//		for (WasteLog w : getLogDetail) {
//			l.add(w.getWorkerId());
//			wasteLogRepository.deleteById(w.getLogId());
//		}
//		return l;
//	}
//
//	public void deleteLogDataOfWorkerByZoneId(Long zoneId) {
//		wasteLogRepository.deleteAllByZoneIdAndWeightCollectedIsNull(zoneId);
//	}
//
//	public void updateVehicleLiveStatus(Long VehicleId) {
//		vehicleServiceFeignClient.updateVehicleStatus(VehicleId);
//	}
//
//}
package com.example.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.dto.AssignmentResponseDTO;
import com.example.dto.VehicleAndWorkerResponseDTO;
import com.example.dto.WasteLogRequestDTO;
import com.example.dto.WasteLogResponseDTO;
import com.example.dto.WorkerAssignmentResponseDTO;
import com.example.dto.WorkerResponseDTO;
import com.example.exception.InvalidRequestException;
import com.example.exception.MissingDateRangeException;
import com.example.exception.ResourceNotFoundException;
import com.example.feign.VehicleServiceFeignClient;
import com.example.feign.WorkerServiceFeignClient;
import com.example.model.WasteLog;
import com.example.repository.WasteLogRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WasteLogService {

	private final WasteLogRepository wasteLogRepository;

	private final VehicleServiceFeignClient vehicleServiceFeignClient;
	
	private final WorkerServiceFeignClient workerServiceFeignClient;

	public WasteLogResponseDTO createWasteLog(WasteLogRequestDTO requestDTO) {
		log.info("Received request to create a waste log: {}", requestDTO);

		if (requestDTO.getCollectionTime() == null) {
			requestDTO.setCollectionTime(LocalDateTime.now().withNano(0));
		}

		WasteLog wasteLog = new WasteLog();
		wasteLog.setZoneId(requestDTO.getZoneId());
		wasteLog.setVehicleId(requestDTO.getVehicleId());
		wasteLog.setWorkerId(requestDTO.getWorkerId());
		wasteLog.setWeightCollected(requestDTO.getWeightCollected());
		wasteLog.setCollectionTime(requestDTO.getCollectionTime());

		WasteLog savedWasteLog = wasteLogRepository.save(wasteLog);

		log.info("Successfully created waste log with ID: {}", savedWasteLog.getLogId());
		WasteLogResponseDTO response = new WasteLogResponseDTO();
		response.setLogId(savedWasteLog.getLogId());
		response.setZoneId(savedWasteLog.getZoneId());
		response.setVehicleId(savedWasteLog.getVehicleId());
		response.setWorkerId(savedWasteLog.getWorkerId());
		response.setWeightCollected(savedWasteLog.getWeightCollected());
		response.setCollectionTime(savedWasteLog.getCollectionTime());
		return response;
	}

	public Page<WasteLogResponseDTO> getWasteCollectionReport(LocalDate startDate, LocalDate endDate, Long zoneId,
			Long vehicleId, Long workerId, Pageable pageable) {

		log.info(
				"Fetching waste collection report with filters - StartDate: {}, EndDate: {}, ZoneId: {}, VehicleId: {}, WorkerId: {}",
				startDate, endDate, zoneId, vehicleId, workerId);

		if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
			log.warn("Invalid date range: Only one of StartDate or EndDate is provided.");
			throw new MissingDateRangeException("Both startDate and endDate must be provided together.");
		}

		if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
			log.warn("Invalid date range: StartDate ({}) is after EndDate ({})", startDate, endDate);
			throw new InvalidRequestException("Start date must be before end date.");
		}

		Page<WasteLog> wasteLogs;

		if (startDate != null && endDate != null && zoneId != null) {
			LocalDateTime startDateTime = startDate.atStartOfDay();
			LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
			wasteLogs = wasteLogRepository.findByCollectionTimeBetweenAndZoneId(startDateTime, endDateTime, zoneId,
					pageable);
		} else if (startDate != null && endDate != null) {
			LocalDateTime startDateTime = startDate.atStartOfDay();
			LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
			wasteLogs = wasteLogRepository.findByCollectionTimeBetween(startDateTime, endDateTime, pageable);
		} else if (zoneId != null && vehicleId != null) {
			wasteLogs = wasteLogRepository.findByZoneIdAndVehicleId(zoneId, vehicleId, pageable);
		} else if (zoneId != null) {
			wasteLogs = wasteLogRepository.findByZoneId(zoneId, pageable);
		} else if (vehicleId != null && workerId != null) {
			wasteLogs = wasteLogRepository.findByVehicleIdAndWorkerId(vehicleId, workerId, pageable);
		} else if (vehicleId != null) {
			wasteLogs = wasteLogRepository.findByVehicleId(vehicleId, pageable);
		} else if (workerId != null) {
			wasteLogs = wasteLogRepository.findByWorkerId(workerId, pageable);
		} else {
			wasteLogs = wasteLogRepository.findAll(pageable);
		}

		// Filter out records where collectionTime or weightCollected are null
		List<WasteLogResponseDTO> filteredResponses = wasteLogs.stream()
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
				}).collect(Collectors.toList());

		// IMPORTANT FIX: Reconstruct Page object with filtered content,
		// but use the total elements from the original page (wasteLogs.getTotalElements())
		// to ensure correct totalPages calculation for pagination.
		Page<WasteLogResponseDTO> resultPage = new PageImpl<>(filteredResponses, pageable, wasteLogs.getTotalElements());

		if (resultPage.isEmpty() && wasteLogs.getTotalElements() > 0) {
			log.warn("No waste collection records found on this page after filtering for non-null collection time and weight collected, but total elements exist.");
			// It's generally better to return an empty page here rather than throwing an exception
			// if there might be data on other pages or if the current page just happened to have all nulls.
		} else if (resultPage.isEmpty() && wasteLogs.getTotalElements() == 0) {
			log.warn("No waste collection records found for the given filters.");
			throw new ResourceNotFoundException("No waste collection records found for the given filters.");
		}


		log.info(
				"Successfully fetched {} waste collection records (after filtering for non-null collection time and weight collected) out of total {} matching records.",
				resultPage.getNumberOfElements(), resultPage.getTotalElements());

		return resultPage;
	}

	public Page<WasteLogResponseDTO> getWasteCollectionReportByWorker(Long workerId, LocalDate startDate,
			LocalDate endDate, Long zoneId, Long vehicleId, Pageable pageable) {

		log.info(
				"Fetching waste collection report for worker ID: {} with filters - StartDate: {}, EndDate: {}, ZoneId: {}, VehicleId: {}",
				workerId, startDate, endDate, zoneId, vehicleId);

		if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
			log.warn("Invalid date range: Only one of StartDate or EndDate is provided.");
			throw new MissingDateRangeException("Both startDate and endDate must be provided together.");
		}

		if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
			log.warn("Invalid date range: StartDate ({}) is after EndDate ({})", startDate, endDate);
			throw new InvalidRequestException("Start date must be before end date.");
		}

		Page<WasteLog> workerLogs;

		// Re-evaluating the logic for combining filters to ensure correct repository calls
		if (startDate != null && endDate != null) {
			LocalDateTime startDateTime = startDate.atStartOfDay();
			LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

			if (zoneId != null && vehicleId != null) {
				workerLogs = wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndZoneIdAndVehicleId(workerId,
						startDateTime, endDateTime, zoneId, vehicleId, pageable);
			} else if (zoneId != null) {
				workerLogs = wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndZoneId(workerId, startDateTime,
						endDateTime, zoneId, pageable);
			} else if (vehicleId != null) {
				workerLogs = wasteLogRepository.findByWorkerIdAndCollectionTimeBetweenAndVehicleId(workerId,
						startDateTime, endDateTime, vehicleId, pageable);
			} else {
				workerLogs = wasteLogRepository.findByWorkerIdAndCollectionTimeBetween(workerId, startDateTime,
						endDateTime, pageable);
			}
		} else if (zoneId != null && vehicleId != null) {
			workerLogs = wasteLogRepository.findByWorkerIdAndZoneIdAndVehicleId(workerId, zoneId, vehicleId, pageable);
		} else if (zoneId != null) {
			workerLogs = wasteLogRepository.findByWorkerIdAndZoneId(workerId, zoneId, pageable);
		} else if (vehicleId != null) {
			workerLogs = wasteLogRepository.findByWorkerIdAndVehicleId(workerId, vehicleId, pageable);
		} else {
			workerLogs = wasteLogRepository.findByWorkerId(workerId, pageable);
		}


		// Filter out records where collectionTime or weightCollected are null
		List<WasteLogResponseDTO> filteredResponses = workerLogs.stream()
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
				}).collect(Collectors.toList());

		// IMPORTANT FIX: Reconstruct Page object with filtered content,
		// but use the total elements from the original page (workerLogs.getTotalElements())
		// to ensure correct totalPages calculation for pagination.
		Page<WasteLogResponseDTO> resultPage = new PageImpl<>(filteredResponses, pageable, workerLogs.getTotalElements());

		if (resultPage.isEmpty() && workerLogs.getTotalElements() > 0) {
			log.warn("No waste collection records found on this page for worker ID: {} after filtering for non-null collection time and weight collected, but total elements exist.", workerId);
		} else if (resultPage.isEmpty() && workerLogs.getTotalElements() == 0) {
			log.warn("No waste collection records found for worker ID: {} with the given filters.", workerId);
			throw new ResourceNotFoundException("No waste collection records found for the given filters.");
		}


		log.info(
				"Successfully fetched {} waste collection records for worker ID: {} (after filtering for non-null collection time and weight collected) out of total {} matching records.",
				resultPage.getNumberOfElements(), resultPage.getTotalElements(), workerId);

		return resultPage;
	}

	public void storeVehicleIdAndWorkers(VehicleAndWorkerResponseDTO vehicleAndWorkerResponseDTO) {
		System.out.println(vehicleAndWorkerResponseDTO.getListOfWorkersAssigned());
		List<AssignmentResponseDTO> workerAssignment = vehicleAndWorkerResponseDTO.getListOfWorkersAssigned();
		if (!workerAssignment.isEmpty()) {
			for (AssignmentResponseDTO assignments : workerAssignment) {
				WasteLog wasteLog = new WasteLog();
				wasteLog.setVehicleId(vehicleAndWorkerResponseDTO.getVehicleId());
				wasteLog.setWorkerId(assignments.getWorkerId());
				wasteLog.setZoneId(assignments.getZoneId());
				wasteLogRepository.save(wasteLog);
			}
		}
	}

	public WasteLogResponseDTO enterLogData(Long workerId, Double weightCollected, LocalDateTime collectionTime) {
		WasteLog getLogDetail = wasteLogRepository.findByWorkerIdAndWeightCollectedIsNUll(workerId);
		getLogDetail.setCollectionTime(collectionTime);
		getLogDetail.setWeightCollected(weightCollected);
		wasteLogRepository.save(getLogDetail);
		vehicleServiceFeignClient.isWasteLogged(getLogDetail.getVehicleId(),getLogDetail.getZoneId());
		return new WasteLogResponseDTO(getLogDetail.getLogId(), getLogDetail.getZoneId(), getLogDetail.getVehicleId(),
				getLogDetail.getWorkerId(), getLogDetail.getWeightCollected(), getLogDetail.getCollectionTime());
	}
	public WorkerAssignmentResponseDTO fetchWorkerDetails(@Valid Long workerId) {
		List<WasteLog> logdata=wasteLogRepository.findByWorkerId(workerId);
		if(logdata.isEmpty()) {
			throw new ResourceNotFoundException("No waste collection records found for the given worker.");
		}

		WorkerAssignmentResponseDTO worker=new WorkerAssignmentResponseDTO();
		worker.setWorkerId(workerId);
		worker.setZoneId(logdata.get(logdata.size()-1).getZoneId());
		worker.setVehicleId(logdata.get(logdata.size()-1).getVehicleId());
		worker.setWeightCollected(logdata.get(logdata.size()-1).getWeightCollected());

		return worker;
	}
	
	public List<Long> updateLogDataOfWorkerAndVehicleId(Long prevVehicleId,Long newVehicleId) {
		List<WasteLog> getLogDetail = wasteLogRepository.findByVehicleIdAndWeightCollectedIsNull(prevVehicleId);
		System.out.println("getLogDetail"+getLogDetail);
		LocalDateTime currentDateTime = LocalDateTime.now(); // or fetched from a database record
		LocalDate targetDate = currentDateTime.toLocalDate();
		LocalDateTime startOfDay = targetDate.atStartOfDay();
		LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);
		List<WasteLog> logs = wasteLogRepository.findByVehicleIdAndCollectionTimeBetween(prevVehicleId, startOfDay, endOfDay);
		List<Long> l = new ArrayList<>();
		if(getLogDetail != null &&!getLogDetail.isEmpty()) {
		for (WasteLog w : getLogDetail) {
			l.add(w.getWorkerId());
			System.out.println("W"+w);
			w.setVehicleId(newVehicleId);
			wasteLogRepository.save(w);
			System.out.println("WORK"+w);
		}
		}
		if(logs!=null && !logs.isEmpty()) {
		for(WasteLog w:logs) {
			l.add(w.getWorkerId());
			w.setVehicleId(newVehicleId);
			wasteLogRepository.save(w);
		}
		}
		return l;
	}
	
	public List<Long> deleteLogDataOfWorkerAndVehicleId(Long vehicleId) {
		List<WasteLog> getLogDetail = wasteLogRepository.findByVehicleIdAndWeightCollectedIsNull(vehicleId);
		LocalDateTime currentDateTime = LocalDateTime.now(); // or fetched from a database record
		LocalDate targetDate = currentDateTime.toLocalDate();
		LocalDateTime startOfDay = targetDate.atStartOfDay();
		LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);

		List<WasteLog> logs = wasteLogRepository.findByVehicleIdAndCollectionTimeBetween(vehicleId, startOfDay, endOfDay);

		List<Long> l = new ArrayList<>();
		if(getLogDetail != null &&!getLogDetail.isEmpty()) {
		for (WasteLog w : getLogDetail) {
			l.add(w.getWorkerId());
			wasteLogRepository.deleteById(w.getLogId());
		}
		}
		if(logs!=null && !logs.isEmpty()) {
		for(WasteLog w:logs) {
			l.add(w.getWorkerId());
		}
		}
		return l;
	}

	public void deleteLogDataOfWorkerByZoneId(Long zoneId) {
		wasteLogRepository.deleteAllByZoneIdAndWeightCollectedIsNull(zoneId);
	}

	public String updateVehicleLiveStatus(Long VehicleId) {
		String message=vehicleServiceFeignClient.updateVehicleStatus(VehicleId);
		System.out.println(message);
		return message;
	}

	public Long fetchWorkerDetailsByEmail(String emailId) {
		WorkerResponseDTO worker=workerServiceFeignClient.getWorkerByIdByEmail(emailId).getBody();
		return worker.getWorkerId();
	}
	
	public String presentVehicleStatus(Long vehicleId) {
		String message=vehicleServiceFeignClient.getVehicleStatus(vehicleId);
		System.out.println(message);
		return message;
	}
 

	

}
