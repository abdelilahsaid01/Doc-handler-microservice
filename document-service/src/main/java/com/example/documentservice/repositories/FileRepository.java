package com.example.documentservice.repositories;

import com.example.documentservice.entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, String> {
     void deleteByName(String name);
     Optional<FileEntity> findByName(String name);
}
