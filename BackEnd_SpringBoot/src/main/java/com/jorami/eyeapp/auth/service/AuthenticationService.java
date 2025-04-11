package com.jorami.eyeapp.auth.service;

import com.jorami.eyeapp.auth.model.*;
import com.jorami.eyeapp.model.User;

import java.io.UnsupportedEncodingException;

public interface AuthenticationService {

    void register(RegistrationRequest request) throws UnsupportedEncodingException;

    AuthenticationResponse authentication(AuthenticationRequest authenticationRequest);

    void confirmResetPassword(User user) throws UnsupportedEncodingException;

    void validateCode(VerificationCode verificationCode) throws UnsupportedEncodingException;

    User resetPassword(User userSource, ResetPasswordRequest resetPasswordRequest);

}
