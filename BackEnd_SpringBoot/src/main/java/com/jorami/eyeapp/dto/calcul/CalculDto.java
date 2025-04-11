package com.jorami.eyeapp.dto.calcul;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jorami.eyeapp.dto.ConstantDto;
import com.jorami.eyeapp.dto.DiopterDto;
import com.jorami.eyeapp.dto.LensDto;
import com.jorami.eyeapp.dto.exam.ExamDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CalculDto {

    private String eyeSide;
    private ExamDto exam;
    private LensDto lens;
    @JsonProperty("constants")
    private List<ConstantDto> constants;
    private List<DiopterDto> diopters;
    private Float targetRefraction;
    private Boolean isSecondEye;
    private Float precPowerSelected;
    private Float se;
    private Long idReferencePrecExam;
    private Long idReferencePrecCalcul;
}