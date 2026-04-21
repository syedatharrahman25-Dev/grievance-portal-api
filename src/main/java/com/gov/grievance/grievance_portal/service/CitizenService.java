package com.gov.grievance.grievance_portal.service;

import com.gov.grievance.grievance_portal.dto.CitizenRegisterDTO;
import com.gov.grievance.grievance_portal.entity.Citizen;
import com.gov.grievance.grievance_portal.repository.CitizenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CitizenService {

    private final CitizenRepository citizenRepository;
    public Citizen register(CitizenRegisterDTO dto)
    {
        if(citizenRepository.existsByEmail(dto.getEmail()))
        {
            throw new RuntimeException("Email already in registered: ");
        }
        Citizen citizen = Citizen.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(dto.getPassword())
                .address(dto.getAddress())
                .city(dto.getCity())
                .state(dto.getState())
                .build();

        return citizenRepository.save(citizen);
    }

    public Optional<Citizen> findById(Long id)
    {
        return citizenRepository.findById(id);
    }

    public Citizen findByEmail(String email)
    {
        return citizenRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Citizen not found with email: " + email));
    }
}
