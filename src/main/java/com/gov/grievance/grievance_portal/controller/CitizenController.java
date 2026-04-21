package com.gov.grievance.grievance_portal.controller;

import com.gov.grievance.grievance_portal.dto.CitizenRegisterDTO;
import com.gov.grievance.grievance_portal.entity.Citizen;
import com.gov.grievance.grievance_portal.service.CitizenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/citizens")
@RequiredArgsConstructor
public class CitizenController {

    private final CitizenService citizenService;

    @PostMapping("/register")
    public ResponseEntity<Citizen> register(@Valid @RequestBody CitizenRegisterDTO dto) {
        Citizen citizen = citizenService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(citizen);
    }

    @GetMapping("/{id}")

    public ResponseEntity<Citizen> getById(@PathVariable Long id) {
        return citizenService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
