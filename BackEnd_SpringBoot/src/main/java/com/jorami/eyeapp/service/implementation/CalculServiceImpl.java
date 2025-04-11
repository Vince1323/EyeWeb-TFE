package com.jorami.eyeapp.service.implementation;

import com.jorami.eyeapp.model.*;
import com.jorami.eyeapp.model.Enum;
import com.jorami.eyeapp.model.exam.Exam;
import com.jorami.eyeapp.model.formula.Cooke;
import com.jorami.eyeapp.model.formula.Pearl;
import com.jorami.eyeapp.repository.CalculRepository;
import com.jorami.eyeapp.repository.DiopterRepository;
import com.jorami.eyeapp.service.CalculService;
import com.jorami.eyeapp.service.CataractService;
import com.jorami.eyeapp.service.FormulaApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorami.eyeapp.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.jorami.eyeapp.exception.ConstantMessage.*;


@Service
@AllArgsConstructor
public class CalculServiceImpl implements CalculService {

    private final DiopterRepository diopterRepository;
    private final CalculRepository calculRepository;
    private final FormulaApiService formulaApiService;
    private final CataractService cataractService;
    private final UserService userService;

    private static final Logger logger = Logger.getLogger(CalculServiceImpl.class);


    @Override
    public Calcul getCalculById(Long id, List<Long> organizationsId) {
        userService.validOrganizations(organizationsId);
        Calcul calcul = calculRepository.getCalculById(id, organizationsId);
        if (calcul == null) {
            throw new EntityNotFoundException(ITEM_NOT_FOUND);
        }
        return calcul;
    }

