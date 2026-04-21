package com.gov.grievance.grievance_portal.repository;

import com.gov.grievance.grievance_portal.entity.Grievance;
import com.gov.grievance.grievance_portal.enums.GrievanceDepartment;
import com.gov.grievance.grievance_portal.enums.GrievanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrievanceRepository extends JpaRepository<Grievance, Long> {

    Optional<Grievance> findByReferenceNumber(String referenceNumber);

    List<Grievance> findByCitizenId(Long citizenId);

    List<Grievance> findByStatus(GrievanceStatus status);
    List<Grievance> findByDepartment(GrievanceDepartment department);

    List<Grievance> findByCitizenIdAndStatus(Long citizenId, GrievanceStatus status);
}
