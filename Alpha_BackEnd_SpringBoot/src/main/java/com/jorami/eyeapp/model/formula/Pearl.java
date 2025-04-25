package com.jorami.eyeapp.model.formula;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jorami.eyeapp.model.Calcul;
import com.jorami.eyeapp.model.Enum;
import com.jorami.eyeapp.model.exam.Exam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@NoArgsConstructor
@Getter
@Setter
public class Pearl {

    @JsonProperty("side")
    private boolean side;
    @JsonProperty("AL")
    private Double AL;
    @JsonProperty("K1")
    private Double K1;
    @JsonProperty("K2")
    private Double K2;
    @JsonProperty("ACD")
    private Double ACD;
    @JsonProperty("biometer")
    private String biometer;
    @JsonProperty("IOLModel")
    private String IOLModel;
    @JsonProperty("target")
    private Double target;
    @JsonProperty("isNonPhysioCornea")
    private boolean isNonPhysioCornea;
    @JsonProperty("isICL")
    private boolean isICL;
    @JsonProperty("isRK")
    private boolean isRK;
    @JsonProperty("isCornealLaser")
    private boolean isCornealLaser;
    @JsonProperty("isSecond")
    private boolean isSecond;
    @JsonProperty("cornealLaserType")
    private String cornealLaserType;
    @JsonProperty("aconst")
    private Double aconst;
    @JsonProperty("LT")
    private Double LT;
    @JsonProperty("CCT")
    private Double CCT;
    @JsonProperty("AL2")
    private Double AL2;
    @JsonProperty("K12")
    private Double K12;
    @JsonProperty("K22")
    private Double K22;
    @JsonProperty("ACD2")
    private Double ACD2;
    @JsonProperty("LT2")
    private Double LT2;
    @JsonProperty("CCT2")
    private Double CCT2;
    @JsonProperty("power2")
    private Double power2;
    @JsonProperty("SE2")
    private Double SE2;


    public Pearl(boolean side, Double AL, Double K1, Double K2, Double ACD, String biometer, String IOLModel, Double target,
                 boolean isNonPhysioCornea, boolean isICL, boolean isRK, boolean isCornealLaser, boolean isSecond,
                 String cornealLaserType, Double aconst, Double LT , Double CCT) {
        this.side = side;
        this.AL = AL;
        this.K1 = K1;
        this.K2 = K2;
        this.ACD = ACD;
        this.biometer = biometer;
        this.IOLModel = IOLModel;
        this.target = target;
        this.isNonPhysioCornea = isNonPhysioCornea;
        this.isICL = isICL;
        this.isRK = isRK;
        this.isCornealLaser = isCornealLaser;
        this.isSecond = isSecond;
        this.cornealLaserType = cornealLaserType;
        this.aconst = aconst;
        this.LT = LT;
        this.CCT = CCT;
    }

    public Pearl(Exam biometry, Map<String, String> constantes, Calcul newCalcul) {
        setSide(biometry.getEyeSide().toString());
        setAL(Double.valueOf(biometry.getAl()));
        setK1(Double.valueOf(biometry.getK1()));
        setK2(Double.valueOf(biometry.getK2()));
        setACD(Double.valueOf(biometry.getAcd()));
        setBiometer(String.valueOf(biometry.getMachine()));
        setIOLModel(newCalcul.getLens().getName());
        setTarget(Double.valueOf(newCalcul.getTargetRefraction()));
        setNonPhysioCornea(false);
        setICL(false);
        setRK(false);
        setCornealLaser(false);
        setSecond(false);
        setCornealLaserType("Myopic");
        setAconst(Double.valueOf(constantes.get(Enum.Formula.PEARL.getValue())));
        setLT((biometry.getLensThickness() != null) ? (checkRangeLT(biometry.getLensThickness()) ? Double.valueOf(biometry.getLensThickness()) : null) : null);
        setCCT((biometry.getCct() != null) ? (checkRangeCCT(biometry.getCct()) ? Double.valueOf(biometry.getCct()) : null) : null);
    }

