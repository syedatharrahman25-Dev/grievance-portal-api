package com.gov.grievance.grievance_portal.service;

import com.gov.grievance.grievance_portal.dto.AttachmentResponseDTO;
import com.gov.grievance.grievance_portal.entity.Grievance;
import com.gov.grievance.grievance_portal.entity.GrievanceAttachment;
import com.gov.grievance.grievance_portal.exception.ResourceNotFoundException;
import com.gov.grievance.grievance_portal.repository.AttachmentRepository;
import com.gov.grievance.grievance_portal.repository.GrievanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final GrievanceRepository grievanceRepository;
    private final FileStorageService fileStorageService;

    public AttachmentResponseDTO uploadAttachment(
            String refNumber,
            MultipartFile file
    )
    {
        Grievance grievance = grievanceRepository
                .findByReferenceNumber(refNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Grievance ", "referenceNumber", refNumber));

        List<GrievanceAttachment> existing =
                attachmentRepository
                        .findByGrievanceId(grievance.getId());

        if(existing.size()>=3){
            throw new com.gov.grievance.grievance_portal
                    .exception.BadRequestException(
                            "There is more than 3 attachments in this Grievance. "
            +"This grievance already has " +
                    existing.size()+" attachment(s).");
        }
        String storedFileName = fileStorageService.storeFile(file);

        GrievanceAttachment attachment =
                GrievanceAttachment.builder()
                        .fileName(file.getOriginalFilename())
                        .storedFileName(storedFileName)
                        .filePath("uploads/"+storedFileName)
                        .fileType(file.getContentType())
                        .fileSize(file.getSize())
                        .grievance(grievance)
                        .build();
        GrievanceAttachment saved =
                attachmentRepository.save(attachment);

        return mapToResponseDTO(saved);
    }
    public List<AttachmentResponseDTO> getAttachments(
            String refNumber){
        Grievance grievance = grievanceRepository
                .findByReferenceNumber(refNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Grievance ", "referenceNumber", refNumber));

        return attachmentRepository
                .findByGrievanceId(grievance.getId())
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    public GrievanceAttachment getAttachmentById(Long id){
        return attachmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attachment", "id", id
                        ));
    }

    public void deleteAttachment(Long id)
    {
        GrievanceAttachment attachment =
                attachmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Attachment", "id", id
                                ));
        fileStorageService.deleteFile(
                attachment.getStoredFileName());
        attachmentRepository.deleteById(id);
    }

    private AttachmentResponseDTO mapToResponseDTO(
            GrievanceAttachment attachment)
    {
        String downloadUrl =
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/attachments/download")
                        .path(String.valueOf(attachment.getId()))
                        .toUriString();
        return AttachmentResponseDTO.builder()
                .id(attachment.getId())
                .fileName(attachment.getFileName())
                .fileType(attachment.getFileType())
                .fileSize(attachment.getFileSize())
                .downloadUrl(downloadUrl)
                .uploadedAt(attachment.getUploadedAt())
                .build();
    }
}
