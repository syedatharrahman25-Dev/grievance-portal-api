package com.gov.grievance.grievance_portal.dto;

import com.gov.grievance.grievance_portal.enums.GrievanceDepartment;
import com.gov.grievance.grievance_portal.enums.GrievanceStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GrievanceResponseDTO {
    private Long id;
    private String referenceNumber;
    private String title;
    private String description;
    private GrievanceDepartment department;
    private GrievanceStatus status;
    private String location;
    private String citizenName;
    private String citizenEmail;
    private LocalDateTime submittedAt;
    private LocalDateTime lastUpdatedAt;
}
