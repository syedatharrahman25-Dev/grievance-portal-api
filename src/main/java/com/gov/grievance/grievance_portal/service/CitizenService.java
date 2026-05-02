package com.gov.grievance.grievance_portal.service;

import com.gov.grievance.grievance_portal.dto.CitizenRegisterDTO;
import com.gov.grievance.grievance_portal.entity.Citizen;
import com.gov.grievance.grievance_portal.repository.CitizenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CitizenService implements UserDetailsService{

    private final CitizenRepository citizenRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException{
        return citizenRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Citizen not found with email: " + email));
    }
    public Citizen register(CitizenRegisterDTO dto)
    {
        if(citizenRepository.existsByEmail(dto.getEmail()))
        {
            throw new RuntimeException("Email already in registered: "
            + dto.getEmail());
        }
        Citizen citizen = Citizen.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(passwordEncoder.encode(dto.getPassword()))
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
