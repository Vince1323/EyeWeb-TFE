package com.jorami.eyeapp.util;

import lombok.Getter;

public class LanguagesDictionary {

    @Getter
    public enum EyeSide {
        OD_ENG("RIGHT"),
        OS_ENG("LEFT"),
        OD_FRA("DROIT"),
        OS_FRA("GAUCHE");

        private final String value;

        EyeSide(String value) {
            this.value = value;
        }
    }
}
