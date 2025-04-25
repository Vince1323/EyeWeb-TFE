package com.jorami.eyeapp.auth.controller;

import com.jorami.eyeapp.auth.model.*;
import com.jorami.eyeapp.auth.service.AuthenticationService;
import com.jorami.eyeapp.auth.service.LogoutService;
import com.jorami.eyeapp.model.User;
import com.jorami.eyeapp.service.EmailService;
import com.jorami.eyeapp.service.UserService;
import com.jorami.eyeapp.util.mapper.UserMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;

import static com.jorami.eyeapp.exception.ConstantMessage.*;


@CrossOrigin(
        origins = {"*"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        maxAge = 3600L
)
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final LogoutService logoutService;
    private final UserService userService;
    private final EmailService emailService;
    private final UserMapper mapper;


    @GetMapping("/health")
    public String getHealth() {
        return "The API is healthy";
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) throws UnsupportedEncodingException {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authentication(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authentication(request));
    }

    @PostMapping("/logout")
    public void logout(@RequestBody HttpServletRequest request, @RequestBody HttpServletResponse response, @RequestBody Authentication authentication) {
        logoutService.logout(request, response, authentication);
    }

    @PostMapping("/send-code-reset-password")
    public ResponseEntity<?> sendEmailCode(@RequestBody AuthenticationRequest request) throws UnsupportedEncodingException {
        Optional<User> user = userService.findUserByEmail(request);
        if (user.isPresent()) {
            authenticationService.confirmResetPassword(user.get());
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ResetPasswordRequest resetRequest) {
        VerificationCode verificationCode = emailService.validateCodeExist(resetRequest.getCode());
        if(verificationCode != null) {
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(resetRequest.getEmail(),resetRequest.getPassword());
            Optional<User> userOptional = userService.findUserByEmail(authenticationRequest);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                return ResponseEntity.ok(mapper.toUserDto(authenticationService.resetPassword(user, resetRequest)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(JSON_KEY, UNAUTHORIZED_EMAIL_PASSWORD));
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(JSON_KEY, CODE_EMAIL_VALIDATION));
        }
    }

    @PutMapping("/verify-email/{code}")
    public ResponseEntity<?> verifyEmail(@PathVariable("code") String code) throws UnsupportedEncodingException {
        VerificationCode verificationCode = emailService.validateCodeExist(code);
        if(verificationCode != null) {
            authenticationService.validateCode(verificationCode);
            return ResponseEntity.ok(verificationCode);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(JSON_KEY, CODE_EMAIL_VALIDATION));
        }
    }

}
