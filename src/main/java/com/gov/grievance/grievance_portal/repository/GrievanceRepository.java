package com.gov.grievance.grievance_portal.repository;

import com.gov.grievance.grievance_portal.entity.Grievance;
import com.gov.grievance.grievance_portal.enums.GrievanceDepartment;
import com.gov.grievance.grievance_portal.enums.GrievanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GrievanceRepository extends JpaRepository<Grievance, Long> {

    Optional<Grievance> findByReferenceNumber(
            String referenceNumber);

    List<Grievance> findByCitizenId(Long citizenId);
    List<Grievance> findByStatus(GrievanceStatus status);

    // ====== PAGINATED VERSIONS ==========
    Page<Grievance> findAllByOrderBySubmittedAtDesc(
            Pageable pageable);

    Page<Grievance> findByStatus(
            GrievanceStatus status,
            Pageable pageable);

    Page<Grievance> findByDepartment(
            GrievanceDepartment department,
            Pageable pageable);

    Page<Grievance> findByCitizenId(
            Long citizenId,
            Pageable pageable);

    Page<Grievance> findByStatusAndDepartment(
            GrievanceStatus status,
            GrievanceDepartment department,
            Pageable pageable);

    boolean existsByReferenceNumber(
            String referenceNumber);

    long countByStatus(
            GrievanceStatus status);
}
