package com.example.documentservice.services;

import com.example.documentservice.exceptions.FileException;
import com.example.documentservice.utils.FileOperationUtils;
import com.example.documentservice.dao.ResponseFileDto;
import com.example.documentservice.entities.FileEntity;
import com.example.documentservice.mappers.FileMapper;
import com.example.documentservice.repositories.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileServiceImpl implements FileService{
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final FileOperationUtils fileOperationUtils;


    public FileServiceImpl(FileRepository fileRepository, FileMapper fileMapper, FileOperationUtils fileOperationUtils) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
        this.fileOperationUtils = fileOperationUtils;
    }

    @Override
    public ResponseFileDto saveFile(MultipartFile file) {
        String fileName =FilenameUtils.getBaseName(file.getOriginalFilename());
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        try {
            fileOperationUtils.validFile(file);
            String path = fileOperationUtils.buildFilePath(fileName, fileExtension).toString();
            FileEntity fileEntity = FileEntity.builder()
                    .name(fileName)
                    .mimeType(file.getContentType())
                    .size(file.getSize())
                    .extension(fileExtension)
                    .path(path)
                    .build();

            FileEntity savedFile = fileRepository.save(fileEntity);
            if(savedFile.getName().isEmpty()) throw new FileException("Could not save File");

            fileOperationUtils.transferToFileSystem(file);

            /*ResponseFileDto responseFileDto = new ResponseFileDto();
            BeanUtils.copyProperties(savedFile, responseFileDto);
            return responseFileDto;*/
            return fileMapper.fileEntityToResponseFileDto(savedFile);
        } catch (MaxUploadSizeExceededException e) {
            throw new MaxUploadSizeExceededException(file.getSize());
        } catch (Exception e) {
            throw new FileException("Could not save File: " + file.getOriginalFilename() + e.getMessage());
        }
    }

    @Override
    public void saveFiles(MultipartFile[] files) {
        Arrays.asList(files).forEach(file -> {
            try {
                saveFile(file);
                fileOperationUtils.transferToFileSystem(file);
            } catch (Exception e) {
                throw new FileException("Could not save File: " + file.getOriginalFilename() + e.getMessage());
            }
        });
    }

    @Override
    public List<ResponseFileDto> getAllFiles() throws FileNotFoundException {
        List<FileEntity> files = fileRepository.findAll();
        if(files.isEmpty()) {
            throw new FileNotFoundException();
        }
        return files.stream().map(fileMapper::fileEntityToResponseFileDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteFileByName(String fileName) {
        Optional<FileEntity> fileOptional = fileRepository.findByName(fileName);

        fileOptional.ifPresentOrElse(
                file -> {
                    deleteFileEntity(file);
                    deleteFileFromDisk(file);
                },
                () -> {
                    throw new FileException("File not found with name: " + fileName);
                }
        );
    }

    private void deleteFileEntity(FileEntity file) {
        fileRepository.deleteByName(file.getName());
    }

    private void deleteFileFromDisk(FileEntity file) {
        Path path = fileOperationUtils.buildFilePath(file.getName(), file.getExtension());

        try {
            Files.deleteIfExists(path);
            log.info("File deleted successfully.");
        } catch (IOException e) {
            throw new FileException("Error deleting file: " + e.getMessage(), e);
        }
    }
}
