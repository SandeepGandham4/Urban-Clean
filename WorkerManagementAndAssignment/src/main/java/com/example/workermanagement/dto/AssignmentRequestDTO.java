package com.example.workermanagement.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentRequestDTO {
	private Long vehicleId;
    private Long driverId;
    private List<Long> workerIds;
    private Map<Long, String> shiftTimeByWorker;
}
