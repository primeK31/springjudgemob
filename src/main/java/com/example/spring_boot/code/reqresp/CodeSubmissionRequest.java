package com.example.spring_boot.code.reqresp;

public class CodeSubmissionRequest {
    private String code;
    private String output;
    private Integer exitCode;
    private String language;
    private Long executionTimeMs;

    public CodeSubmissionRequest() {}

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }

    public Integer getExitCode() { return exitCode; }
    public void setExitCode(Integer exitCode) { this.exitCode = exitCode; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
}
