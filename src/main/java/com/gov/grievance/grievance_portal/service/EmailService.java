package com.gov.grievance.grievance_portal.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.from-name}")
    private String fromName;

    @Async
    public void sendGrievanceSubmittedEmail(
            String toEmail,
            String citizenName,
            String referenceNumber,
            String grievanceTitle)
    {
        String subject =
                "Grievance Received - " + referenceNumber;

        String body = buildGrievanceSubmittedBody(
                citizenName, referenceNumber, grievanceTitle);

        sendEmail(toEmail, subject, body);
    }
    @Async
    public void sendStatusUpdateEmail(
            String toEmail,
            String citizenName,
            String referenceNumber,
            String grievanceTitle,
            String oldStatus,
            String newStatus,
            String remarks,
            String officerName
    ){
        String subject =
                "Grievance Status Updated - " + referenceNumber;
        String body = buildStatusUpdateBody(
                citizenName, referenceNumber, grievanceTitle,
                oldStatus, newStatus, remarks, officerName
        );

        sendEmail(toEmail, subject, body);
    }
    @Async
    public void sendGrievanceResolvedEmail(
            String toEmail,
            String citizenName,
            String referenceNumber,
            String grievanceTitle,
            String resolutionRemarks
    ){
        String subject = "Grievance Resolved - " + referenceNumber;

        String body = buildResolvedBody(
                citizenName, referenceNumber,
                grievanceTitle, resolutionRemarks
        );
        sendEmail(toEmail, subject, body);
    }
    private void sendEmail(
            String toEmail,
            String subject,
            String htmlbody) {
        try{
            MimeMessage message =
                    mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper
                            (message, true, "UTF-8");
            helper.setFrom(fromEmail, fromName);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlbody, true);
            mailSender.send(message);

            System.out.println(
                    "Email sent successfully to: " + toEmail);
        }catch(MessagingException e){
            System.err.println(
                    "Failed to send email to: "
                    + toEmail + " Error: " + e.getMessage());
        }catch(Exception e){
            System.err.println(
                    "Unexpected error sending email: "
                    + e.getMessage());
        }
    }
    private String buildGrievanceSubmittedBody(
            String citizenName,
            String referenceNumber,
            String grievanceTitle)
    {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<body style = 'font-family: Arial, sans-serif; " +
                "max-width: 600px; margin: 0 auto;'>" +
                // Header
                "<div style = 'background-color: #1a5276;" +
                "padding: 20px; text-align: center;'>"+
                "<h2 style = 'color: white; margin: 0;'>"+
                "Grievance Management Portal</h2>"+
                "</div>" +
                // Body
                "<div style = 'padding: 30px;"+
                "background-color: #f8f9fa;'>"+
                "<p> Dear <strong>" + citizenName + "</string>, </p>"+
                "<p> Your grievance has been successfully "+
                "register in our system. </p>" +

                // Reference number box
                "<div style='background-color: #d5e8d4; " +
                "border: 2px solid #82b366; "+
                "border-radius: 8px; padding: 15px; "+
                "margin: 20px 0; text-align: center; '>"+
                "<p style='margin: 0; font-size: 14px;'>"+
                "Your Reference Number</p>" +
                "<h2 style='margin: 5px 0; color: #1a5276;'>"+
                referenceNumber + "</h2>" +
                "<p style = 'margin: 0; font-size: 12px; "+
                "color: #666;'> keep this for tracking</p>" +
                "</div>" +

                "</p><strong> Grievance:</strong> "+
                grievanceTitle + "</p>" +
                "<p><strong>Status:</strong> "+
                "<span style= 'color: #e67e22; '> SUBMITTED</span>"
                + "</p>"+

                "<p>Our team will review your complaint and "+
                "take necessary action. You will be notified " +
                "at every step via email. </p>" +

                // Footer
                "<hr style='border: 1px solid #ddd;'>" +
                "<p style='color: #666; font-size: 12px;'>"+
                "This is an automated message from the " +
                "Grievance Management Portal. "+
                "Please do not reply to this email. </p>" +
                "</div>" +

                "</body></html>";
    }
    private String buildStatusUpdateBody(
            String citizenName,
            String referenceNumber,
            String grievanceTitle,
            String oldStatus,
            String newStatus,
            String remarks,
            String officerName){
        String statusColor;
        switch (newStatus)
        {
            case "IN_PROGRESS":
                statusColor = "#e67e22";
                // Orange
                break;
            case "RESOLVED":
                statusColor = "#27ae60";
                // Green
                break;
            case "REJECTED":
                statusColor = "#e74c3c"; // Red color
                break;
            default:
                statusColor = "#2980b9";
                // Blue = other statuses

        }
        return"<!DOCTYPE html>"+
                "<html>" +
                "<body style = 'font-family: Arial, sans-serif; "+
                "max-width: 600px; margin: 0 auto;'>"+
                "<div style='background-color: #1a5276; "+
                "padding: 20px; text-align: center;'>"+
                "<h2 style='color: white; margin: 0;'>" +
                "Grievance Management Portal</h2>" +
                "</div>" +

                "<div style ='padding: 30px; "+
                "background-color: #f8f9fa;'>"+
                "<p>Dear <strong>" + citizenName + "</strong>, </p>"+
                "<p> Your grievance status has been updated. </p>" +
                "<table style = 'width: 100%; "+
                "border-collapse: collapse; margin: 20px 0; '>"+
                "<tr style='background-color: #eaf2ff;'>"+
                "<td style='padding: 10px; border: 1px solid #ddd;"+
                "font-weight: bold; '>Reference Number</td>"+
                "td style = 'padding: 10px; "+
                "border: 1px solid #ddd;'>"+
                referenceNumber + "</td></tr>" +

                "<tr>" +
                "<td style ='padding: 10px; border: 1px solid #ddd;"+
                " font-weight: bold; '>Grievance</td>" +
                "<td style='padding: 10px; "+
                "border: 1px solid #ddd; '>" +
                grievanceTitle + "</td></tr>" +

                "<tr style = 'background-color: #eaf2ff;'>" +
                "<td style = 'padding: 10px; border: 1px solid #ddd;"+
                " font-weight: bold; '>Previous Status</td>" +
                "<td style = 'padding: 10px; "+
                "border: 1px solid #ddd; color: #666; '>" +
                oldStatus + "</td></tr>" +

                "<tr>" +
                "<td style = 'padding: 10px; border: 1px solid #ddd;"+
                "<font-weight: bold; '>Current Status </td>" +
                "<td style = 'padding: 10px; border: 1px solid #ddd; "+
                "color: " + statusColor + "; font-weight: bold; '>"+
                newStatus + "</td></tr>" +

                "<tr style= ' background-color: #eaf2ff;'>"+
                "<td style= 'padding: 10px; border: 1px solid #ddd;"+
                "font-weight: bold; '>Remarks</td>" +
                "<td style = 'padding: 10px; "+
                "border: 1px solid #ddd; '>" +
                (remarks != null ? remarks : "No remarks") +
                "</td></tr>" +

                "<tr>" +
                "<td style ='padding: 10px; border: 1px solid #ddd;"+
                "font-weight: bold;'>Updated By</td>" +
                "<td style ='padding: 10px; "+
                "border: 1px solid #ddd; "+
                (officerName != null ? officerName: "System")+
                "</td></tr>" +
                "</table>" +

                "<hr style='border: 1px solid #ddd;'/>"+
                "<p style='color: #666; font-size: 12px;'>"+
                "This is an automated message. " +
                "Please do not reply. </p>" +
                "</div>" +

                "</body></html>";

    }
    private String buildResolvedBody(
            String citizenName,
            String referenceNumber,
            String grievanceTitle,
            String resolutionRemarks){
        return"<!DOCTYPE html>"+
                "<html>" +
                "<body style = 'font-family: Arial, sans-serif; "+
                "max-width = 600px; margin: 0 auto;'>"+

                "<div style='background-color: #27ae60; "+
                "padding: 20px; text-align: center;'>" +
                "<h2 style= 'color: white; margin: 0; '>"+
                "Grievance Resolved</h2>" +
                "</div>" +

                "<div style='padding: 30px; "+
                "background-color: #f8f9fa;'>" +
                "<p>Dear <strong>" + citizenName + "</strong>,</p>" +
                "<p> We are pleased to inform you that your "+
                "grievance has been <strong style = 'color: #27ae60;'>" +
                "RESOLVED</strong>.</p>" +

                "<p><strong> Reference:</strong> "+
                referenceNumber + "</p>" +
                "<p><strong>Grievance: </strong> "+
                grievanceTitle + "</p>" +
                "<p><strong> Resolution: </strong> "+
                (resolutionRemarks != null ?
                        resolutionRemarks: "Issue resolved")+
                "</p>" +

                "<p>If you are not satisfied with the resolution, " +
                "you may raise a new grievance or contact "+
                "our helpline.</p>" +

                "<p> Thank you for using the Grievance "+
                "Management Portal. </p>" +

                "<hr style= 'border: 1px solid #ddd;'>" +
                "<p style='color: #666; font-size: 12px;'>" +
                "This is an automated message. "+
                "Please do not reply. </p>" +
                "</div>" +

                "</body></html>";
    }
}
