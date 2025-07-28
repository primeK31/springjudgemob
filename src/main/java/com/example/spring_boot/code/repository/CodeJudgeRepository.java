package com.example.spring_boot.code.repository;

import com.example.spring_boot.code.reqresp.CodeSubmissionResponse;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Repository
public class CodeJudgeRepository {


    public CodeSubmissionResponse compileAndRun(String sourceCode) {
        try {
            Path tempDir = Files.createTempDirectory("judge");
            Path cppFile = tempDir.resolve("main.cpp");
            Files.writeString(cppFile, sourceCode);

            List<String> command = Arrays.asList(
                    "docker", "run", "--rm",
                    "--memory=128m", "--cpus=0.5",
                    "-v", tempDir.toAbsolutePath().toString() + ":/code",
                    "gcc:13",
                    "bash", "-c", "cd /code && g++ main.cpp -o main.out && ./main.out"
            );

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            boolean finished = process.waitFor(5, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                return new CodeSubmissionResponse("", "Execution timeout", -1);
            }

            String output = new String(process.getInputStream().readAllBytes());
            int exitCode = process.exitValue();
            return new CodeSubmissionResponse(output, "", exitCode);

        } catch (IOException | InterruptedException e) {
            return new CodeSubmissionResponse("", e.getMessage(), -1);
        }
    }
}
