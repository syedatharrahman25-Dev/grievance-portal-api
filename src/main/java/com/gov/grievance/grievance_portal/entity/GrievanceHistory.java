package com.gov.grievance.grievance_portal.entity;


import com.gov.grievance.grievance_portal.enums.GrievanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "grievance_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrievanceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GrievanceStatus fromStatus;

    @Enumerated(EnumType.STRING)
    private GrievanceStatus toStatus;

    private String remarks;
    private String updatedBy;

    private LocalDateTime changedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grievance_id")
    private Grievance grievance;

    @PrePersist
    public void onCreate()
    {
        changedAt = LocalDateTime.now();
    }
}
