package com.gov.grievance.grievance_portal.controller;


import com.gov.grievance.grievance_portal.dto.ApiResponse;
import com.gov.grievance.grievance_portal.dto.GrievanceRequestDTO;
import com.gov.grievance.grievance_portal.dto.GrievanceResponseDTO;
import com.gov.grievance.grievance_portal.enums.GrievanceStatus;
import com.gov.grievance.grievance_portal.service.GrievanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grievances")
@RequiredArgsConstructor
public class GrievanceController {

    private final GrievanceService grievanceService;

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<GrievanceResponseDTO>> submit(
            @Valid @RequestBody GrievanceRequestDTO dto)
    {
        GrievanceResponseDTO grievance =
                grievanceService.submit(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        grievance, "Grievance submitted successfully. "+
                        "Reference: " + grievance.getReferenceNumber(),
                        201));
    }

    @GetMapping("/track/{refNumber}")

    public ResponseEntity<ApiResponse<GrievanceResponseDTO>> track(
            @PathVariable String refNumber)
    {
        GrievanceResponseDTO grievance =
                grievanceService.trackByRefNumber(refNumber);
        return ResponseEntity.ok(
                ApiResponse.success(grievance,
                        "Grievance Found",
                        200));
    }

    @GetMapping("/citizen/{citizenId}")
    public ResponseEntity<ApiResponse<List<GrievanceResponseDTO>>>
            getByCitizens(
            @PathVariable Long citizenId){
        List<GrievanceResponseDTO> grievances =
                grievanceService.getByCitizen(citizenId);
        return ResponseEntity.ok(
                ApiResponse.success(grievances,
                        grievances.size()+ "grievance(s) found for citizen",
                        200));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<GrievanceResponseDTO>>>
            getStatus(
            @PathVariable GrievanceStatus status){
        List<GrievanceResponseDTO> grievances =
                grievanceService.getByStatus(status);
        return ResponseEntity.ok(
                ApiResponse.success(grievances,
                        grievances.size() +
                                "grievance (s) with status: "
                +status, 200));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<GrievanceResponseDTO>>>
                getAll()
    {
        List<GrievanceResponseDTO> grievances =
                grievanceService.getAll();
        return ResponseEntity.ok(
                ApiResponse.success(
                grievances,
                "Total grievances: " +grievances.size()
                ,200));
    }

    @PutMapping("/{refNumber}/status")
    public ResponseEntity<ApiResponse<GrievanceResponseDTO>>
            updateStatus(
            @PathVariable String refNumber,
            @RequestBody Map<String, String> body)
    {
        GrievanceStatus newStatus =
                GrievanceStatus.valueOf(body.get("status"));
        String remarks = body.get("remarks");
        String officerName = body.get("officerName");

        GrievanceResponseDTO updated =
                grievanceService.updateStatus(
                        refNumber, newStatus, officerName, remarks
                );
        return ResponseEntity.ok(
                ApiResponse.success(
                        updated,
                        "Grievance status updated to: " + newStatus,
                        200));
    }
}
