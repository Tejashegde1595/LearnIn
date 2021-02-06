package com.example.portfolio.service.common;


import java.io.*;
import java.nio.file.*;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public static void deleteFile(String uploadDir, String fileName) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try{
            Path filePath = uploadPath.resolve(fileName);
            Files.delete(filePath);
        } catch (IOException ioe) {
            throw new IOException("Could not delete image file: " + fileName, ioe);
        }
    }

    public static void UpdateFile(String uploadDir, String fileName,
                                  MultipartFile multipartFile) throws IOException{
        try {
            deleteFile(uploadDir, fileName);
            saveFile(uploadDir, fileName, multipartFile);
        }catch (IOException ioe){
            throw new IOException("Could not edit image file: " + fileName, ioe);
        }
    }
}