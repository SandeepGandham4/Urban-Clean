package com.example.workermanagement.dto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDTO {
    @NotNull(message = "Worker ID is mandatory")
    private Long workerId;

    @NotNull(message = "Shift Time is mandatory")
    private String shiftTime;    
   
}
