package com.jorami.eyeapp.auth.model;

import com.jorami.eyeapp.exception.ConstantMessage;
import com.jorami.eyeapp.model.Address;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest extends AuthenticationRequest {

    @NotBlank(message = ConstantMessage.VALIDATION_NOT_BLANK)
    private String firstname;
    @NotBlank(message = ConstantMessage.VALIDATION_NOT_BLANK)
    private String lastname;
    private String birthDate;
    private String phone;
    @NonNull
    private Address address;
    private boolean hasReadTermsAndConditions;

}
