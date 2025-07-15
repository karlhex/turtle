package com.fwai.turtle.base.service;

public interface SequenceService {
    /**
     * 获取下一个序号
     *
     * @param type 序号类型
     * @return 格式化后的序号
     */
    String getNextSequence(String type);
}