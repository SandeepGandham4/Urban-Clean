package com.example.workermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResponseDTO {
    private Long assignmentId;
    private Long workerId;
    private Long zoneId;
    private String shiftTime;
}
