package com.example.spring_boot.code.controller;

import com.example.spring_boot.code.repository.CodeJudgeRepository;
import com.example.spring_boot.code.reqresp.CodeSubmissionRequest;
import com.example.spring_boot.code.reqresp.CodeSubmissionResponse;
import com.example.spring_boot.code.service.CodeSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/judge")
public class CodeJudgeController {

    @Autowired
    private CodeJudgeRepository codeJudgeRepository;

    @Autowired
    private CodeSubmissionService codeSubmissionService;

    @PostMapping("/submit")
    public ResponseEntity<CodeSubmissionResponse> submitCode(@RequestBody CodeSubmissionRequest request) {
        CodeSubmissionResponse response = codeJudgeRepository.compileAndRun(
                request.getSourceCode(),
                request.getLanguage() != null ? request.getLanguage() : "cpp",
                request.getInput()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/test")
    public ResponseEntity<CodeSubmissionResponse> testCode(@RequestBody CodeSubmissionRequest request) {
        CodeSubmissionResponse response = codeJudgeRepository.compileAndRunWithoutSaving(
                request.getSourceCode(),
                request.getLanguage() != null ? request.getLanguage() : "cpp"
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submit/{language}")
    public ResponseEntity<CodeSubmissionResponse> submitCodeWithLanguage(
            @PathVariable String language,
            @RequestBody CodeSubmissionRequest request) {
        CodeSubmissionResponse response = codeJudgeRepository.compileAndRun(
                request.getSourceCode(),
                language,
                request.getInput()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/submission/{id}")
    public ResponseEntity<?> getSubmission(@PathVariable Long id) {
        return codeSubmissionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
