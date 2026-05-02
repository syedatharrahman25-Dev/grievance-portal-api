package com.gov.grievance.grievance_portal.enums;

public enum GrievanceStatus {
    SUBMITTED, // Just filled by citizen
    UNDER_REVIEW, // Officer picked it up.
    IN_PROGRESS, // Being worked on.
    RESOLVED, //Done
    CLOSED, //Closed by admin
    REJECTED //Invalid Complaint.
}
