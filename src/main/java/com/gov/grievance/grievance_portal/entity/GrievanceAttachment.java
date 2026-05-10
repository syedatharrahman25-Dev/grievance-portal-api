package com.gov.grievance.grievance_portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "grievance_attachments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrievanceAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String storedFileName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private Long fileSize;

    @Column(updatable = false)
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grievance_id", nullable = false)
    private Grievance grievance;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}
