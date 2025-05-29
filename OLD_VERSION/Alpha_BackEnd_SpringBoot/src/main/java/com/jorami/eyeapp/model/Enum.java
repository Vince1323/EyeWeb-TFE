package com.jorami.eyeapp.model;

import lombok.Getter;

public final class Enum {

    public enum UserRole {
        ADMIN,
        USER
    }

    @Getter
    public enum OrganizationRole {
        ADMIN,
        SURGEON,
        NURSE,
        SECRETARY
    }

    @Getter
    public enum Gender {
        MALE,
        FEMALE
    }

    @Getter
    public enum EyeSide {
        OD,
        OS,
        OU
    }

    @Getter
    public enum Workflow {
        CATARACT,
        REFRACTIVE,
        GLAUCOMA
    }

    @Getter
    public enum Biometer {
        HAAG_STREIT_LS_900("HAAG-STREIT LENSTAR LS 900"),
        HAAG_STREIT_ES_900("HAAG-STREIT EYESTAR ES 900"),
        ZEISS_IOLMASTER_500("ZEISS IOLMASTER 500"),
        ZEISS_IOLMASTER_700("ZEISS IOLMASTER 700"),
        NIDEK_AL_SCAN("NIDEK AL-SCAN"),
        TOMEY_OA_2000("TOMEY OA 2000"),
        TOPCON_ALADDIN("TOPCON Aladdin"),
        OCULUS_PENTACAM_AXL("OCULUS PENTACAM AXL"),
        ZIEMER_GALILEI_G6("ZIEMER GALILEI G6"),
        MOVU_ALCON_ARGOS("MOVU/ALCON ARGOS"),
        HEIDELBERG_ANTERION("HEIDELBERG ANTERION"),
        MANUAL_ENCODING("MANUAL ENCODING");

        private final String value;

        Biometer(String value) {
            this.value = value;
        }

        public static Biometer getKeyFromValue(String value) {
            for (Biometer constant : values()) {
                if (constant.getValue().equals(value)) {
                    return constant;
                }
            }
            throw new IllegalArgumentException("Valeur inconnue : " + value);
        }
    }

    @Getter
    public enum ConstantType {
        NOMINAL("Nominal"),
        MANUFACTURER("Manufacturer"),
        OPTIMIZED("Optimized"),
        CUSTOM("Custom");

        private final String value;

        ConstantType(String value) {
            this.value = value;
        }

        public static ConstantType getKeyFromValue(String value) {
            for (ConstantType constant : values()) {
                if (constant.getValue().equals(value)) {
                    return constant;
                }
            }
            throw new IllegalArgumentException("Valeur inconnue : " + value);
        }
    }

    @Getter
    public enum Formula {
        HOFFERQ("HofferQ"),
        HOFFERQST("HofferQst"),
        PEARL("Pearl"),
        KANE("Kane"),
        BARRETT("Barrett"),
        EVO("Evo"),
        HOLLADAY("Holladay"),
        SRKT("Srkt"),
        HAIGIS("Haigis"),
        COOKE("Cooke");

        private final String value;

        Formula(String value) {
            this.value = value;
        }

        public static Formula getKeyFromValue(String value) {
            for (Formula formula : values()) {
                if (formula.getValue().equals(value)) {
                    return formula;
                }
            }
            throw new IllegalArgumentException("Value not found : " + value);
        }
    }

}
