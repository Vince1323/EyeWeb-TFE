package com.jorami.eyeapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private long id;
    private long version;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private RoleDto role;
    private boolean hasReadTermsAndConditions;
    private boolean verified;
    private boolean validEmail;
    private AddressDto address;
    private LocalDate birthDate;

    // Getters et Setters
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
