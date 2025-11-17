package com.nhom.weather_hub.service.impl;

import com.nhom.weather_hub.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String to, String token) {
        String verifyUrl = "http://localhost:8080/api/auth/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify your email - WeatherHub");
        message.setText("Click the link to verify your account: " + verifyUrl);

        mailSender.send(message);
    }
}
