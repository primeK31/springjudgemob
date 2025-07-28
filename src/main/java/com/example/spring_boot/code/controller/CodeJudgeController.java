package com.example.spring_boot.code.controller;

import com.example.spring_boot.code.service.CodeJudgeService;
import com.example.spring_boot.code.reqresp.CodeSubmissionRequest;
import com.example.spring_boot.code.reqresp.CodeSubmissionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/judge")
public class CodeJudgeController {
    private final CodeJudgeService codeService;

    @Autowired
    public CodeJudgeController(CodeJudgeService codeService) {
        this.codeService = codeService;
    }

    @PostMapping
    public CodeSubmissionResponse runCode(@RequestBody CodeSubmissionRequest request) {
        return codeService.judgeCode(request);
    }
}
