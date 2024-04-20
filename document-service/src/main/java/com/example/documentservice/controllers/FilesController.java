package com.example.documentservice.controllers;

import com.example.documentservice.dao.ResponseFileDto;
import com.example.documentservice.services.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("files")
public class FilesController {
    private final FileService fileService;

    public FilesController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/single/upload")
    public ResponseEntity<ResponseFileDto> saveFile(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok(fileService.saveFile(file));
    }

    @PostMapping("/multiple/upload")
    public void saveFiles(@RequestParam("files") MultipartFile[] files) throws Exception {
        fileService.saveFiles(files);
    }

    @GetMapping
    public ResponseEntity<List<ResponseFileDto>> getAllFiles() throws FileNotFoundException {
        return ResponseEntity.ok(fileService.getAllFiles()) ;
    }

    @DeleteMapping("{fileName}")
    public void deleteFileByName(@PathVariable String fileName) {
        fileService.deleteFileByName(fileName);
    }
}
