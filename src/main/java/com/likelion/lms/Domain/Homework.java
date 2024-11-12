package com.likelion.lms.Domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "homework")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String cate;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @Column(nullable = false)
    private LocalDate dueDate;
    @Column(nullable = false)
    private String dueTime;
    @OneToMany(mappedBy = "homework", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<UserHomework> userHomeworks;

    // 파일 경로를 반환하는 메서드
    // 파일 경로를 설정하는 메서드
    @Getter
    @Setter
    private String filePath;  // 파일 경로 저장을 위한 변수

    // 기존의 다른 필드들...

}