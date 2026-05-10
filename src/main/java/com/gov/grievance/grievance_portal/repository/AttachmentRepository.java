package com.gov.grievance.grievance_portal.repository;


import com.gov.grievance.grievance_portal.entity.GrievanceAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<GrievanceAttachment, Long> {
    List<GrievanceAttachment> findByGrievanceId(Long grievanceId);
    void deleteByGrievanceId(Long grievanceId);
}
