package com.peton.useyourwords.services;

import com.peton.useyourwords.exceptions.FileStorageException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;

@Service
public class FileService{

    @Value("${app.upload.dir:${user.home}}")
    public String uploadDir;

    public void uploadFile(MultipartFile file, int id) {

        try {

            ClassLoader classLoader = getClass().getClassLoader();



            Path copyLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(id + "." + FilenameUtils.getExtension(file.getOriginalFilename())));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public void deleteFile(String filePath) throws IOException {
        try
        {
            Files.deleteIfExists(Paths.get(filePath));
        }
        catch(NoSuchFileException e)
        {
            System.out.println("No such file exists");
        }
    }
}