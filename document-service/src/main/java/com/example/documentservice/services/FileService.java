package com.example.documentservice.services;

import com.example.documentservice.dao.ResponseFileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

public interface FileService {
    ResponseFileDto saveFile(MultipartFile file) throws Exception;
    void saveFiles(MultipartFile[] files) throws Exception;
    List<ResponseFileDto> getAllFiles() throws FileNotFoundException;
    void deleteFileByName(String fileName);
}
