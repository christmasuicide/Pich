package com.kma.pich.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileSavingUtils {

    public static String saveFile(String uploadDir,
                                MultipartFile multipartFile) throws IOException {
        if(multipartFile == null || multipartFile.getContentType() == null) {
            return null;
        }
        Path uploadPath = Paths.get(uploadDir);
        String type;
        if(multipartFile.getContentType().equalsIgnoreCase("image/png")) {
            type = ".png";
        } else {
            type = ".jpg";
        }

        String strPath = uploadDir + UUID.randomUUID().toString().replaceAll("-", "") + type;
        Path path = Path.of(strPath);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            return strPath;
        } catch (IOException ioe) {
            throw new IOException("Could not save image file", ioe);
        }
    }

}
