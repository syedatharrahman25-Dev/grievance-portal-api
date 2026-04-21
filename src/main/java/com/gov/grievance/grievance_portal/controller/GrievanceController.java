package com.gov.grievance.grievance_portal.controller;


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
    public ResponseEntity<GrievanceResponseDTO> submit(
            @Valid @RequestBody GrievanceRequestDTO dto)
    {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(grievanceService.submit(dto));
    }

    @GetMapping("/track/{refNumber}")

    public ResponseEntity<GrievanceResponseDTO> track(
            @PathVariable String refNumber)
    {
        return ResponseEntity.ok(grievanceService.trackByRefNumber(refNumber));
    }

    @GetMapping("/citizen/{citizenId}")
    public ResponseEntity<List<GrievanceResponseDTO>> getCitizens(
            @PathVariable Long citizenId){
        return ResponseEntity.ok(grievanceService.getByCitizen(citizenId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<GrievanceResponseDTO>> getStatus(
            @PathVariable GrievanceStatus status){
        return ResponseEntity.ok(grievanceService.getByStatus(status));
    }

    @GetMapping("/all")
    public ResponseEntity<List<GrievanceResponseDTO>> getAll()
    {
        return ResponseEntity.ok(grievanceService.getAll());
    }

    @PutMapping("/{refNumber}/status")
    public ResponseEntity<GrievanceResponseDTO> updateStatus(
            @PathVariable String refNumber,
            @RequestBody Map<String, String> body)
    {
        GrievanceStatus newStatus = GrievanceStatus.valueOf(body.get("status"));
        String remarks = body.get("remarks");
        String officerName = body.get("officerName");
        return ResponseEntity.ok(
                grievanceService.updateStatus(refNumber, newStatus, remarks, officerName));
    }
}
