package com.develop.repository;

import com.develop.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<File, Long> {

    File findByFileName(String fileName);
    File findFirstByFileName(String fileName);
}
