package com.example.spring_boot;

import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/judge")
public class JudgeController {

    @PostMapping
    public CodeResponse runCode(@RequestBody CodeRequest request) throws IOException, InterruptedException {
        String sourceCode = request.getCode();
        Path tempDir = Files.createTempDirectory("judge");
        Path cppFile = tempDir.resolve("main.cpp");
        Files.writeString(cppFile, sourceCode);

        // docker run --rm -v /host/tempDir:/code gcc:13 bash -c "cd /code && g++ main.cpp -o main.out && ./main.out"
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
            return new CodeResponse("", "Execution timeout", -1);
        }

        String output = new String(process.getInputStream().readAllBytes());
        int exitCode = process.exitValue();

        return new CodeResponse(output, "", exitCode);
    }

}
