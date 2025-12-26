package com.fwai.turtle.base.service;

import java.util.Map;

/**
 * EmailService
 * 邮件发送服务接口
 */
public interface EmailService {

    /**
     * 发送简单文本邮件
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    void sendSimpleEmail(String to, String subject, String content);

    /**
     * 发送HTML格式邮件
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param htmlContent HTML内容
     */
    void sendHtmlEmail(String to, String subject, String htmlContent);

    /**
     * 使用模板发送邮件
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param templateName 模板名称
     * @param variables 模板变量
     */
    void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables);

    /**
     * 批量发送邮件
     * @param toList 收件人列表
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    void sendBatchEmails(String[] toList, String subject, String content);
}