package com.nhom.weather_hub.service;

public interface MailService {

    void sendVerificationEmail(String to, String token);

}
