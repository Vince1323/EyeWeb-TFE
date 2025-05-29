package com.jorami.eyeapp.model.formula;

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
public class Cooke {

    private Double KIndex;
    private int PredictionsPerIol;
    private List<IOL> IOLs;
    private List<Eye> Eyes;

    @Getter
    @Setter
    public static class IOL {
        private Double AConstant;
        private String Family;
        private List<Power> Powers;
        public IOL(Map<String, String> constantes){
            setAConstant(Double.parseDouble(constantes.get(Enum.Formula.COOKE.getValue())));
            setFamily("Other");
            setPowers(List.of(new Power()));
        }
        @Getter
        @Setter
        public static class Power {
            private Double From;
            private Double To;
            private Double By;
            public Power(){
                setFrom(0.0);
                setTo(80.0);
                setBy(0.5);
            }
        }
    }

    @Getter
    @Setter
    public static class Eye {
        private Double TgtRx;
        private Double K1;
        private Double K2;
        private String Biometer;
        private Double AL;
        private Double CCT;
        private Double ACD;
        private Double LT;
        private Double WTW;
        public Eye(Exam biometry, Calcul newCalcul) {
            setTgtRx(Double.valueOf(newCalcul.getTargetRefraction()));
            setK1(Double.valueOf(biometry.getK1()));
            setK2(Double.valueOf(biometry.getK2()));
            setBiometer(String.valueOf(biometry.getMachine()));
            setAL(Double.valueOf(biometry.getAl()));
            setCCT((biometry.getCct() != null) ? (checkRangeCCT(biometry.getCct()) ? Double.valueOf(biometry.getCct()) : null) : null);
            setACD(Double.valueOf(biometry.getAcd()));
            setLT((biometry.getLensThickness() != null) ? (checkRangeLT(biometry.getLensThickness()) ? Double.valueOf(biometry.getLensThickness()) : null) : null);
            setWTW((biometry.getWtw() != null) ? (checkRangeWTW(biometry.getWtw()) ? Double.valueOf(biometry.getWtw()) : null) : null);
        }
        public void setBiometer(String biometer) {
            if(Stream.of("Argos", "Lenstar").anyMatch((biometer::contains))) {
                this.Biometer = biometer;
            } else {
                this.Biometer = "Other";
            }
        }
        private boolean checkRangeLT(Float LT) {
            return (LT >= 2.52 && LT <= 6.47);
        }
        private boolean checkRangeCCT(Float CCT) {
            return (CCT >= 310 && CCT <= 700);
        }
        private boolean checkRangeWTW(Float WTW) {
            return (WTW >= 9.8 && WTW <= 13.5);
        }
    }

    public Cooke(Exam biometry, Map<String, String> constantes, Calcul newCalcul) {
        setKIndex(1.3375);
        setPredictionsPerIol(7);
        setIOLs(List.of(new IOL(constantes)));
        setEyes(List.of(new Eye(biometry, newCalcul)));
    }
}
