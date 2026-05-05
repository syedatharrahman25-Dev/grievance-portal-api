package com.gov.grievance.grievance_portal.controller;

import com.gov.grievance.grievance_portal.dto.ApiResponse;
import com.gov.grievance.grievance_portal.dto.LoginRequestDTO;
import com.gov.grievance.grievance_portal.dto.LoginResponseDTO;
import com.gov.grievance.grievance_portal.entity.Citizen;
import com.gov.grievance.grievance_portal.security.JwtService;
import com.gov.grievance.grievance_portal.service.CitizenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CitizenService citizenService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>>
            login(
            @Valid @RequestBody LoginRequestDTO request){
            Authentication authentication = authenticationManager.
                    authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            Citizen citizen = (Citizen) authentication.getPrincipal();
            String token = jwtService.generateToken(citizen);

            LoginResponseDTO loginResponse = LoginResponseDTO
                    .builder()
                    .token(token)
                    .email(citizen.getEmail())
                    .fullName(citizen.getFullName())
                    .citizenId(citizen.getId())
                    .tokenType("Bearer")
                    .build();

            return ResponseEntity.ok(
                    ApiResponse.success(
                            loginResponse,
                            "Login successful. Welcome,"
                    + citizen.getFullName()+ "!",
                            200));
    }
}
