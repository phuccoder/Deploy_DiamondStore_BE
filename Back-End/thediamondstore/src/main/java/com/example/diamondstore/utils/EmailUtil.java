package com.example.diamondstore.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender javaMailSender;
    
    public String loadEmailTemplate_SetPassword() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("template/SetPassword.html");

        if (inputStream == null) {
            throw new IOException("File not found: template/SetPassword.html");
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }

    public String loadEmailTemplate_OtpEmail() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("template/OtpEmail.html");

        if (inputStream == null) {
            throw new IOException("File not found: template/OtpEmail.html");
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }
    
    public void sendOtpEmail(String email, String otp) throws MessagingException { 
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Xác Thực OTP The Diamond Store");
        
        String emailContent;
        try {
            emailContent = loadEmailTemplate_OtpEmail();
            emailContent = emailContent.replace("{{otp}}", otp);
            emailContent = emailContent.replace("{{email}}", email);
        } catch (IOException e) {
            emailContent = "Default email content if template cannot be read";
        }
        
        mimeMessageHelper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    public void sendSetPasswordEmail(String email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Yêu Cầu Đặt Lại Mật Khẩu The Diamond Store");
        
        String emailContent;
        try {
            emailContent = loadEmailTemplate_SetPassword();
            emailContent = emailContent.replace("{{email}}", email);
        } catch (IOException e) {
            emailContent = "Default email content if template cannot be read";
        }
        
        mimeMessageHelper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }
}
