package com.gov.grievance.grievance_portal.service;


import com.gov.grievance.grievance_portal.dto.GrievanceRequestDTO;
import com.gov.grievance.grievance_portal.dto.GrievanceResponseDTO;
import com.gov.grievance.grievance_portal.entity.Citizen;
import com.gov.grievance.grievance_portal.entity.Grievance;
import com.gov.grievance.grievance_portal.entity.GrievanceHistory;
import com.gov.grievance.grievance_portal.enums.GrievanceStatus;
import com.gov.grievance.grievance_portal.exception.BadRequestException;
import com.gov.grievance.grievance_portal.exception.ResourceNotFoundException;
import com.gov.grievance.grievance_portal.repository.CitizenRepository;
import com.gov.grievance.grievance_portal.repository.GrievanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrievanceService {

    private final GrievanceRepository grievanceRepository;
    private final CitizenRepository CitizenRepository;

    @Transactional
    public GrievanceResponseDTO submit(GrievanceRequestDTO dto)
    {
        //Step 1: find the citizen

        Citizen citizen = CitizenRepository
                .findById(dto.getCitizenId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Citizen", "id", +dto.getCitizenId()));

        // Step 2: Build the grievance object

        Grievance grievance = Grievance.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .department(dto.getDepartment())
                .location(dto.getLocation())
                .citizen(citizen)
                .referenceNumber(generateReferenceNumber())
                .status(GrievanceStatus.SUBMITTED)
                .build();

        //Step 3: Create first history entry

        GrievanceHistory history = GrievanceHistory.builder()
                .fromStatus(null)
                .toStatus(GrievanceStatus.SUBMITTED)
                .remarks("Grievance submitted by citizen")
                .updatedBy("SYSTEM")
                .grievance(grievance)
                .build();


        //Step 4: Attach history to grievance
        List<GrievanceHistory> historyList = new ArrayList<>();
        historyList.add(history);
        grievance.setHistory(historyList);

        //Step 5: Save to database
        Grievance saved = grievanceRepository.save(grievance);

        //Step 6: Return response
        return mapToResponseDTO(saved);
    }

    @Transactional
    public GrievanceResponseDTO updateStatus(
            String refNumber,
            GrievanceStatus newStatus,
            String remarks,
            String officerName)
    {
        //Step 1: find GRIEVANCE by reference number

        Grievance grievance = grievanceRepository
                .findByReferenceNumber(refNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Grievance", "referenceNumber", refNumber));
        if(grievance.getStatus()==GrievanceStatus.CLOSED){
            throw new BadRequestException(
                    "Cannot update a CLOSED grievance." +
                            "Reference: " + refNumber);
        }
        if(grievance.getStatus()== GrievanceStatus.RESOLVED && newStatus == GrievanceStatus.SUBMITTED){
            throw new BadRequestException(
                    "Cannot move grievance back to SUBMITTED" +
                            "after it has been RESOLVED.");
        }

        //Step 2:  Save old status for history
        GrievanceStatus oldStatus = grievance.getStatus();
        //Step 3: Update Status
        grievance.setStatus(newStatus);
        //Step 4: If resolved, set resolved time

        if(newStatus == GrievanceStatus.RESOLVED)
        {
            grievance.setResolvedAt(LocalDateTime.now());
        }

        //Step 5: Record status change in history
        GrievanceHistory history = GrievanceHistory.builder()
                .fromStatus(oldStatus)
                .toStatus(newStatus)
                .remarks(remarks)
                .updatedBy(officerName)
                .grievance(grievance)
                .build();

        grievance.getHistory().add(history);
        //Step 6: Save and return
        return mapToResponseDTO(grievanceRepository.save(grievance));
    }

    public GrievanceResponseDTO trackByRefNumber(String refNumber)
    {
        Grievance g = grievanceRepository
                .findByReferenceNumber(refNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Grievance", "referenceNumber",refNumber));
        return mapToResponseDTO(g);
    }

    public List<GrievanceResponseDTO> getByCitizen(Long citizenId)
    {
        if(!CitizenRepository.existsById(citizenId)){
            throw new ResourceNotFoundException(
                    "Citizen", "id", citizenId);
        }
        return grievanceRepository.findByCitizenId(citizenId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<GrievanceResponseDTO> getByStatus(GrievanceStatus status)
    {
        return grievanceRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<GrievanceResponseDTO> getAll()
    {
        return grievanceRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Private Helpers ------------------------

    private String generateReferenceNumber()
    {
        String year = String.valueOf(LocalDate.now().getYear());
        String unique = String.valueOf(System.currentTimeMillis());
        unique = unique.substring(unique.length() - 5);
        return "GRV-" + year + "-" + unique;
    }

    private GrievanceResponseDTO mapToResponseDTO(Grievance g)
    {
        return GrievanceResponseDTO.builder()
                .id(g.getId())
                .referenceNumber(g.getReferenceNumber())
                .title(g.getTitle())
                .description(g.getDescription())
                .department(g.getDepartment())
                .status(g.getStatus())
                .location(g.getLocation())
                .citizenName(g.getCitizen().getFullName())
                .citizenEmail(g.getCitizen().getEmail())
                .submittedAt(g.getSubmittedAt())
                .lastUpdatedAt(g.getLastUpdatedAt())
                .build();
    }
}
