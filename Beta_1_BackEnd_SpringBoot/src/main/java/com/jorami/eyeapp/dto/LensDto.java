package com.jorami.eyeapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LensDto {

    private long id;
    private long version;

    private String name;
    private String commentTradeName;

    // Valeur nominale
    private Float nominal;
    private Float haigisA0;
    private Float haigisA1;
    private Float haigisA2;
    private Float hofferQPACD;
    private Float holladay1SF;
    // aConstant
    private Float srkta;
    private Float castropC;
    private Float castropH;
    private Float castropR;

    // Valeur optimisée
    private Float haigisA0Optimized;
    private Float haigisA1Optimized;
    private Float haigisA2Optimized;
    private Float hofferQPACDOptimized;
    private Float holladay1SFOptimized;
    private Float cookeOptimized;
    // aConstant
    private Float srktaOptimized;
    private Float castropCOptimized;
    private Float castropHOptimized;
    private Float castropROptimized;

    private LensManufacturerDto lensManufacturer;

}