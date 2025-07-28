package com.example.spring_boot.code.service;

import com.example.spring_boot.code.model.CodeSubmission;
import com.example.spring_boot.code.repository.CodeSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class CodeSubmissionService {

    @Autowired
    private CodeSubmissionRepository codeSubmissionRepository;

    public CodeSubmission saveSubmission(String code, String output, int exitCode) {
        CodeSubmission submission = new CodeSubmission();
        submission.setSourceCode(code);
        submission.setOutput(output);
        submission.setExitCode(exitCode);

        return codeSubmissionRepository.save(submission);
    }

    public CodeSubmission saveSubmission(String code, String output, int exitCode,
                                         String language, Long executionTimeMs) {
        CodeSubmission submission = new CodeSubmission();
        submission.setSourceCode(code);
        submission.setOutput(output);
        submission.setExitCode(exitCode);
        submission.setLanguage(language);
        submission.setExecutionTimeMs(executionTimeMs);

        return codeSubmissionRepository.save(submission);
    }

    @Transactional(readOnly = true)
    public Optional<CodeSubmission> findById(Long id) {
        return codeSubmissionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<CodeSubmission> findAll() {
        return codeSubmissionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<CodeSubmission> findAll(Pageable pageable) {
        return codeSubmissionRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<CodeSubmission> findByExitCode(Integer exitCode) {
        return codeSubmissionRepository.findByExitCode(exitCode);
    }

    @Transactional(readOnly = true)
    public List<CodeSubmission> findSuccessfulSubmissions() {
        return codeSubmissionRepository.findSuccessfulSubmissions();
    }

    @Transactional(readOnly = true)
    public List<CodeSubmission> findByLanguage(String language) {
        return codeSubmissionRepository.findByLanguage(language);
    }

    @Transactional(readOnly = true)
    public List<CodeSubmission> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return codeSubmissionRepository.findByCreatedAtBetween(start, end);
    }

    // Statistics methods
    @Transactional(readOnly = true)
    public Long countByExitCode(Integer exitCode) {
        return codeSubmissionRepository.countByExitCode(exitCode);
    }

    @Transactional(readOnly = true)
    public Long getTotalSubmissions() {
        return codeSubmissionRepository.count();
    }

    @Transactional(readOnly = true)
    public Long getSuccessfulSubmissionsCount() {
        return countByExitCode(0);
    }

    // Delete methods
    public void deleteById(Long id) {
        codeSubmissionRepository.deleteById(id);
    }

    public void deleteSubmission(CodeSubmission submission) {
        codeSubmissionRepository.delete(submission);
    }

    // Utility method to check if submission was successful
    public boolean isSuccessful(CodeSubmission submission) {
        return submission.getExitCode() != null && submission.getExitCode() == 0;
    }
}
