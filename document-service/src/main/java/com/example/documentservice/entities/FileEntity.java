package com.example.documentservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "files")
public class FileEntity extends BaseEntity {
    @Column(unique = true)
    private String name;
    private String extension;
    private String mimeType ;
    private Long size;
    private String path;
}
