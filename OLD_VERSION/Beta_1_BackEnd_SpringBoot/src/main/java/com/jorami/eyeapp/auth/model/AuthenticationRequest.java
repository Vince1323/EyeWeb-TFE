package com.jorami.eyeapp.auth.model;

import com.jorami.eyeapp.exception.ConstantMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = ConstantMessage.VALIDATION_NOT_BLANK)
    @Email(message = ConstantMessage.VALIDATION_EMAIL)
    private String email;
    @NotBlank(message = ConstantMessage.VALIDATION_NOT_BLANK)
    @Size(min = 8, message = ConstantMessage.VALIDATION_PASSWORD_MIN_LENGTH)
    @Size(max = 100, message = ConstantMessage.VALIDATION_PASSWORD_MAX_LENGTH)
    private String password;

}
