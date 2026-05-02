package com.gov.grievance.grievance_portal.entity;

import com.gov.grievance.grievance_portal.enums.GrievanceDepartment;
import com.gov.grievance.grievance_portal.enums.GrievanceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "grievances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grievance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String referenceNumber;

    @NotBlank
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private GrievanceDepartment department;

    @Enumerated(EnumType.STRING)
    private GrievanceStatus status;

    private String location;

    @Column(updatable = false)
    private LocalDateTime submittedAt;

    private LocalDateTime lastUpdatedAt;
    private LocalDateTime resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizen_id", nullable = false)
    private Citizen citizen;

    @OneToMany(mappedBy = "grievance", cascade = CascadeType.ALL)
    private List<GrievanceHistory> history;

    @PrePersist
    protected void onCreate()
    {
        submittedAt = LocalDateTime.now();
        lastUpdatedAt = LocalDateTime.now();
        if(status == null) status = GrievanceStatus.SUBMITTED;
    }

    @PreUpdate
    protected void onUpdate()
    {
        lastUpdatedAt = LocalDateTime.now();
    }
}
