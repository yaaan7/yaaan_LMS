package com.likelion.lms.Domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class User {
    @Id // 키값임을 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY) //디비가 설정 해줄 것임
    private Long id;
    private String name;
    private boolean is_admin;
}