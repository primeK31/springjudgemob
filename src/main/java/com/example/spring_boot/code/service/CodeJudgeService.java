package com.example.spring_boot.code.service;

import com.example.spring_boot.code.repository.CodeJudgeRepository;
import com.example.spring_boot.code.reqresp.CodeSubmissionRequest;
import com.example.spring_boot.code.reqresp.CodeSubmissionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CodeJudgeService {

    private final CodeJudgeRepository codeJudgeRepository;

    @Autowired
    public CodeJudgeService(CodeJudgeRepository codeJudgeRepository) {
        this.codeJudgeRepository = codeJudgeRepository;
    }

    public CodeSubmissionResponse judgeCode(CodeSubmissionRequest request) {
        return codeJudgeRepository.compileAndRun(request.getSourceCode());
    }
}
