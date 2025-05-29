package com.jorami.eyeapp.service.implementation;

import com.jorami.eyeapp.model.*;
import com.jorami.eyeapp.model.Enum;
import com.jorami.eyeapp.model.exam.Exam;
import com.jorami.eyeapp.model.exam.ExamForUpdate;
import com.jorami.eyeapp.model.patient.Patient;
import com.jorami.eyeapp.repository.CataractRepository;
import com.jorami.eyeapp.repository.OrganizationRepository;
import com.jorami.eyeapp.service.CataractService;
import com.jorami.eyeapp.service.PatientService;
import com.jorami.eyeapp.service.UserService;
import com.jorami.eyeapp.util.*;
import com.jorami.eyeapp.util.mapper.ExamMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static com.jorami.eyeapp.exception.ConstantMessage.*;
import static com.jorami.eyeapp.util.Utils.*;

@Service
@AllArgsConstructor
public class CataractServiceImpl implements CataractService {

    private final CataractRepository cataractRepository;
    private final PatientService patientService;
    private final OrganizationRepository organizationRepository;
    private final UserService userService;
    private final ExamMapper mapper;
    private final Map<String, Sinks.Many<String>> userLogSinks = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(CataractServiceImpl.class);


    @Override
    public Exam getExamById(Long examId, List<Long> organizationsId) {
        userService.validOrganizations(organizationsId);
        return cataractRepository.findExamById(examId, organizationsId).orElseThrow(() -> new NoSuchElementException(ITEM_NOT_FOUND));
    }