    public Pearl(Exam biometry, Map<String, String> constantes, Calcul newCalcul, Exam precExam) {
        setSide(biometry.getEyeSide().toString());
        setAL(Double.valueOf(biometry.getAl()));
        setK1(Double.valueOf(biometry.getK1()));
        setK2(Double.valueOf(biometry.getK2()));
        setACD(Double.valueOf(biometry.getAcd()));
        setBiometer(String.valueOf(biometry.getMachine()));
        setIOLModel(newCalcul.getLens().getName());
        setTarget(Double.valueOf(newCalcul.getTargetRefraction()));
        setNonPhysioCornea(false);
        setICL(false);
        setRK(false);
        setCornealLaser(false);
        setSecond(newCalcul.getIsSecondEye());
        setCornealLaserType("Myopic");
        setAconst(Double.valueOf(constantes.get(Enum.Formula.PEARL.getValue())));
        setLT((biometry.getLensThickness() != null) ? (checkRangeLT(biometry.getLensThickness()) ? Double.valueOf(biometry.getLensThickness()) : null) : null);
        setCCT((biometry.getCct() != null) ? (checkRangeCCT(biometry.getCct()) ? Double.valueOf(biometry.getCct()) : null) : null);
        setAL2(Double.valueOf(precExam.getAl()));
        setK12(Double.valueOf(precExam.getK1()));
        setK22(Double.valueOf(precExam.getK2()));
        setACD2(Double.valueOf(precExam.getAcd()));
        setLT2((precExam.getLensThickness() != null) ? (checkRangeLT(precExam.getLensThickness()) ? Double.valueOf(precExam.getLensThickness()) : null) : null);
        setCCT2((precExam.getCct() != null) ? (checkRangeCCT(precExam.getCct()) ? Double.valueOf(precExam.getCct()) : null) : null);
        setPower2(Double.valueOf(newCalcul.getPrecPowerSelected()));
        setSE2(Double.valueOf(newCalcul.getSe()));
    }

    public void setSide(String side) {
        this.side = side.equals("OD");
    }

/*
    public void setBiometer(String biometer) {
        if(Stream.of("Argos", "USimmersion", "USaplanation").anyMatch((biometer::contains))) {
            this.biometer = biometer;
        } else {
            this.biometer = "Other";
        }
    }

    public void setIOLModel(String IOLModel) {
        if(Stream.of("Finevision", "Other_Men", "Panoptix", "Vivinex", "SN60WF", "PCB",
                "MX60", "SA60AT", "Asphina", "60MA", "A1-UV", "AR40", "RayOne").anyMatch((IOLModel::contains))) {
            this.IOLModel = IOLModel;
        } else {
            this.IOLModel = "Other_Men";
        }
    }
 */

    public void setBiometer(String biometer) {
        List<String> validBiometer = List.of("Argos", "USimmersion", "USaplanation");
        this.biometer = validBiometer.stream()
                .filter(biometer::contains)
                .findFirst()
                .orElse("Other");
    }

    public void setIOLModel(String IOLModel) {
        List<String> validModels = List.of(
                "Finevision", "Other_Men", "Panoptix", "Vivinex", "SN60WF", "PCB",
                "MX60", "SA60AT", "Asphina", "60MA", "A1-UV", "AR40", "RayOne"
        );
        this.IOLModel = validModels.stream()
                .filter(IOLModel::contains)
                .findFirst()
                .orElse("Other_Men");
    }

    private boolean checkRangeLT(Float LT) {
        return (LT >= 2.5 && LT <= 8);
    }

    private boolean checkRangeCCT(Float CCT) {
        return (CCT >= 350 && CCT <= 700);
    }

    public static String extractErrors(String json) {
        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            JsonArray details = jsonObject.getAsJsonArray("detail");
            StringBuilder errorMessages = new StringBuilder();

            for (JsonElement element : details) {
                JsonObject error = element.getAsJsonObject();
                JsonArray locArray = error.getAsJsonArray("loc");
                if (locArray.size() >= 2) {
                    String fieldName = locArray.get(1).getAsString();
                    String message = error.get("msg").getAsString();
                    errorMessages.append(fieldName).append(": ").append(message).append("\n");
                }
            }
            return errorMessages.toString().trim();
        } catch (Exception e) {
            return "Failed to parse error response: " + json;
        }
    }
}
