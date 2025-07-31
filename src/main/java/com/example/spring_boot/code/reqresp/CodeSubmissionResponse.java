package com.example.spring_boot.code.reqresp;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CodeSubmissionResponse {
    private String output;
    private String errorOutput;
    private int exitCode;
    private Long submissionId;
    private Long executionTimeMs;
    private String status;

    // Constructors
    public CodeSubmissionResponse() {}

    public CodeSubmissionResponse(String output, String errorOutput, int exitCode) {
        this.output = output;
        this.errorOutput = errorOutput;
        this.exitCode = exitCode;
        this.status = exitCode == 0 ? "SUCCESS" : "FAILED";
    }

    public CodeSubmissionResponse(String output, String errorOutput, int exitCode, Long submissionId, Long executionTimeMs) {
        this(output, errorOutput, exitCode);
        this.submissionId = submissionId;
        this.executionTimeMs = executionTimeMs;
    }

    // Getters and Setters
    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }

    public String getErrorOutput() { return errorOutput; }
    public void setErrorOutput(String errorOutput) { this.errorOutput = errorOutput; }

    public int getExitCode() { return exitCode; }
    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
        this.status = exitCode == 0 ? "SUCCESS" : "FAILED";
    }

    public Long getSubmissionId() { return submissionId; }
    public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isSuccessful() {
        return exitCode == 0;
    }
}
