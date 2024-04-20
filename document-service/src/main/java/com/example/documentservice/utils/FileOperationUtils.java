package com.example.documentservice.utils;

import com.example.documentservice.exceptions.FileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
@RefreshScope
@Component
public class FileOperationUtils {
    private final String basePath ;

    public FileOperationUtils(@Value("${file.storage.path}") String basePath) {
        this.basePath = basePath;
    }

    public void validFile(MultipartFile file) throws FileException, IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        long maxSize = (1024 * 1024);
        if(fileName.contains("..") || file.getBytes().length > maxSize) {
            throw new FileException("Invalid file :" + fileName);
        }
    }

    public void transferToFileSystem(MultipartFile file)  {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new FileException("Cannot transfer file " + e.getMessage());
        }
    }

    public Path buildFilePath(String fileName, String extension)  {
        return Paths.get(basePath + (fileName + "." + extension));
    }
}
