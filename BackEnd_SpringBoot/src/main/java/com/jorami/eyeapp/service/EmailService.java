package com.jorami.eyeapp.service;

import com.jorami.eyeapp.model.User;
import com.jorami.eyeapp.auth.model.VerificationCode;
import com.jorami.eyeapp.strategy.EmailStrategy;

import java.io.UnsupportedEncodingException;

public interface EmailService {
    void sendEmail(User user, String code, EmailStrategy emailStrategy) throws IllegalStateException, UnsupportedEncodingException;

    VerificationCode validateCodeExist(String code);

}