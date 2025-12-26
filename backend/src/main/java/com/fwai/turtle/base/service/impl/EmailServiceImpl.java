package com.fwai.turtle.base.service.impl;

import com.fwai.turtle.base.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;

/**
 * EmailServiceImpl
 * 邮件发送服务实现类
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired(required = false)
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username:noreply@example.com}")
    private String fromEmail;

    @Override
    public void sendSimpleEmail(String to, String subject, String content) {
        if (mailSender == null) {
            System.out.println("邮件服务未配置，模拟发送邮件：");
            System.out.println("收件人: " + to);
            System.out.println("主题: " + subject);
            System.out.println("内容: " + content);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        if (mailSender == null) {
            System.out.println("邮件服务未配置，模拟发送HTML邮件：");
            System.out.println("收件人: " + to);
            System.out.println("主题: " + subject);
            System.out.println("HTML内容: " + htmlContent.substring(0, Math.min(100, htmlContent.length())) + "...");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    @Override
    public void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        if (templateEngine == null) {
            System.out.println("模板引擎未配置，模拟发送模板邮件：");
            System.out.println("收件人: " + to);
            System.out.println("主题: " + subject);
            System.out.println("模板: " + templateName);
            System.out.println("变量: " + variables);
            return;
        }

        try {
            Context context = new Context();
            context.setVariables(variables);

            String htmlContent = templateEngine.process(templateName, context);

            sendHtmlEmail(to, subject, htmlContent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send template email", e);
        }
    }

    @Override
    public void sendBatchEmails(String[] toList, String subject, String content) {
        for (String to : toList) {
            try {
                sendSimpleEmail(to, subject, content);
            } catch (Exception e) {
                // 记录错误但继续发送其他邮件
                System.err.println("Failed to send email to: " + to + ", error: " + e.getMessage());
            }
        }
    }
}