package com.likelion.lms.Domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String filePath;

    public File(String fileName, String string) {
        this.fileName = fileName;
        this.filePath = string;
    }

    public File() {

    }
}