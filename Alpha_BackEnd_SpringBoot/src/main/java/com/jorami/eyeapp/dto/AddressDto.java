package com.jorami.eyeapp.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressDto {

    private Long id;
    private Long version;

    private String street;
    private String streetNumber;
    private String boxNumber;
    private String zipCode;
    private String city;
    private String country;

}