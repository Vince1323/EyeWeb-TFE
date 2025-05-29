package com.jorami.eyeapp.model.exam;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jorami.eyeapp.model.Enum;
import com.jorami.eyeapp.model.IdentifiedModel;
import com.jorami.eyeapp.model.Link;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Audited
@Where(clause = "deleted = FALSE")
@Entity
@Table(name = "exam")
public class Exam extends IdentifiedModel {

    @JsonProperty("eyeSide")
    @Enumerated(EnumType.STRING)
    private com.jorami.eyeapp.model.Enum.EyeSide eyeSide;
    @JsonProperty("examType")
    private String examType;
    @JsonProperty("examDate")
    private LocalDateTime examDate;
    @JsonProperty("examComment")
    private String examComment;
    @JsonProperty("examQuality")
    private String examQuality;
    @JsonProperty("calculDate")
    private LocalDateTime calculDate;

    @JsonProperty("eyeStatus")
    private String eyeStatus;
    @JsonProperty("selected")
    @ColumnDefault("true")
    private boolean selected;

    @JsonProperty("al")
    private Float al;
    @JsonProperty("acd")
    private Float acd;
    @JsonProperty("internalAcd")
    private Float internalAcd;
    @JsonProperty("pupilDia")
    private Float pupilDia;
    @JsonProperty("pupilMin")
    private Float pupilMin;
    @JsonProperty("pupilMax")
    private Float pupilMax;
    @JsonProperty("wtw")
    private Float wtw;
    @JsonProperty("cord")
    private Float cord;
    @JsonProperty("z40")
    private Float z40;
    @JsonProperty("hoa")
    private Float hoa;
    @JsonProperty("kappaAngle")
    private Float kappaAngle;

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
    @Column(name = "kAstig")
    private Float k_astig;
    @JsonProperty("kAstigAxis")
    @Column(name = "kAstigAxis")
    private Float k_astigAxis;
    @JsonProperty("kAvg")
    @Column(name = "kAvg")
    private Float k_avg;
    @JsonProperty("rAvg")
    @Column(name = "rAvg")
    private Float r_avg;
    @JsonProperty("k1CorneaBack")
    private Float k1CorneaBack;
    @JsonProperty("k2CorneaBack")
    private Float k2CorneaBack;
    @JsonProperty("k1AxisCorneaBack")
    private Float k1AxisCorneaBack;
    @JsonProperty("k2AxisCorneaBack")
    private Float k2AxisCorneaBack;

    @JsonProperty("siaCyl")
    private Float siaCyl;
    @JsonProperty("siaAxis")
    private Float siaAxis;

    @JsonProperty("snr")
    private Float snr;
    @JsonProperty("lensThickness")
    private Float lensThickness;
    @JsonProperty("cct")
    private Float cct;
    @JsonProperty("cctMin")
    private Float cctMin;

    @JsonProperty("asphQf")
    private Float asphQf;
    @JsonProperty("asphQb")
    private Float asphQb;
    @JsonProperty("alStatus")
    private Float alStatus;
    @JsonProperty("alError")
    private Float alError;
    @JsonProperty("kPreRefrAvg")
    @Column(name = "kPreRefrAvg")
    private Float k_preRefrAvg;
    @JsonProperty("rPreRefrAvg")
    @Column(name = "rPreRefrAvg")
    private Float r_preRefrAvg;
    @JsonProperty("manifestRefrDate")
    private LocalDateTime manifestRefrDate;
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
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Enum.Biometer machine;

    @JsonProperty("importType")
    private String importType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exam", cascade = CascadeType.ALL)
    @NotAudited
    private List<Link> links;


    @Override
    public String toString() {
        return "Biometry{" +
                "eyeSide=" + eyeSide +
                ", examType='" + examType + '\'' +
                ", examDate='" + examDate + '\'' +
                ", examComment='" + examComment + '\'' +
                ", examQuality='" + examQuality + '\'' +
                ", calculDate='" + calculDate + '\'' +
                ", eyeStatus='" + eyeStatus + '\'' +
                ", selected=" + selected +
                ", al='" + al + '\'' +
                ", acd='" + acd + '\'' +
                ", internalAcd='" + internalAcd + '\'' +
                ", pupilDia='" + pupilDia + '\'' +
                ", pupilMin='" + pupilMin + '\'' +
                ", pupilMax='" + pupilMax + '\'' +
                ", wtw='" + wtw + '\'' +
                ", cord='" + cord + '\'' +
                ", z40='" + z40 + '\'' +
                ", hoa='" + hoa + '\'' +
                ", kappaAngle='" + kappaAngle + '\'' +
                ", n='" + n + '\'' +
                ", k1='" + k1 + '\'' +
                ", k1Mm='" + k1Mm + '\'' +
                ", k1Axis='" + k1Axis + '\'' +
                ", k2='" + k2 + '\'' +
                ", k2Mm='" + k2Mm + '\'' +
                ", k2Axis='" + k2Axis + '\'' +
                ", kAstig='" + k_astig + '\'' +
                ", kAstigAxis='" + k_astigAxis + '\'' +
                ", kAvg='" + k_avg + '\'' +
                ", rAvg='" + r_avg + '\'' +
                ", k1CorneaBack='" + k1CorneaBack + '\'' +
                ", k2CorneaBack='" + k2CorneaBack + '\'' +
                ", k1AxisCorneaBack='" + k1AxisCorneaBack + '\'' +
                ", k2AxisCorneaBack='" + k2AxisCorneaBack + '\'' +
                ", siaCyl='" + siaCyl + '\'' +
                ", siaAxis='" + siaAxis + '\'' +
                ", snr='" + snr + '\'' +
                ", lensThickness='" + lensThickness + '\'' +
                ", cct='" + cct + '\'' +
                ", cctMin='" + cctMin + '\'' +
                ", asphQf='" + asphQf + '\'' +
                ", asphQb='" + asphQb + '\'' +
                ", alStatus='" + alStatus + '\'' +
                ", alError='" + alError + '\'' +
                ", kPreRefrAvg='" + k_preRefrAvg + '\'' +
                ", rPreRefrAvg='" + r_preRefrAvg + '\'' +
                ", manifestRefrDate='" + manifestRefrDate + '\'' +
                ", manifestRefrSph='" + manifestRefrSph + '\'' +
                ", manifestRefrCyl='" + manifestRefrCyl + '\'' +
                ", manifestRefrAxis='" + manifestRefrAxis + '\'' +
                ", manifestRefrVd='" + manifestRefrVd + '\'' +
                ", targetRefrSph='" + targetRefrSph + '\'' +
                ", targetRefrCyl='" + targetRefrCyl + '\'' +
                ", biometer='" + machine + '\'' +
                ", importType='" + importType + '\'' +
                '}';
    }

    public void addLink(Link link) {
        links.add(link);
        link.setExam(this);
    }
}