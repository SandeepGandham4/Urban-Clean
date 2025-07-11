package com.urbanclean.dto;
import com.urbanclean.enums.WorkerRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDTO {
	
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Contact Info is mandatory")
    @Pattern(regexp = "^\\d{10}$", message = "Contact Info must be a valid 10-digit number")
    private String contactInfo;

    private WorkerRole role;
}
