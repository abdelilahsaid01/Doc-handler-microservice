package com.example.documentservice.mappers;

import com.example.documentservice.dao.ResponseFileDto;
import com.example.documentservice.entities.FileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper {

    //@Mapping(target = "path", ignore = true)
    ResponseFileDto fileEntityToResponseFileDto(FileEntity fileEntity);
}

