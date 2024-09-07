package com.likelion.lms.Repository;

import com.likelion.lms.Domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

}