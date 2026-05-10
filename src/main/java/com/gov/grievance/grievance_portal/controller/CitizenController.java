package com.gov.grievance.grievance_portal.controller;

import com.gov.grievance.grievance_portal.dto.ApiResponse;
import com.gov.grievance.grievance_portal.dto.CitizenRegisterDTO;
import com.gov.grievance.grievance_portal.entity.Citizen;
import com.gov.grievance.grievance_portal.service.CitizenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/citizens")
@RequiredArgsConstructor
public class CitizenController {

    private final CitizenService citizenService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Citizen>> register(
            @Valid @RequestBody CitizenRegisterDTO dto) {
        Citizen citizen = citizenService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        citizen,
                        "Citizen registered successfully",
                        201));
    }

    @GetMapping("/{id}")

    public ResponseEntity<ApiResponse<Citizen>> getById(
            @PathVariable Long id) {
        Citizen citizen = citizenService.findById(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                citizen, "Citizen retrieved successfully",
                                200));
    }
}
