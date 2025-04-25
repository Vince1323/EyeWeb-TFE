package com.jorami.eyeapp.dto.exam;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jorami.eyeapp.model.Enum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExamDto {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("version")
    private Long version;

    //OD, OS ou OU
    @JsonProperty("eyeSide")
    private Enum.EyeSide eyeSide;
    //Uniquement pour Pentacam (25 ou 50)
    @JsonProperty("examType")
    private String examType;
    @JsonProperty("examDate")
    private String examDate;
    @JsonProperty("examComment")
    private String examComment;
    //Statut
    @JsonProperty("examQuality")
    private String examQuality;
    @JsonProperty("calculDate")
    private String calculDate;

    //État œil
    @JsonProperty("eyeStatus")
    private String eyeStatus;
    @JsonProperty("selected")
    private boolean selected;

    //AL | AXL | Used Axial Length | Optical AXL
    @JsonProperty("al")
    private Float al;
    //ACD | Anterior Chamber Depth (External ou Epithelial)
    @JsonProperty("acd")
    private Float acd;
    //AD | Anterior Chamber Depth Endothelial | Intern | Anterior Aqueous Depth
    @JsonProperty("internalAcd")
    private Float internalAcd;
    //Pupil dia | Pupil Size | Pupil Diameter | P (biomètre Argos)
    @JsonProperty("pupilDia")
    private Float pupilDia;
    //Pupil min | Pupil Photopic
    @JsonProperty("pupilMin")
    private Float pupilMin;
    //Pupil max | Pupil Scotopic
    @JsonProperty("pupilMax")
    private Float pupilMax;
    //HWTW | WTW | WTOW | Blanc à blanc | Cornea dia
    @JsonProperty("wtw")
    private Float wtw;
    //Cord µ
    @JsonProperty("cord")
    private Float cord;
    //Abérration Sphérique
    @JsonProperty("z40")
    private Float z40;
    //High Order Aberration
    @JsonProperty("hoa")
    private Float hoa;
    //Angle Kappa
    @JsonProperty("kappaAngle")
    private Float kappaAngle;

    //Keratometry Index
    @JsonProperty("n")
    private Float n;
    @JsonProperty("k1")
    private Float k1;
    @JsonProperty("k1Mm")
    private Float k1Mm;
    @JsonProperty("k1Axis")
    private Float k1Axis;
    @JsonProperty("k2")
    private Float k2;
    @JsonProperty("k2Mm")
    private Float k2Mm;
    @JsonProperty("k2Axis")
    private Float k2Axis;
    @JsonProperty("kAstig")
    private Float k_astig;
    @JsonProperty("kAstigAxis")
    private Float k_astigAxis;
    @JsonProperty("kAvg")
    private Float k_avg;
    @JsonProperty("rAvg")
    private Float r_avg;
    @JsonProperty("k1CorneaBack")
    private Float k1CorneaBack;
    @JsonProperty("k2CorneaBack")
    private Float k2CorneaBack;
    @JsonProperty("k1AxisCorneaBack")
    private Float k1AxisCorneaBack;
    @JsonProperty("k2AxisCorneaBack")
    private Float k2AxisCorneaBack;

    //ZEISS
    @JsonProperty("siaCyl")
    private Float siaCyl;
    @JsonProperty("siaAxis")
    private Float siaAxis;

    //SNR
    @JsonProperty("snr")
    private Float snr;
    //LT
    @JsonProperty("lensThickness")
    private Float lensThickness;
    //CCT | Pachy Center
    @JsonProperty("cct")
    private Float cct;
    @JsonProperty("cctMin")
    private Float cctMin;

    //Asphericity
    @JsonProperty("asphQf")
    private Float asphQf;
    @JsonProperty("asphQb")
    private Float asphQb;
    @JsonProperty("alStatus")
    private Float alStatus;
    @JsonProperty("alError")
    private Float alError;
    @JsonProperty("kPreRefrAvg")
    private Float k_preRefrAvg;
    @JsonProperty("rPreRefrAvg")
    private Float r_preRefrAvg;
    @JsonProperty("manifestRefrDate")
    private String manifestRefrDate;
    @JsonProperty("manifestRefrSph")
    private Float manifestRefrSph;
    @JsonProperty("manifestRefrCyl")
    private Float manifestRefrCyl;
    @JsonProperty("manifestRefrAxis")
    private Float manifestRefrAxis;
    @JsonProperty("manifestRefrVd")
    private Float manifestRefrVd;
    @JsonProperty("targetRefrSph")
    private Float targetRefrSph;
    @JsonProperty("targetRefrCyl")
    private Float targetRefrCyl;

    @JsonProperty("machine")
    private String machine;
    @JsonProperty("importType")
    private String importType;
    private Long patientId;
}

