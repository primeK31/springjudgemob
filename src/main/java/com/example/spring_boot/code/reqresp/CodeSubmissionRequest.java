package com.example.spring_boot.code.reqresp;

import jakarta.validation.constraints.NotBlank;

public class CodeSubmissionRequest {
    @NotBlank(message = "Source code cannot be empty")
    private String sourceCode;

    private String language = "cpp"; // Default language
    private String input; // Optional input for the program
    private String problemId; // Optional problem identifier

    public CodeSubmissionRequest() {}

    public CodeSubmissionRequest(String sourceCode, String language) {
        this.sourceCode = sourceCode;
        this.language = language;
    }

    // Getters and Setters
    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }

    public String getProblemId() { return problemId; }
    public void setProblemId(String problemId) { this.problemId = problemId; }
}