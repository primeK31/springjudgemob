package com.example.spring_boot.code.model;

import com.example.spring_boot.code.reqresp.CodeSubmissionResponse;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "code_submissions")
public class CodeSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_code", columnDefinition = "TEXT")
    private String sourceCode;

    @Column(name = "output", columnDefinition = "TEXT")
    private String output;

    @Column(name = "exit_code")
    private Integer exitCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "language")
    private String language;

    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    // Constructors
    public CodeSubmission() {
        this.createdAt = LocalDateTime.now();
    }

    public CodeSubmission(String sourceCode, String output, Integer exitCode) {
        this();
        this.sourceCode = sourceCode;
        this.output = output;
        this.exitCode = exitCode;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }

    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }

    public Integer getExitCode() { return exitCode; }
    public void setExitCode(Integer exitCode) { this.exitCode = exitCode; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public CodeSubmissionResponse toResponse(CodeSubmission sub) {
        return new CodeSubmissionResponse(sub.getOutput(), "", sub.getExitCode());
    }
}
