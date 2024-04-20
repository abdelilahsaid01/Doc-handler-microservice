package com.example.documentservice.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseFileDto {
    private String name;
    private String extension;
    private String mimeType ;
    private Long size;
    private String path;
}
