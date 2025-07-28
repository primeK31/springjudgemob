package com.example.spring_boot.code.repository;

import com.example.spring_boot.code.model.CodeSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface CodeSubmissionRepository extends JpaRepository<CodeSubmission, Long> {

    List<CodeSubmission> findByExitCode(Integer exitCode);

    // Find successful submissions (exit code 0)
    @Query("SELECT cs FROM CodeSubmission cs WHERE cs.exitCode = 0")
    List<CodeSubmission> findSuccessfulSubmissions();

    List<CodeSubmission> findByLanguage(String language);

    List<CodeSubmission> findByCreatedAtBetween(LocalDateTime createdAt, LocalDateTime createdAt2);

    @Query("SELECT cs FROM CodeSubmission cs ORDER BY cs.createdAt DESC")
    List<CodeSubmission> findRecentSubmissions();

    @Query("SELECT COUNT(cs) FROM CodeSubmission cs WHERE cs.exitCode = :exitCode")
    Long countByExitCode(@Param("exitCode") Integer exitCode);
}