package com.gov.grievance.grievance_portal.dto;


import com.gov.grievance.grievance_portal.enums.GrievanceDepartment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GrievanceRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Department is required")
    private GrievanceDepartment department;

    private String location;

    @NotNull(message = "Citizen ID is required")
    private Long citizenId;
}
