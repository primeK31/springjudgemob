package com.example.spring_boot.code.repository;

import com.example.spring_boot.code.model.CodeSubmission;
import com.example.spring_boot.code.reqresp.CodeSubmissionResponse;
import com.example.spring_boot.code.service.CodeSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Repository
public class CodeJudgeRepository {

    @Autowired
    private CodeSubmissionService codeSubmissionService;

    public CodeSubmissionResponse compileAndRun(String sourceCode) {
        return compileAndRun(sourceCode, "cpp", null, true);
    }

    public CodeSubmissionResponse compileAndRun(String sourceCode, String language) {
        return compileAndRun(sourceCode, language, null, true);
    }

    public CodeSubmissionResponse compileAndRun(String sourceCode, String language, String input) {
        return compileAndRun(sourceCode, language, input, true);
    }

    public CodeSubmissionResponse compileAndRun(String sourceCode, String language, String input, boolean saveToDatabase) {
        long startTime = System.currentTimeMillis();
        String output = "";
        String errorOutput = "";
        int exitCode = -1;

        try {
            Path tempDir = Files.createTempDirectory("judge");

            // Handle different languages
            switch (language.toLowerCase()) {
                case "cpp":
                case "c++":
                    return compileAndRunCpp(sourceCode, input, tempDir, saveToDatabase, startTime);
                default:
                    errorOutput = "Unsupported language: " + language;
                    exitCode = -1;
            }

        } catch (IOException | InterruptedException e) {
            errorOutput = "System error: " + e.getMessage();
            exitCode = -1;
        }

        if (saveToDatabase) {
            long executionTime = System.currentTimeMillis() - startTime;
            CodeSubmission submission = codeSubmissionService.saveSubmission(
                    sourceCode,
                    output.isEmpty() ? errorOutput : output,
                    exitCode,
                    language,
                    executionTime
            );
            return new CodeSubmissionResponse(output, errorOutput, exitCode, submission.getId(), executionTime);
        }

        return new CodeSubmissionResponse(output, errorOutput, exitCode);
    }

    private CodeSubmissionResponse compileAndRunCpp(String sourceCode, String input, Path tempDir,
                                                    boolean saveToDatabase, long startTime) throws IOException, InterruptedException {
        Path cppFile = tempDir.resolve("main.cpp");
        Files.writeString(cppFile, sourceCode);

        // Write input file if provided
        if (input != null && !input.isEmpty()) {
            Path inputFile = tempDir.resolve("input.txt");
            Files.writeString(inputFile, input);
        }

        List<String> command = Arrays.asList(
                "docker", "run", "--rm",
                "--memory=128m", "--cpus=0.5",
                "-v", tempDir.toAbsolutePath().toString() + ":/code",
                "gcc:13",
                "bash", "-c",
                input != null && !input.isEmpty() ?
                        "cd /code && g++ main.cpp -o main.out && ./main.out < input.txt" :
                        "cd /code && g++ main.cpp -o main.out && ./main.out"
        );

        return executeCommand(command, sourceCode, "cpp", saveToDatabase, startTime);
    }

    private CodeSubmissionResponse executeCommand(List<String> command, String sourceCode, String language,
                                                  boolean saveToDatabase, long startTime) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(false); // Separate stdout and stderr
        Process process = processBuilder.start();

        boolean finished = process.waitFor(5, TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();

            if (saveToDatabase) {
                long executionTime = System.currentTimeMillis() - startTime;
                CodeSubmission submission = codeSubmissionService.saveSubmission(
                        sourceCode,
                        "Execution timeout",
                        -1,
                        language,
                        executionTime
                );
                return new CodeSubmissionResponse("", "Execution timeout", -1, submission.getId(), executionTime);
            }

            return new CodeSubmissionResponse("", "Execution timeout", -1);
        }

        String output = new String(process.getInputStream().readAllBytes());
        String errorOutput = new String(process.getErrorStream().readAllBytes());
        int exitCode = process.exitValue();

        // Clean up temporary directory
        try {
            Files.walk(Paths.get(command.get(5).split(":")[0]))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (Exception e) {
            // Ignore cleanup errors
        }

        // Save to database if requested
        if (saveToDatabase) {
            long executionTime = System.currentTimeMillis() - startTime;
            String finalOutput = output.isEmpty() ? errorOutput : output + (errorOutput.isEmpty() ? "" : "\nErrors:\n" + errorOutput);

            CodeSubmission submission = codeSubmissionService.saveSubmission(
                    sourceCode,
                    finalOutput,
                    exitCode,
                    language,
                    executionTime
            );

            return new CodeSubmissionResponse(output, errorOutput, exitCode, submission.getId(), executionTime);
        }

        return new CodeSubmissionResponse(output, errorOutput, exitCode);
    }

    // Method to run without saving to database
    public CodeSubmissionResponse compileAndRunWithoutSaving(String sourceCode, String language) {
        return compileAndRun(sourceCode, language, null, false);
    }

    // Method to test code with input
    public CodeSubmissionResponse testCode(String sourceCode, String language, String input) {
        return compileAndRun(sourceCode, language, input, true);
    }
}
