package com.example.repository;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.WasteLog;

@Repository
public interface WasteLogRepository extends JpaRepository<WasteLog, Long> {

    
	Page<WasteLog> findByCollectionTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
	Page<WasteLog> findByZoneId(Long zoneId, Pageable pageable);
	Page<WasteLog> findByVehicleId(Long vehicleId, Pageable pageable);
	Page<WasteLog> findByWorkerId(Long workerId, Pageable pageable);
	Page<WasteLog> findByZoneIdAndVehicleId(Long zoneId, Long vehicleId, Pageable pageable);
	Page<WasteLog> findByCollectionTimeBetweenAndZoneId(LocalDateTime startDate, LocalDateTime endDate, Long zoneId, Pageable pageable);
	Page<WasteLog> findByVehicleIdAndWorkerId(Long vehicleId, Long workerId, Pageable pageable);
	Page<WasteLog> findByWorkerIdAndCollectionTimeBetweenAndZoneIdAndVehicleId(Long workerId, LocalDateTime startDateTime, LocalDateTime endDateTime, Long zoneId, Long vehicleId, Pageable pageable);
	Page<WasteLog> findByWorkerIdAndCollectionTimeBetweenAndZoneId(Long workerId, LocalDateTime startDateTime, LocalDateTime endDateTime, Long zoneId, Pageable pageable);
	Page<WasteLog> findByWorkerIdAndCollectionTimeBetween(Long workerId, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);
	Page<WasteLog> findByWorkerIdAndZoneId(Long workerId, Long zoneId, Pageable pageable);
	Page<WasteLog> findByWorkerIdAndZoneIdAndVehicleId(Long workerId, Long zoneId, Long vehicleId, Pageable pageable);
	Page<WasteLog> findByWorkerIdAndVehicleId(Long workerId, Long vehicleId, Pageable pageable);
	Page<WasteLog> findByWorkerIdAndCollectionTimeBetweenAndVehicleId(Long workerId, LocalDateTime startDateTime, LocalDateTime endDateTime, Long vehicleId, Pageable pageable);
	List<WasteLog> findByCollectionTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<WasteLog> findByZoneId(Long zoneId);
    List<WasteLog> findByVehicleId(Long vehicleId);
    List<WasteLog> findByWorkerId(Long workerId);
    List<WasteLog> findByZoneIdAndVehicleId(Long zoneId,Long vehicleId);
    List<WasteLog> findByCollectionTimeBetweenAndZoneId(LocalDateTime startDate, LocalDateTime endDate, Long zoneId);
    List<WasteLog> findByVehicleIdAndWorkerId(Long vehicleId, Long workerId);
	List<WasteLog> findByWorkerIdAndCollectionTimeBetweenAndZoneIdAndVehicleId(Long workerId,
			LocalDateTime startDateTime, LocalDateTime endDateTime, Long zoneId, Long vehicleId);
	List<WasteLog> findByWorkerIdAndCollectionTimeBetweenAndZoneId(Long workerId, LocalDateTime startDateTime,
			LocalDateTime endDateTime, Long zoneId);
	List<WasteLog> findByWorkerIdAndCollectionTimeBetween(Long workerId, LocalDateTime startDateTime,
			LocalDateTime endDateTime);
	List<WasteLog> findByWorkerIdAndZoneId(Long workerId, Long zoneId);
	List<WasteLog> findByWorkerIdAndZoneIdAndVehicleId(Long workerId, Long zoneId, Long vehicleId);
	List<WasteLog> findByWorkerIdAndVehicleId(Long workerId, Long vehicleId);
	List<WasteLog> findByWorkerIdAndCollectionTimeBetweenAndVehicleId(Long workerId, LocalDateTime startDateTime,
			LocalDateTime endDateTime, Long vehicleId);
	@Query("select w from WasteLog w where w.workerId=?1 and w.weightCollected IS NULL ")
	WasteLog findByWorkerIdAndWeightCollectedIsNUll(Long workerId);
	
	@Query("select w from WasteLog w where w.vehicleId = ?1 and w.weightCollected IS NULL")
	List<WasteLog> findByVehicleIdAndWeightCollectedIsNull(Long vehicleId);
	
	@Query("delete from WasteLog w where w.zoneId=?1and w.weightCollected IS NULL")
	void deleteAllByZoneIdAndWeightCollectedIsNull(Long zoneId);
	
	List<WasteLog> findByVehicleIdAndCollectionTimeBetween(Long vehicleId, LocalDateTime startOfDay,
			LocalDateTime endOfDay);
    
}