package com.jorami.eyeapp.util;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.Map;

@Getter
public class SelectorConfig {

    //Importation
    @SerializedName("pentacamBiometryImportation")
    private Map<String, String> pentacamBiometryImportationSelector;

    @SerializedName("iolMasterBiometryImportation")
    private Map<String, String> iolMasterBiometryImportationSelector;


    //Ocr
    @SerializedName("pentacamOcr")
    private Map<String, String> pentacamOcrSelector;

    @SerializedName("iolMaster500Ocr")
    private Map<String, String> iolMaster500OcrSelector;

    @SerializedName("iolMaster700Ocr")
    private Map<String, String> iolMaster700OcrSelector;

    @SerializedName("argosOcr")
    private Map<String, String> argosOcrSelector;

    @SerializedName("topConOcr")
    private Map<String, String> topConOcrSelector;

    @SerializedName("anterionOcr")
    private Map<String, String> anterionOcrSelector;


    //Injection
    @SerializedName("pearl")
    private Map<String, String> pearlSelectors;

    @SerializedName("kane")
    private Map<String, String> kaneSelectors;

    @SerializedName("barrett")
    private Map<String, String> barrettSelectors;

    @SerializedName("barrettTrueK")
    private Map<String, String> barretTrueKSelectors;

    @SerializedName("evo")
    private Map<String, String> evoSelectors;

    @SerializedName("cooke")
    private Map<String, String> cookeSelectors;

    @SerializedName("hoffer")
    private Map<String, String> hofferSelectors;
}
