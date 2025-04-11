package com.jorami.eyeapp.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {

    private String email;
    private String password;
    private String resetPassword;
    private String code;

}
