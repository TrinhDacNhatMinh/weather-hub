package com.nhom.weather_hub.service;

public interface MailService {

    public void sendVerificationEmail(String to, String token);

}
