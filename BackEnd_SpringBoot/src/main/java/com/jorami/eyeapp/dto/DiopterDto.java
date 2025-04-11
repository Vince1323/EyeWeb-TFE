package com.jorami.eyeapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DiopterDto {

    private long id;
    private long version;
    private Float iolPower;
    private Float value;
    private String formula;

}