    @Override
    public List<Calcul> getAllCalculsByPatientIdAndEyeSide(Long patientId, Enum.EyeSide eyeSide, List<Long> organizationsId) {
        userService.validOrganizations(organizationsId);
        List<Calcul> calculs = new ArrayList<>();
        try {
            calculs = calculRepository.findAllCalculsByPatientIdAndEyeSide(patientId, eyeSide, organizationsId);
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage(), e);
        }
        return calculs;
    }

    @Override
    public List<Calcul> getAllCalculsByExamIdEyeSidePowerTrue(Long examId, Enum.EyeSide eyeSide, List<Long> organizationsId) {
        userService.validOrganizations(organizationsId);
        List<Calcul> calculs = new ArrayList<>();
        try {
            calculs = calculRepository.findAllCalculsByExamIdEyeSidePowerTrue(examId, eyeSide, organizationsId);
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage(), e);
        }
        return calculs;
    }

    @Override
    public void loopOnCalculs(List<Calcul> calculs, List<Long> organizationsId) {
        if(calculs == null || calculs.isEmpty()) {
            throw new NoSuchElementException(CALCUL_UNSPECIFIED);
        }
        for(final Calcul calcul : calculs) {
            Exam bio = cataractService.getExamById(calcul.getExam().getId(), organizationsId);
            if(bio.getK1() == null || bio.getK2() == null || bio.getAl() == null || bio.getAcd() == null) {
                throw new NoSuchElementException(CALCUL_FIELDS_EMPTY);
            }
            calcul.setExam(bio);
            Lens lens = calcul.getLens();

            Map<String, String> constantes = new HashMap<>();
            constantes.put("targetRefraction", String.valueOf(calcul.getTargetRefraction()));
            for(Constant constant : calcul.getConstants()) {
                // Utilise le type de constante comme clé et la valeur comme valeur dans la map
                constantes.put(String.valueOf(constant.getFormula()), constant.getValue());
            }
            try {
                injectionAndFormulaGen3(calcul, lens, constantes, organizationsId);
            } catch (InterruptedException | JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void injectionAndFormulaGen3(Calcul calcul, Lens lens, Map<String, String> constantes, List<Long> organizationsId) throws InterruptedException, JsonProcessingException {

        String examEye = calcul.getExam().getEyeSide().toString();
        List<Float> interval_powers;
        List<String> selectedSites = new ArrayList<>(constantes.keySet());

        for(String site : selectedSites){
            System.out.println(site);
        }

        Calcul newCalcul = new Calcul();

        newCalcul.setExam(calcul.getExam());
        newCalcul.setLens(lens);
        newCalcul.setTargetRefraction(calcul.getTargetRefraction());
        newCalcul.setConstants(cloneAndAssociateConstantsWithCalcul(calcul.getConstants(), newCalcul));
        newCalcul.setIdReferencePrecExam(calcul.getIdReferencePrecExam());
        newCalcul.setIdReferencePrecCalcul(calcul.getIdReferencePrecCalcul());
        newCalcul.setSe(calcul.getSe());
        newCalcul.setPrecPowerSelected(calcul.getPrecPowerSelected());
        newCalcul.setIsSecondEye(calcul.getIsSecondEye());
        if(examEye.equals("OS"))
            newCalcul.setEyeSide(Enum.EyeSide.OS);
        else
            newCalcul.setEyeSide(Enum.EyeSide.OD);

        newCalcul = calculRepository.save(newCalcul);

        List<Diopter> diopters = new ArrayList<>();
        List<Diopter> dioptersPearl = new ArrayList<>();
        List<Diopter> dioptersCooke = new ArrayList<>();
        List<Diopter> dioptersForInterval = new ArrayList<>();

        // Lance les API formules
        if(selectedSites.contains(Enum.Formula.PEARL.getValue())) {
            dioptersPearl = launchAPIPearl(calcul.getExam(), constantes, newCalcul, organizationsId);
            dioptersForInterval.addAll(dioptersPearl);
        }
        if(selectedSites.contains(Enum.Formula.COOKE.getValue())) {
            dioptersCooke = launchAPICooke(calcul.getExam(), constantes, newCalcul);
            dioptersForInterval.addAll(dioptersCooke);
        }

        if(!dioptersForInterval.isEmpty()) {
            interval_powers = getPowerIntervalApi(dioptersForInterval);
        } else {
            // interval_powers = getPowerForATargetRefraction(calcul.getBiometry(),12.0, -0.5313028450313741, "119");
            // TODO : régler la méthode getPowerForATargetRefraction pour obtenir les powers si injection ne fonctionne pas ( attendre solution FX )
            interval_powers = new ArrayList<>(Arrays.asList(10.0f, 10.5f, 11.0f, 11.5f, 12.0f, 12.5f, 13.0f, 13.5f, 14.0f, 14.5f, 15.0f, 15.5f, 16.0f, 16.5f, 17.0f, 17.5f, 18.0f));
        }

        List<Diopter> dioptersFormulaGen3 = launchThirdGenerationFormula(calcul.getExam(), interval_powers, constantes, newCalcul, selectedSites);

        diopters.addAll(dioptersFormulaGen3);
        diopters.addAll(dioptersPearl);
        diopters.addAll(dioptersCooke);
        newCalcul.setDiopters(diopters);
    }

    @Override
    public void updateSelectedDiopters(Long idCalcul, Float power, List<Long> organizationsId) {
        Calcul calcul = getCalculById(idCalcul, organizationsId);
        calcul.getDiopters().forEach(diopter -> {
            diopter.setSelected(Objects.equals(diopter.getIolPower(), power));
        });
        calculRepository.save(calcul);
    }

    private List<Diopter> launchThirdGenerationFormula(Exam biometry, List<Float> listPower, Map<String, String> constantes, Calcul newCalcul, List<String> selectedListSites) {
        Float arcDouble = 0.0f;
        Float alDouble = 0.0f;
        Float acdDouble = 0.0f;
        boolean acdIsOk = true;

        if (biometry.getR_avg() == null)
            arcDouble = calculRavg(biometry.getK1(), biometry.getK2(), 1.3375);
        else
            arcDouble = biometry.getR_avg();
        if (!(biometry.getAl() == null))
            alDouble = biometry.getAl();
        if (biometry.getAcd() == null)
            acdIsOk = false;
        else
            acdDouble = biometry.getAcd();

        double dxDouble = 12.0;
        double sf = selectedListSites.contains("Holladay") ? Double.parseDouble(constantes.get("Holladay")) : 0;
        double pAcd = selectedListSites.contains("HofferQ") ? Double.parseDouble(constantes.get("HofferQ")) : 0;
        double aConstant = selectedListSites.contains("SRKT") ? Double.parseDouble(constantes.get("SRKT")) : 0;
        String haigisString = selectedListSites.contains("Haigis") ? constantes.get("Haigis") : null;
        double a0 = 0.0;
        double a1 = 0.0;
        double a2 = 0.0;

        if (haigisString != null) {
            // Diviser la chaîne pour obtenir les différentes parties contenant a0, a1, et a2
            String[] parts = haigisString.split(";");

            // Extraire les valeurs de a0, a1, et a2
            for (String part : parts) {
                if (part.contains("a0")) {
                    a0 = Double.parseDouble(part.split("=")[1]);
                } else if (part.contains("a1")) {
                    a1 = Double.parseDouble(part.split("=")[1]);
                } else if (part.contains("a2")) {
                    a2 = Double.parseDouble(part.split("=")[1]);
                }
            }
        }

        // Liste pour stocker les Diopters
        List<Diopter> diopters = new ArrayList<>();

        for (Float power : listPower) {
            if(selectedListSites.contains("Holladay")) {
                // Calculer les résultats pour chaque formule
                Float holladayResult = calculateHolladay(alDouble, arcDouble, power, dxDouble, sf);
                diopters.add(diopterRepository.save(createDiopter(power, holladayResult, Enum.Formula.HOLLADAY, newCalcul)));
            }
            if(selectedListSites.contains("HofferQ")) {
                Float hofferQResult = calculateHofferQ(alDouble, arcDouble, power, pAcd);
                diopters.add(diopterRepository.save(createDiopter(power, hofferQResult, Enum.Formula.HOFFERQ, newCalcul)));
            }
            if(selectedListSites.contains("SRKT")) {
                Float srktResult = calculateSrkt(alDouble, arcDouble, power, dxDouble, aConstant);
                diopters.add(diopterRepository.save(createDiopter(power, srktResult, Enum.Formula.SRKT, newCalcul)));
            }
            if(acdIsOk && selectedListSites.contains("Haigis")) {
                Float haigisResult = calculateHaigis(alDouble, arcDouble, acdDouble, power, dxDouble, a0, a1, a2);
                diopters.add(diopterRepository.save(createDiopter(power, haigisResult, Enum.Formula.HAIGIS, newCalcul)));
            }
        }

        return diopters;
    }

    private List<Diopter> launchAPIPearl(Exam biometry, Map<String, String> constantes, Calcul newCalcul, List<Long> organizationsId) throws JsonProcessingException {
        List<Diopter> diopters = new ArrayList<>();
        Pearl pearl = new Pearl();
        if(newCalcul.getIsSecondEye() != null && newCalcul.getIsSecondEye()) {
            Exam precExam = this.cataractService.getExamById(newCalcul.getIdReferencePrecExam(), organizationsId);
            if (precExam == null) {
                throw new EntityNotFoundException(ITEM_NOT_FOUND);
            }
            pearl = new Pearl(biometry, constantes, newCalcul, precExam);
        } else {
            pearl = new Pearl(biometry, constantes, newCalcul);
        }
        String result = this.formulaApiService.callWithWebClient(pearl,"https://iolsolver.com/api/v1.0.0/solve/", Enum.Formula.PEARL.getValue()).block();
        if(result != null && !result.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result);
            jsonNode.fields().forEachRemaining(entry -> {
                JsonNode valueNode = entry.getValue();
                if(valueNode.has("power") && !valueNode.get("power").isNull()) {
                    Float power = Float.valueOf(valueNode.get("power").asText());
                    Float es = Float.valueOf(valueNode.get("es").asText());
                    System.out.println("power: " + power);
                    diopters.add(diopterRepository.save(createDiopter(power, es, Enum.Formula.PEARL, newCalcul)));
                }
            });
        }
        return diopters;
    }

    private List<Diopter> launchAPICooke(Exam biometry, Map<String, String> constantes, Calcul newCalcul) throws JsonProcessingException {
        List<Diopter> diopters = new ArrayList<>();
        Cooke cooke = new Cooke(biometry, constantes, newCalcul);
        String result = this.formulaApiService.callWithWebClient(cooke,"https://cookeformula.com/api/v1/k6/v2024.01/preop", Enum.Formula.COOKE.getValue()).block();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result);

        for (JsonNode iolNode : jsonNode) {
            JsonNode iols = iolNode.get("IOLs");
            if (iols != null && iols.isArray()) {
                for (JsonNode iol : iols) {
                    JsonNode predictions = iol.get("Predictions");
                    if (predictions != null && predictions.isArray()) {
                        for (JsonNode prediction : predictions) {
                            if (prediction.has("IOL") && prediction.has("Rx")) {
                                Float power = Float.valueOf(prediction.get("IOL").asText());
                                Float value = Float.valueOf(prediction.get("Rx").asText());

                                System.out.println("(power): " + power + " (value): " + value);
                                diopters.add(diopterRepository.save(createDiopter(power, value, Enum.Formula.COOKE, newCalcul)));
                            }
                        }
                    }
                }
            }
        }

        return diopters;
    }

    private List<Float> getPowerIntervalApi(List<Diopter> diopters) {
        List<Float> intervals;
        intervals = diopters.stream()
                .map(Diopter::getIolPower)
                .collect(Collectors.toList());
        return intervals;
    }

    // Méthode auxiliaire pour créer un Diopter à partir des résultats de calcul
    private Diopter createDiopter(Float power, Float result, Enum.Formula formula, Calcul newCalcul) {
        Diopter diopter = new Diopter();
        diopter.setIolPower(power);
        diopter.setValue(result);
        diopter.setFormula(formula.getValue());
        diopter.setCalcul(newCalcul);

        return diopter;
    }

    // Méthodes calculées en interne
    private Float calculateHolladay(double AL, double ARC, double Power, double dx, double SF) {
        // IOL CONSTANTS pour Holladay I
        double KeratometricIndexHolladay = 1.3333333;
        double naholladay = 1.336;
        double RTholladay = 0.2;

        double Rholladay = (ARC < 7) ? 7 : ARC;
        double AG1 = 12.5 * AL / 23.45;
        double AG = (AG1 > 13.5) ? 13.5 : AG1;
        double ACDholladay = 0.56 + Rholladay - (Math.sqrt((Rholladay * Rholladay) - ((AG * AG / 4))));
        double ALmHolladay = AL + RTholladay;
        double calc1holladay = (1000 * naholladay * (naholladay * ARC - (KeratometricIndexHolladay - 1) * ALmHolladay) - (Power * (ALmHolladay - ACDholladay - SF) * (naholladay * ARC - (KeratometricIndexHolladay - 1) * (ACDholladay + SF))));
        double calc2holladay = (naholladay * (dx * (naholladay * ARC - ((KeratometricIndexHolladay - 1) * ALmHolladay)) + ALmHolladay * ARC) - 0.001 * Power * (ALmHolladay - ACDholladay - SF) * (dx * (naholladay * ARC - (KeratometricIndexHolladay - 1) * (ACDholladay + SF)) + ((ACDholladay + SF) * ARC)));
        double holladay = calc1holladay / calc2holladay;

        return (float) holladay;
    }

    private Float calculateHofferQ(double AL, double ARC, double Power, double pACD) {
        // IOL CONSTANTS pour Hoffer I
        double KeratometricIndexHoffer = 1.3375;
        double dxhofferQ = (double) 12 / 1000; // dx divisé 1000

        double KhofferQ = ((KeratometricIndexHoffer - 1) * 1000 / ARC);
        double MhofferQ = AL <= 23 ? 1 : -1;
        double GhofferQ = AL <= 23 ? 28 : 23.5;
        double AhofferQ = AL > 31 ? 31 : AL < 18.5 ? 18.5 : AL;
        double p2hofferQ = 0.3 * (AhofferQ - 23.5);
        double p3hofferQ = Math.pow(Math.tan(KhofferQ * (Math.PI / 180)), 2);
        double p4hofferQ = 0.1 * MhofferQ * (Math.pow((23.5 - AhofferQ), 2));
        double p5hofferQ = Math.tan((0.1 * Math.pow((GhofferQ - AhofferQ), 2)) * (Math.PI / 180));
        double ACDhofferQ = pACD + p2hofferQ + p3hofferQ + (p4hofferQ * p5hofferQ) - 0.99166;
        double calc1hofferQ = (1.336 / (1.336 / (1336 / (AL - ACDhofferQ - 0.05) - Power) + (ACDhofferQ + 0.05) / 1000)) - KhofferQ;
        double hofferQ = calc1hofferQ / (1 + dxhofferQ * calc1hofferQ);

        return (float) hofferQ;
    }

    private Float calculateSrkt(double AL, double ARC, double Power, double dx, double aConstant) {
        // IOL CONSTANTS pour Srkt I
        double KeratometricIndexSRKT = 1.3375;
        double naSRKT = 1.336;
        double ncmlSRKT = 0.333;
        double ACDcstSRKT = 0.62467 * aConstant - 68.747;
        double offsetsrkt = ACDcstSRKT - 3.336;

        double Ksrkt = ((KeratometricIndexSRKT - 1) * 1000 / ARC);
        double LcorSRKT = AL <= 24.2 ? AL : (-3.446 + (1.715 * AL) - (0.0237 * AL * AL));
        double cwsrkt = (-5.40948 + (0.58412 * LcorSRKT) + (0.098 * Ksrkt));
        double xsrkt = ((ARC * ARC) - (cwsrkt * cwsrkt * 0.25));
        double x2srkt = xsrkt < 0 ? 0 : xsrkt;
        double Hsrkt = ARC - Math.sqrt(x2srkt);
        double ACDestsrkt = Hsrkt + offsetsrkt;
        double rethicksrkt = 0.65696 - 0.02029 * AL;
        double Loptsrkt = AL + rethicksrkt;
        double calc1srkt = 1000 * naSRKT * (naSRKT * ARC - ncmlSRKT * Loptsrkt) - Power * (Loptsrkt - ACDestsrkt) * (naSRKT * ARC - ncmlSRKT * ACDestsrkt);
        double calc2srkt = naSRKT * (dx * (naSRKT * ARC - ncmlSRKT * Loptsrkt) + Loptsrkt * ARC) - 0.001 * Power * (Loptsrkt - ACDestsrkt) * (dx * (naSRKT * ARC - ncmlSRKT * ACDestsrkt) + ACDestsrkt * ARC);
        double srkt = calc1srkt / calc2srkt;

        return (float) srkt;
    }

    private Float calculateHaigis(double AL, double ARC, double ACD, double Power, double dx, double a0, double a1, double a2) {
        // IOL CONSTANTS pour Haigis I
        double KeratometricIndexHaigis = 1.3315;
        double nvithaigis = 1.336;
        double dxhaigis = dx / 1000;

        double Khaigis = ((KeratometricIndexHaigis - 1) * 1000) / ARC;
        double ELPhaigis = a0 + a1 * ACD + a2 * AL;
        double calc1haigis = nvithaigis * (nvithaigis - Power * ((AL / 1000) - (ELPhaigis / 1000)));
        double calc2haigis = nvithaigis * ((AL / 1000) - (ELPhaigis / 1000)) + (ELPhaigis / 1000) * (nvithaigis - Power * ((AL / 1000) - (ELPhaigis / 1000)));
        double calc3haigis = calc1haigis / calc2haigis;
        double haigis = (calc3haigis - Khaigis) / (1 + dxhaigis * (calc3haigis - Khaigis));

        return (float) haigis;
    }

    private List<Constant> cloneAndAssociateConstantsWithCalcul(List<Constant> originalConstants, Calcul newCalcul) {
        List<Constant> newConstants = new ArrayList<>();
        for (Constant oldConstant : originalConstants) {
            Constant newConstant = new Constant();
            newConstant.setConstantType(oldConstant.getConstantType());
            newConstant.setValue(oldConstant.getValue());
            newConstant.setFormula(oldConstant.getFormula());

            newConstant.setCalcul(newCalcul);
            newConstants.add(newConstant);
        }
        return newConstants;
    }

    public static Float calculRavg(Float k1, Float k2, Double n) {
        // Calcul de la moyenne de k1 et k2
        double Km = (k1 + k2) / 2;

        // Calcul de Ravg selon la formule fournie

        return (float) ((1000 * (n - 1)) / Km);
    }

}