package com.jorami.eyeapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConstantDto {

    private long id;
    private long version;
    private String constantType;
    private String value;
    private String formula;

}
