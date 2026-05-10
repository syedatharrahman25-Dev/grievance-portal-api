package com.gov.grievance.grievance_portal.service;

import com.gov.grievance.grievance_portal.exception.BadRequestException;
import com.gov.grievance.grievance_portal.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.module.ResolutionException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final List<String> ALLOWED_TYPES =
            Arrays.asList(
                    "image/jpeg",
                    "image/png",
                    "image/gif",
                    "application/pdf"
            );
    private static final long MAX_FILE_SIZE =
            5* 1024 * 1024;

    public String storeFile(MultipartFile file) {
        if(file.isEmpty()){
            throw new BadRequestException(
                    "Cannot upload empty file. "+
                    "Please select a file.");
        }

        String contentType = file.getContentType();
        if(contentType == null ||
                !ALLOWED_TYPES.contains(contentType))
        {
            throw new BadRequestException(
                    "File type not allowed: " +
                            contentType);
        }

        if(file.getSize() > MAX_FILE_SIZE){
            throw new BadRequestException(
                    "File size exceeds maximum limit of 5 mb.");
        }
        try{

            String originalFileName = file.getOriginalFilename();
                    if(originalFileName == null){
                        originalFileName = "UNKNOWN";
                    }

            String fileExtension = "";
            if(originalFileName.contains(".")){
                fileExtension = originalFileName
                        .substring(originalFileName.lastIndexOf("."));
            }
            String storedFileName = UUID.randomUUID()
                    .toString() + fileExtension;

            Path uploadPath = Paths.get(uploadDir)
                        .toAbsolutePath()
                        .normalize();

            Files.createDirectories(uploadPath);
            Path targetLocation = uploadPath
                    .resolve(storedFileName);

            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return storedFileName;
        }
        catch(IOException e){
            throw new BadRequestException(
                    "Could not store file. Please try again."+
                            "Error: " + e.getMessage());
        }
    }
    public byte[] loadFile(String storedFileName){
        try{
            Path filePath = Paths.get(uploadDir)
                    .resolve(storedFileName);
            if(!Files.exists(filePath)){
                throw new ResourceNotFoundException(
                        "File", "name", storedFileName
                );
            }
            return Files.readAllBytes(filePath);
        }
        catch(IOException e){
            e.printStackTrace();
            throw new BadRequestException(
                    "Could not read file: " +
                            storedFileName);
        }
    }
    public void deleteFile(String storedFileName){
        try{
            Path filePath = Paths.get(uploadDir)
                    .resolve(storedFileName);
            Files.deleteIfExists(filePath);
        }
        catch(IOException e){
            System.err.println(
                    "Could not delete file: "
                            + storedFileName);
        }
    }
}
