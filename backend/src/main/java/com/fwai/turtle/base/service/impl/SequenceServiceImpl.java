package com.fwai.turtle.base.service.impl;

import com.fwai.turtle.base.entity.Sequence;
import com.fwai.turtle.base.repository.SequenceRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fwai.turtle.base.service.SequenceService;

@Service
@RequiredArgsConstructor
public class SequenceServiceImpl implements SequenceService {

    private final SequenceRepository sequenceRepository;

    @Override
    @Transactional
    public String getNextSequence(String type) {
        Sequence sequence = sequenceRepository.findByTypeForUpdate(type)
                .orElseThrow(() -> new EntityNotFoundException("序号类型不存在: " + type));

        // 递增序号
        sequence.setCurrentValue(sequence.getCurrentValue() + 1);
        sequenceRepository.save(sequence);

        // 构建序号
        StringBuilder sb = new StringBuilder();
        sb.append(sequence.getPrefix());

        LocalDateTime now = LocalDateTime.now();
        
        if (Boolean.TRUE.equals(sequence.getIncludeYear())) {
            sb.append(now.format(DateTimeFormatter.ofPattern("yyyy")));
        }
        if (Boolean.TRUE.equals(sequence.getIncludeMonth())) {
            sb.append(now.format(DateTimeFormatter.ofPattern("MM")));
        }
        if (Boolean.TRUE.equals(sequence.getIncludeDay())) {
            sb.append(now.format(DateTimeFormatter.ofPattern("dd")));
        }

        // 添加序号，根据配置的长度，不足补0
        int seqLength = sequence.getSeqLength() != null ? sequence.getSeqLength() : 6;
        String formatPattern = "%0" + seqLength + "d";
        sb.append(String.format(formatPattern, sequence.getCurrentValue()));

        return sb.toString();
    }
}
