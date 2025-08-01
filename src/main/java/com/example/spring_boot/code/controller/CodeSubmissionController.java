package com.example.spring_boot.code.controller;


import com.example.spring_boot.code.model.CodeSubmission;
import com.example.spring_boot.code.reqresp.CodeSubmissionRequest;
import com.example.spring_boot.code.reqresp.CodeSubmissionResponse;
import com.example.spring_boot.code.service.CodeSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/submissions")
public class CodeSubmissionController {

    @Autowired
    private CodeSubmissionService codeSubmissionService;
    /*
    @PostMapping
    public ResponseEntity<CodeSubmission> createSubmission(@RequestBody CodeSubmissionRequest request) {
        CodeSubmissionResponse response = new CodeSubmissionResponse();
        CodeSubmission saved = codeSubmissionService.saveSubmission(
                request.getSourceCode(),
                response.getOutput(),
                response.getExitCode(),
                request.getLanguage(),
                response.getExecutionTimeMs()
        );
        return ResponseEntity.ok(saved);
    }
     */

    @GetMapping("/{id}")
    public ResponseEntity<CodeSubmission> getSubmission(@PathVariable Long id) {
        Optional<CodeSubmission> submission = codeSubmissionService.findById(id);
        return submission.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<CodeSubmission>> getAllSubmissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CodeSubmission> submissions = codeSubmissionService.findAll(
                PageRequest.of(page, size)
        );
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/successful")
    public ResponseEntity<List<CodeSubmission>> getSuccessfulSubmissions() {
        List<CodeSubmission> submissions = codeSubmissionService.findSuccessfulSubmissions();
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<List<CodeSubmission>> getSubmissionsByLanguage(@PathVariable String language) {
        List<CodeSubmission> submissions = codeSubmissionService.findByLanguage(language);
        return ResponseEntity.ok(submissions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        codeSubmissionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
