package com.fwai.turtle.controller;

import com.fwai.turtle.common.ApiResponse;
import com.fwai.turtle.service.interfaces.SequenceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/sequences")
@RequiredArgsConstructor
public class SequenceController {

    private final SequenceService sequenceService;

    /**
     * 获取下一个序号
     *
     * @param type 序号类型
     * @return 格式化后的序号
     */
    @GetMapping("/next")
    public ApiResponse<String> getNextSequence(@RequestParam String type) {
        log.debug("getNextSequence: {}", type);
        String nextSequence = sequenceService.getNextSequence(type);
        return ApiResponse.ok(nextSequence);
    }
}