    @Override
    public List<Exam> getAllBiometriesByPatientIdAndEyeSide(Long patientId, Enum.EyeSide eyeSide, Boolean isSelected, List<Long> organizationsId) {
        userService.validOrganizations(organizationsId);
        try {
            return cataractRepository.findAllBiometriesByPatientIdAndEyeSide(patientId, eyeSide, isSelected, organizationsId);
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage(), e);
            throw new NoSuchElementException(LIST_ITEM_NOT_FOUND);
        }
    }

    @Override
    public List<Exam> getAllBiometriesByPatientIdEyeSideCalculPowerTrue(Long patientId, Enum.EyeSide eyeSide, Boolean isSelected, List<Long> organizationsId) {
        userService.validOrganizations(organizationsId);
        try {
            return cataractRepository.findAllBiometriesByPatientIdEyeSideCalculPowerTrue(patientId, eyeSide, isSelected, organizationsId);
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage(), e);
            throw new NoSuchElementException(LIST_ITEM_NOT_FOUND);
        }
    }

    @Override
    public void checkIfExist(Exam exam, Long idPatient, List<Long> organizationsId) {
        userService.validOrganizations(organizationsId);
        if(!Objects.equals(exam.getImportType(), "Average") && cataractRepository.findExamByPatientIdAndExamDateAndEyeSideAndMachine(idPatient, exam.getExamDate(), exam.getEyeSide(), exam.getMachine(), organizationsId) != null) {
            throw new NoSuchElementException(ITEM_ALREADY_EXISTS);
        }
    }

    @Override
    public Exam addExam(Exam exam, Long idPatient, List<Long> organizationsId) {
        userService.validOrganizations(organizationsId);
        Patient patient = patientService.getPatientById(organizationsId, idPatient);
        if(exam.getAl() == null || exam.getAcd() == null || exam.getK1() == null || exam.getK2() == null) {
            exam.setSelected(false);
        }
        if(exam.getExamDate() == null) {
            exam.setExamDate(exam.getCalculDate());
        }
        exam.setLinks(new ArrayList<>());
        for (Long orgId : organizationsId) {
            Organization organization = organizationRepository.findById(orgId)
                    .orElseThrow(() -> new RuntimeException(ORGANIZATION_NOT_FOUND));
            Link link = new Link();
            link.setPatient(patient);
            link.setOrganization(organization);
            link.setExam(exam);
            exam.addLink(link);
        }
        return cataractRepository.saveAndFlush(exam);
    }

    @Override
    public void loopAddExams(List<Exam> exams, Long patientId, List<Long> organizationsId) {
        exams.forEach(e -> {
            this.checkIfExist(e, patientId, organizationsId);
            this.addExam(e, patientId, organizationsId);
        });
    }

    @Override
    public void importBiometries(String tokenUser, MultipartFile file, String biometer, List<Long> organizationsId) throws IOException {
        userService.validOrganizations(organizationsId);
        if(file == null || file.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_FILE);
        } else if(biometer == null || biometer.isEmpty()) {
            throw new IllegalArgumentException(BIOMETER_UNSPECIFIED);
        } else if(!List.of(
                Enum.Biometer.OCULUS_PENTACAM_AXL.getValue(),
                Enum.Biometer.ZEISS_IOLMASTER_500.getValue(),
                Enum.Biometer.ZEISS_IOLMASTER_700.getValue()).contains(biometer)) {
            throw new IllegalArgumentException(BIOMETER_NOT_IMPLEMENTED);
        }
        try {
            //Récupère les données du fichier sous forme de liste, en fonction du format du fichier.
            List<List<String>> fileDatas = Utils.getFileDatas(file);
            //Détermine le sélecteur à utiliser afin de récupérer les données en fonction des colonnes.
            SelectorConfig selectorConfig = Utils.loadSelectorConfig("BiometryImportationPatterns", SelectorConfig.class);
            Map<String, String> selector = getSelector(Enum.Biometer.getKeyFromValue(biometer), selectorConfig);

            String extensionFile = FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase();

            if(biometer.equals(Enum.Biometer.OCULUS_PENTACAM_AXL.getValue())) {
                //Fichier contenant une biométrie pour un seul œil par ligne.
                createOneBiometryPerLine(tokenUser, fileDatas, Enum.Biometer.getKeyFromValue(biometer), organizationsId, selector, extensionFile);
            } else if(biometer.equals(Enum.Biometer.ZEISS_IOLMASTER_500.getValue()) || biometer.equals(Enum.Biometer.ZEISS_IOLMASTER_700.getValue())) {
                //Fichier contenant des biométries pour les deux yeux par ligne.
                createTwoBiometriesPerLine(tokenUser, fileDatas, Enum.Biometer.getKeyFromValue(biometer), organizationsId, selector, extensionFile);
            }
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(ITEM_ALREADY_EXISTS);
        } catch (RuntimeException | IOException e) {
            throw new IOException(IMPORT_ERROR);
        }
        logger.info("Import biometries completed.");
    }

    @Override
    public Exam editSelectedExam(Exam exam, List<Long> organizationsId) {
        Exam examSource = getExamById(exam.getId(), organizationsId);
        examSource.setSelected(exam.isSelected());
        try {
            return cataractRepository.saveAndFlush(examSource);
        } catch (OptimisticLockingFailureException e) {
            logger.error(e.getMessage(), e);
            throw new OptimisticLockingFailureException(e.getMessage());
        }
    }

    @Override
    public Exam editExam(Exam exam, List<Long> organizationsId) {
        Exam examSource = getExamById(exam.getId(), organizationsId);
        try {
            BeanUtils.copyProperties(exam, examSource, Utils.getNullPropertyNames(exam));
            ExamForUpdate examUpdate = mapper.toExamForUpdate(exam);
            Utils.updateNullProperties(examUpdate, examSource);
            return cataractRepository.saveAndFlush(examSource);
        } catch (OptimisticLockingFailureException e) {
            logger.error(e.getMessage(), e);
            throw new OptimisticLockingFailureException(e.getMessage());
        }
    }

    @Override
    public void deleteExam(Long examId, List<Long> organizationsId) {
        try {
            Exam examForDelete = getExamById(examId, organizationsId);
            examForDelete.setDeleted(true);
            cataractRepository.save(examForDelete);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(ERROR_DELETE);
        }
    }

    @Override
    public Exam calculAverage(Long idPatient, List<Exam> biometries, List<Long> organizationsId) {
        userService.validOrganizations(organizationsId);
        patientService.getPatientById(organizationsId, idPatient);
        if(biometries.isEmpty()) {
            throw new IllegalArgumentException();
        }
        float M = 0, JO = 0, J45 = 0, dAl = 0, dWtw = 0, dAcd = 0, dPachyCenter = 0, dPachyMin = 0, dPupilDia = 0, dAsphQF = 0,
                dAsphQB = 0, dLens = 0, dAlpha = 0, dNewK1 = 0, dNewK2 = 0, dNewK1Cornea = 0, dNewK2Cornea= 0, dNewAlpha = 0,
                dKAsti = 0, dKAvg = 0, dRAvg = 0;
        float[] tabK1 = new float[biometries.size()];
        float[] tabK2 = new float[biometries.size()];
        float[] tabK1Axis = new float[biometries.size()];
        float[] al = new float[biometries.size()];
        float[] wtw = new float[biometries.size()];
        float[] acd = new float[biometries.size()];
        float[] pachyCenter = new float[biometries.size()];
        float[] pachyMin = new float[biometries.size()];
        float[] pupilDia = new float[biometries.size()];
        float[] asphQF = new float[biometries.size()];
        float[] asphQB = new float[biometries.size()];
        float[] tabK1Cornea = new float[biometries.size()];
        float[] tabK2Cornea = new float[biometries.size()];
        float[] tabK1AxisCornea = new float[biometries.size()];
        float[] tabKAstig = new float[biometries.size()];
        float[] tabKAvg = new float[biometries.size()];
        float[] tabRAvg = new float[biometries.size()];
        int nbrLignesSelected = biometries.size();
        Enum.EyeSide eyeSide = biometries.get(0).getEyeSide();
        String biometer = "";

        for(int i = 0; i < nbrLignesSelected; i++) { //Boucle autant que le nombre de lignes sélectionnées
            Exam biometry = biometries.get(i);
            biometer = String.valueOf(biometry.getMachine());

            if(biometry.getK1() != null)
                tabK1[i] = biometry.getK1();
            if(biometry.getK2() != null)
                tabK2[i] = biometry.getK2();
            if(biometry.getK1Axis() != null)
                tabK1Axis[i] = biometry.getK1Axis();
            if(biometry.getAl() != null)
                al[i] = biometry.getAl();
            if(biometry.getWtw() != null)
                wtw[i] = biometry.getWtw();
            if(biometry.getAcd() != null)
                acd[i] = biometry.getAcd();
            if(biometry.getCct() != null)
                pachyCenter[i] = biometry.getCct();
            if(biometry.getCctMin() != null)
                pachyMin[i] = biometry.getCctMin();
            if(biometry.getPupilDia() != null)
                pupilDia[i] = biometry.getPupilDia();
            if(biometry.getAsphQf() != null)
                asphQF[i] = biometry.getAsphQf();
            if(biometry.getAsphQb() != null)
                asphQB[i] = biometry.getAsphQb();
            if(biometry.getK1CorneaBack() != null)
                tabK1Cornea[i] = biometry.getK1CorneaBack();
            if(biometry.getK2CorneaBack() != null)
                tabK2Cornea[i] = biometry.getK2CorneaBack();
            if(biometry.getK1AxisCorneaBack() != null)
                tabK1AxisCornea[i] = biometry.getK1AxisCorneaBack();
            if(biometry.getK_astig() != null)
                tabKAstig[i] = biometry.getK_astig();
            if(biometry.getK_avg() != null)
                tabKAvg[i] = biometry.getK_avg();
            if(biometry.getR_avg() != null)
                tabRAvg[i] = biometry.getR_avg();
        }

        //Moyennes K1,K2,K1Axis à l'aide de M/JO/J45
        M = AverageUtils.calculM(tabK1, tabK2, nbrLignesSelected);
        JO = AverageUtils.calculJO(tabK1, tabK2, tabK1Axis, nbrLignesSelected);
        J45 = AverageUtils.calculJ45(tabK1, tabK2, tabK1Axis, nbrLignesSelected);
        //Transformation de la valeur moyene M, J0, J45 en valeur moyenne K1, K2, Axis
        //Alpha = Axis
        dAlpha = AverageUtils.calculAlpha(J45, JO);
        //Si on une seule biométrie, les valeurs de newK1 et newK2 devraient correspondre à K1 et K2
        dNewK1 = AverageUtils.calculNewK1(M, JO, dAlpha);
        dNewK2 = AverageUtils.calculNewK2(M, JO, dAlpha);

        //Moyennes K1 cornea,K2 cornea,K1Axis cornea à l'aide de M/JO/J45
        M = AverageUtils.calculM(tabK1Cornea, tabK2Cornea, nbrLignesSelected);
        JO = AverageUtils.calculJO(tabK1Cornea, tabK2Cornea, tabK1AxisCornea, nbrLignesSelected);
        J45 = AverageUtils.calculJ45(tabK1Cornea, tabK2Cornea, tabK1AxisCornea, nbrLignesSelected);
        dNewAlpha = AverageUtils.calculAlpha(J45, JO);
        dNewK1Cornea = AverageUtils.calculNewK1(M, JO, dNewAlpha);
        dNewK2Cornea = AverageUtils.calculNewK2(M, JO, dNewAlpha);

        // Moyennes arithmétiques
        dAl = AverageUtils.calculMoyenneArithmetique(al, nbrLignesSelected);
        dWtw = AverageUtils.calculMoyenneArithmetique(wtw, nbrLignesSelected);
        dAcd = AverageUtils.calculMoyenneArithmetique(acd, nbrLignesSelected);
        dPachyCenter = AverageUtils.calculMoyenneArithmetique(pachyCenter, nbrLignesSelected);
        dPachyMin = AverageUtils.calculMoyenneArithmetique(pachyMin, nbrLignesSelected);
        dPupilDia = AverageUtils.calculMoyenneArithmetique(pupilDia, nbrLignesSelected);
        dAsphQF = AverageUtils.calculMoyenneArithmetique(asphQF, nbrLignesSelected);
        dAsphQB = AverageUtils.calculMoyenneArithmetique(asphQB, nbrLignesSelected);
        dKAsti = AverageUtils.calculMoyenneArithmetique(tabKAstig, nbrLignesSelected);
        //Doit être égal à M (le premier calcul avec K1 et K2 et non pas K1Cornea etc)!
        dKAvg = AverageUtils.calculMoyenneArithmetique(tabKAvg, nbrLignesSelected);
        dRAvg = AverageUtils.calculMoyenneArithmetique(tabRAvg, nbrLignesSelected);
        float k2Axis = Utils.calculK2Axis((float) dAlpha);

        Exam biometry = Exam.builder()
                .eyeSide(eyeSide)
                .examDate(LocalDateTime.now())
                .examQuality("Moyenne")
                .calculDate(LocalDateTime.now())
                .eyeStatus("OK")
                .selected(false)
                .al(dAl)
                .acd(dAcd)
                .pupilDia(dPupilDia)
                .wtw(dWtw)
                .k1(dNewK1)
                .k2(dNewK2)
                .k2Axis(k2Axis)
                .k_astig(dKAsti)
                .k_avg(dKAvg)
                .r_avg(dRAvg)
                .k1CorneaBack(dNewK1Cornea)
                .k2CorneaBack(dNewK2Cornea)
                .cct(dPachyCenter)
                .cctMin(dPachyMin)
                .asphQf(dAsphQF)
                .asphQb(dAsphQB)
                .machine(Enum.Biometer.valueOf(biometer))
                .importType("Average")
                .build();
        return biometry;
    }

    @Override
    public Flux<String> streamLogs(String tokenUser) {
        Sinks.Many<String> userSink = userLogSinks.computeIfAbsent(tokenUser,
                id -> Sinks.many().multicast().onBackpressureBuffer());
        return userSink.asFlux()
                .doFinally(signal -> userLogSinks.remove(tokenUser))
                .doOnError(error -> System.err.println("Error flux : " + error));
    }

    //PRIVATES METHODS
    /**
     * Retourne une map de sélecteurs basée sur le type de biométrie et la configuration de sélecteur fournis.
     *
     * @param biometer Le type de biométrie pour lequel récupérer les sélecteurs.
     * @param selectorConfig L'objet de configuration contenant les différentes maps de sélecteurs.
     * @return Une map de sélecteurs spécifiques au type de biométrie.
     *
     * @throws IllegalArgumentException Si le type de biométrie n'est pas supporté ou si la configuration de sélecteur est nulle.
     */
    private static Map<String, String> getSelector(Enum.Biometer biometer, SelectorConfig selectorConfig) {
        Map<String, String> selector;
        switch(biometer) {
            case OCULUS_PENTACAM_AXL -> selector = selectorConfig.getPentacamBiometryImportationSelector();
            case ZEISS_IOLMASTER_500, ZEISS_IOLMASTER_700 -> selector = selectorConfig.getIolMasterBiometryImportationSelector();
            default -> throw new IllegalArgumentException("Unsupported biometer type: " + biometer);
        }
        return selector;
    }

    /**
     * Détermine si le fichier contient le titre de la colonne spécifié.
     *
     * @param selector Le sélecteur à utiliser.
     * @param selectorKey La clé du sélecteur à utiliser pour rechercher le titre de la colonne.
     * @param columnTitle Le titre de la colonne à rechercher.
     * @return True si le fichier contient le titre de la colonne, false sinon.
     */
    private static boolean fileContainsTitle(Map<String, String> selector, String selectorKey, String columnTitle) {
        if(selector.containsKey(selectorKey)) {
            return Pattern.compile(selector.get(selectorKey)).matcher(columnTitle).find();
        }
        return false;
    }

    private void createOneBiometryPerLine(String tokenUser, List<List<String>> fileDatas, Enum.Biometer biometer, List<Long> organizationsId, Map<String, String> selector, String extensionFile) {
        try {
            String calculDate;
            String calcDate = "";
            String calcTime = "";
            String examDate = "";
            String examTime = "";

            //Parcoure chaque ligne du fichier (sous forme de liste) afin de créer des objets Biometry
            for (int i = 1; i < fileDatas.size(); i++) {
                Exam biometry = new Exam();
                biometry.setMachine(biometer);
                biometry.setImportType(extensionFile);
                Patient patient = new Patient();

                for (int j = 0; j < 60; j++) {
                    String columnTitle = fileDatas.get(0).get(j).toLowerCase();
                    String value = fileDatas.get(i).get(j);
                    if (!value.isEmpty() && !value.equals("/") && !value.equals("?") && !value.startsWith("-")) {
                        if (fileContainsTitle(selector, "lastname", columnTitle)) {
                            patient.setLastname(value);
                        } else if (fileContainsTitle(selector, "firstname", columnTitle)) {
                            patient.setFirstname(value);
                        } else if (fileContainsTitle(selector, "birthDate", columnTitle)) {
                            patient.setBirthDate(DateFunction.parseDateLocalDate(value));
                        } else if (fileContainsTitle(selector, "examEye", columnTitle)) {
                            if (value.equalsIgnoreCase(LanguagesDictionary.EyeSide.OD_ENG.getValue()) || value.equalsIgnoreCase(LanguagesDictionary.EyeSide.OD_FRA.getValue())) {
                                biometry.setEyeSide(Enum.EyeSide.OD);
                            } else if (value.equalsIgnoreCase(LanguagesDictionary.EyeSide.OS_ENG.getValue()) || value.equalsIgnoreCase(LanguagesDictionary.EyeSide.OS_FRA.getValue())) {
                                biometry.setEyeSide(Enum.EyeSide.OS);
                            }
                        } else if (fileContainsTitle(selector, "examType", columnTitle)) {
                            biometry.setExamType(value);
                        } else if (fileContainsTitle(selector, "eyeStatus", columnTitle)) {
                            biometry.setEyeStatus(value);
                        } else if (fileContainsTitle(selector, "examDate", columnTitle)) {
                            examDate = DateFunction.parseDateLocalDate(value).toString();
                        } else if (fileContainsTitle(selector, "examTime", columnTitle)) {
                            examTime = value;
                        } else if (fileContainsTitle(selector, "examComment", columnTitle)) {
                            biometry.setExamComment(value);
                        } else if (fileContainsTitle(selector, "examQuality", columnTitle)) {
                            biometry.setExamQuality(value);
                        } else if (fileContainsTitle(selector, "calcDate", columnTitle)) {
                            calcDate = DateFunction.parseDateLocalDate(value).toString();
                        } else if (fileContainsTitle(selector, "calcTime", columnTitle)) {
                            calcTime = value;
                        } else if (fileContainsTitle(selector, "al", columnTitle)) {
                            biometry.setAl(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "acd", columnTitle)) {
                            biometry.setAcd(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "pupilDia", columnTitle)) {
                            biometry.setPupilDia(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "wtw", columnTitle)) {
                            biometry.setWtw(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "cord", columnTitle)) {
                            biometry.setCord(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "z40", columnTitle)) {
                            biometry.setZ40(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "hoa", columnTitle)) {
                            biometry.setHoa(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "n", columnTitle)) {
                            biometry.setN(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "k1", columnTitle)) {
                            biometry.setK1(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "K1Mm", columnTitle)) {
                            biometry.setK1Mm(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "k1Axis", columnTitle)) {
                            biometry.setK1Axis(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "k2", columnTitle)) {
                            biometry.setK2(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "K2Mm", columnTitle)) {
                            biometry.setK2Mm(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "kAstig", columnTitle)) {
                            biometry.setK_astig(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "kAstigAxis", columnTitle)) {
                            biometry.setK_astigAxis(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "kAvg", columnTitle)) {
                            biometry.setK_avg(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "rAvg", columnTitle)) {
                            biometry.setR_avg(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "k1CorneaBack", columnTitle)) {
                            biometry.setK1CorneaBack(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "k2CorneaBack", columnTitle)) {
                            biometry.setK2CorneaBack(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "k1AxisCorneaBack", columnTitle)) {
                            biometry.setK1AxisCorneaBack(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "siaCyl", columnTitle)) {
                            biometry.setSiaCyl(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "siaAxis", columnTitle)) {
                            biometry.setSiaAxis(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "snr", columnTitle)) {
                            biometry.setSnr(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "lensThickness", columnTitle)) {
                            biometry.setLensThickness(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "cct", columnTitle)) {
                            biometry.setCct(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "cctMin", columnTitle)) {
                            biometry.setCctMin(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "asphQf", columnTitle)) {
                            biometry.setAsphQf(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "asphQb", columnTitle)) {
                            biometry.setAsphQb(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "alStatus", columnTitle)) {
                            biometry.setAlStatus(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "alError", columnTitle)) {
                            biometry.setAlError(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "kPreRefrAvg", columnTitle)) {
                            biometry.setK_preRefrAvg(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "rPreRefrAvg", columnTitle)) {
                            biometry.setR_preRefrAvg(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "manifestRefrDate", columnTitle)) {
                            biometry.setManifestRefrDate(DateFunction.parseDateLocalDateTime(value));
                        } else if (fileContainsTitle(selector, "manifestRefrSph", columnTitle)) {
                            biometry.setManifestRefrSph(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "manifestRefrCyl", columnTitle)) {
                            biometry.setManifestRefrCyl(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "manifestRefrAxis", columnTitle)) {
                            biometry.setManifestRefrAxis(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "manifestRefrVd", columnTitle)) {
                            biometry.setManifestRefrVd(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "targetRefrSph", columnTitle)) {
                            biometry.setTargetRefrSph(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "targetRefrCyl", columnTitle)) {
                            biometry.setTargetRefrCyl(formatToFloat(value));
                        }
                    }
                }
                if (!(patient.getFirstname() == null || patient.getFirstname().isEmpty()) && !(patient.getLastname() == null ||
                        patient.getLastname().isEmpty()) && !(patient.getBirthDate() == null)) {
                    patient = patientService.addPatient(patient, organizationsId);

                    calculDate = calcDate + " " + calcTime;
                    biometry.setCalculDate(DateFunction.parseDateLocalDateTime(calculDate));
                    examDate = examDate + " " + examTime;
                    biometry.setExamDate(DateFunction.parseDateLocalDateTime(examDate));

                    biometry.setK2Axis(Utils.calculK2Axis(biometry.getK1Axis()));
                    if(biometry.getExamDate() == null) {
                        String log = "ERROR: Biometry " + i + "/" + (fileDatas.size() - 1) + " name: " + patient.getFullName() + " error date format.";
                        logger.info(log);
                        sendLog(tokenUser, log);
                    } else {
                        if (cataractRepository.findExamByPatientIdAndExamDateAndEyeSideAndMachine(patient.getId(), biometry.getExamDate(), biometry.getEyeSide(), biometry.getMachine(), organizationsId) == null) {
                            addExam(biometry, patient.getId(), organizationsId);
                            String log = "Info: Biometry " + i + "/" + (fileDatas.size() - 1) + " imported.";
                            logger.info(log);
                            sendLog(tokenUser, log);
                        } else {
                            String log = "Info: Biometry " + i + "/" + (fileDatas.size() - 1) + " already exists.";
                            logger.info(log);
                            sendLog(tokenUser, log);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error while importing biometries.");
        }
    }

    private void createTwoBiometriesPerLine(String tokenUser, List<List<String>> fileDatas, Enum.Biometer biometer, List<Long> organizationsId, Map<String, String> selector, String extensionFile) {
        try {
            String dateTime;
            String date = "";
            String time = "";

            for (int i = 1; i < fileDatas.size(); i++) {
                Exam biometryOD = new Exam();
                biometryOD.setMachine(biometer);
                biometryOD.setEyeSide(Enum.EyeSide.OD);
                biometryOD.setImportType(extensionFile);
                Exam biometryOS = new Exam();
                biometryOS.setMachine(biometer);
                biometryOS.setEyeSide(Enum.EyeSide.OS);
                biometryOS.setImportType(extensionFile);
                Patient patient = new Patient();
                boolean k1ODWasJustBefore = false;
                boolean k1OSWasJustBefore = false;

                for (int j = 0; j < fileDatas.get(i).size(); j++) {
                    String columnTitle = fileDatas.get(0).get(j).toLowerCase();
                    String value = fileDatas.get(i).get(j);
                    if (!value.isEmpty() && !value.equals("/") && !value.equals("?") && !value.startsWith("-")) {
                        if (fileContainsTitle(selector, "lastname", columnTitle)) {
                            patient.setLastname(value);
                        } else if (fileContainsTitle(selector, "firstname", columnTitle)) {
                            patient.setFirstname(value);
                        } else if (fileContainsTitle(selector, "birthDate", columnTitle)) {
                            patient.setBirthDate(DateFunction.parseDateLocalDate(value));
                        } else if (fileContainsTitle(selector, "examDate", columnTitle)) {
                            date = DateFunction.parseDateLocalDate(value).toString();
                        } else if (fileContainsTitle(selector, "examTime", columnTitle)) {
                            time = value;
                        } else if (fileContainsTitle(selector, "alOD", columnTitle)) {
                            biometryOD.setAl(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "alOS", columnTitle)) {
                            biometryOS.setAl(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "acdOD", columnTitle)) {
                            biometryOD.setAcd(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "acdOS", columnTitle)) {
                            biometryOS.setAcd(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "wtwOD", columnTitle)) {
                            biometryOD.setWtw(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "wtwOS", columnTitle)) {
                            biometryOS.setWtw(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "k1OD", columnTitle)) {
                            biometryOD.setK1(formatToFloat(value));
                            k1ODWasJustBefore = true;
                        } else if (fileContainsTitle(selector, "k1OS", columnTitle)) {
                            biometryOS.setK1(formatToFloat(value));
                            k1OSWasJustBefore = true;
                        } else if (fileContainsTitle(selector, "k1AxisOD", columnTitle) && k1ODWasJustBefore) {
                            biometryOD.setK1Axis(formatToFloat(value));
                            k1ODWasJustBefore = false;
                        } else if (fileContainsTitle(selector, "k1AxisOS", columnTitle) && k1OSWasJustBefore) {
                            biometryOS.setK1Axis(formatToFloat(value));
                            k1OSWasJustBefore = false;
                        } else if (fileContainsTitle(selector, "k2OD", columnTitle) && k1ODWasJustBefore) {
                            biometryOD.setK2(formatToFloat(value));
                        } else if (fileContainsTitle(selector, "k2OS", columnTitle) && k1OSWasJustBefore) {
                            biometryOS.setK2(formatToFloat(value));
                        }
                    }
                }
                if (!(patient.getFirstname() == null || patient.getFirstname().isEmpty()) && !(patient.getLastname() == null || patient.getLastname().isEmpty()) && !(patient.getBirthDate() == null)) {
                    patient = patientService.addPatient(patient, organizationsId);

                    biometryOD.setK2Axis(Utils.calculK2Axis(biometryOD.getK1Axis()));
                    biometryOS.setK2Axis(Utils.calculK2Axis(biometryOS.getK1Axis()));

                    biometryOD.setK_avg(Utils.calculKAvg(biometryOD.getK1(), biometryOD.getK2()));
                    biometryOS.setK_avg(Utils.calculKAvg(biometryOS.getK1(), biometryOS.getK2()));

                    biometryOD.setK_astig(Utils.calculKAstig(biometryOD.getK1(), biometryOD.getK2()));
                    biometryOS.setK_astig(Utils.calculKAstig(biometryOS.getK1(), biometryOS.getK2()));

                    if (time.isEmpty()) {
                        //Temps identique pour toutes les bio n'ayant pas d'heure d'examen ou de calcul
                        dateTime = date + " 01:23:45";
                    } else {
                        dateTime = date + " " + time;
                    }
                    biometryOD.setCalculDate(DateFunction.parseDateLocalDateTime(dateTime));
                    biometryOS.setCalculDate(DateFunction.parseDateLocalDateTime(dateTime));
                    biometryOD.setExamDate(DateFunction.parseDateLocalDateTime(dateTime));
                    biometryOS.setExamDate(DateFunction.parseDateLocalDateTime(dateTime));
                    if(biometryOD.getExamDate() == null) {
                        String log = "ERROR Biometry OD" + i + "/" + (fileDatas.size() - 1) + " name: " + patient.getFullName() + " error date format.";
                        logger.info(log);
                        sendLog(tokenUser, log);
                    } else {
                        if (cataractRepository.findExamByPatientIdAndExamDateAndEyeSideAndMachine(patient.getId(), biometryOD.getExamDate(), biometryOD.getEyeSide(), biometryOD.getMachine(), organizationsId) == null) {
                            addExam(biometryOD, patient.getId(), organizationsId);
                            String log = "Info: Biometry OD " + i + "/" + (fileDatas.size() - 1) + " imported.";
                            logger.info(log);
                            sendLog(tokenUser, log);
                        } else {
                            String log = "Info: Biometry OD " + i + "/" + (fileDatas.size() - 1) + " already exists.";
                            logger.info(log);
                            sendLog(tokenUser, log);
                        }
                    }
                    if(biometryOS.getExamDate() == null) {
                        String log = "ERROR Biometry OS " + i + "/" + (fileDatas.size() - 1) + " name: " + patient.getFullName() + " error date format.";
                        logger.info(log);
                        sendLog(tokenUser, log);
                    } else {
                        if (cataractRepository.findExamByPatientIdAndExamDateAndEyeSideAndMachine(patient.getId(), biometryOS.getExamDate(), biometryOS.getEyeSide(), biometryOS.getMachine(), organizationsId) == null) {
                            addExam(biometryOS, patient.getId(), organizationsId);
                            String log = "Info: Biometry OS " + i + "/" + (fileDatas.size() - 1) + " imported.";
                            logger.info(log);
                            sendLog(tokenUser, log);
                        } else {
                            String log = "Info: Biometry OS " + i + "/" + (fileDatas.size() - 1) + " already exists.";
                            logger.info(log);
                            sendLog(tokenUser, log);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error while importing biometries.");
        }
    }

    private void sendLog(String tokenUser, String logMessage) {
        Sinks.Many<String> userSink = userLogSinks.get(tokenUser);
        if (userSink != null) {
            Sinks.EmitResult result = userSink.tryEmitNext(logMessage);
            if (result.isFailure()) {
                System.err.println("Error when emit log : " + result.name());
            }
        }
    }

}
