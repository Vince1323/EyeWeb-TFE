package com.jorami.eyeapp.model.exam;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jorami.eyeapp.model.Enum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExamForUpdate {

    @JsonProperty("al")
    private Float al;
    @JsonProperty("acd")
    private Float acd;
    @JsonProperty("pupilDia")
    private Float pupilDia;
    @JsonProperty("wtw")
    private Float wtw;
    @JsonProperty("cord")
    private Float cord;
    @JsonProperty("z40")
    private Float z40;
    //CCT | Pachy Center
    @JsonProperty("cct")
    private Float cct;
    @JsonProperty("k1")
    private Float k1;
    @JsonProperty("k1Axis")
    private Float k1Axis;
    @JsonProperty("k2")
    private Float k2;
    @JsonProperty("k2Axis")
    private Float k2Axis;
    @JsonProperty("kAstig")
    @Column(name = "kAstig")
    private Float k_astig;
    @JsonProperty("kAvg")
    @Column(name = "kAvg")
    private Float k_avg;
    //LT
    @JsonProperty("lensThickness")
    private Float lensThickness;
    //SNR
    @JsonProperty("snr")
    private Float snr;
    @Enumerated(EnumType.STRING)
    private Enum.Biometer machine;

}
