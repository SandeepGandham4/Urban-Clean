package com.example.workermanagement.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignWorkerRequestDTO {
	private Long driverId;
	private List<Long> workerIds;
}
