package com.example.dto; 

import com.example.enums.WorkerRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerResponseDTO {
	
	
	private Long workerId;

    
    private String name;

   
    private String contactInfo;

    
    private WorkerRole role;
//    public WorkerResponseDTO(Long workerId, String name, String contactInfo, String role) {
//		this.workerId=workerId;
//		this.name=name;
//		this.contactInfo=contactInfo;
//		this.role=role;
//		
//	}
}
