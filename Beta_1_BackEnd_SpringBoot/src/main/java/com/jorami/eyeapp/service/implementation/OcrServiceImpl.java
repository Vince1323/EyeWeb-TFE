package com.jorami.eyeapp.service.implementation;

import com.jorami.eyeapp.dto.exam.ExamDto;
import com.jorami.eyeapp.dto.patient.PatientDto;
import com.jorami.eyeapp.model.Enum;
import com.jorami.eyeapp.service.OcrService;
import com.jorami.eyeapp.util.DateFunction;
import com.jorami.eyeapp.util.SelectorConfig;
import com.jorami.eyeapp.util.Utils;
import lombok.AllArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jorami.eyeapp.util.Utils.*;
import static net.sourceforge.tess4j.util.ImageHelper.rotateImage;

@Service
@AllArgsConstructor
public class OcrServiceImpl implements OcrService {

    private final Tesseract tesseract;

    private static final Logger logger = Logger.getLogger(OcrServiceImpl.class);


    //GET
    @Override
    public String getStringFromOCR(MultipartFile file) {
        BufferedImage image = null;
        String text = "";
        try {
            image = getBufferedImage(file);
            //Transforme l'image en texte
            text = tesseract.doOCR(image).toLowerCase();
        } catch (TesseractException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error while getting text from file.");
        }
        logger.info("------------------------------------- Résultat: " + text);
        return text;
    }


    //POST
    /**
     * Retourne des biométries créées sur base de la lecture d'un fichier .pdf, .png ou .jpg.
     * @param file Le fichier .pdf, .png ou .jpg à lire pour récupérer les données des biométries.
     * @param biometer Le nom de la machine à partir de laquelle est créé le fichier.
     * @param side Le côté de l'œil pour lequel on souhaite obtenir des résultats (OD, OS ou OU).
     * @return La ou les biométries créées sur base du fichier reçu.
     */
    @Override
    public PatientDto recognizeText(MultipartFile file, String biometer, String side, List<Long> organizationsId) {
        try {
            //Transforme le fichier reçu en BufferedImage pour réaliser l'OCR
            BufferedImage image = getBufferedImage(file);
            //Transforme l'image en texte
            String text = tesseract.doOCR(image).toLowerCase();

            SelectorConfig selectorConfig = Utils.loadSelectorConfig("OcrPatterns", SelectorConfig.class);
            String extensionFile = FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase();
            PatientDto patient = new PatientDto();

            switch(biometer) {
                case "OCULUS PENTACAM AXL":
                    patient = createBiometry(text, side, biometer, selectorConfig.getPentacamOcrSelector(), 7, extensionFile, false, image, 0);
                    break;
                case "ZEISS IOLMASTER 500":
                    patient = createBiometry(text, side, biometer, selectorConfig.getIolMaster500OcrSelector(), 7, extensionFile, false, image, 0);
                    break;
                case "ZEISS IOLMASTER 700":
                    patient = createBiometry(text, side, biometer, selectorConfig.getIolMaster700OcrSelector(), 7, extensionFile, false, image, 0);
                    break;
                case "MOVU/ALCON ARGOS":
                    patient = createBiometry(text, side, biometer, selectorConfig.getArgosOcrSelector(), 7, extensionFile, false, image, 0);
                    break;
                case "TOPCON Aladdin":
                    patient = createBiometry(text, side, biometer, selectorConfig.getTopConOcrSelector(), 7, extensionFile, false, image, 0);
                    break;
                case "HEIDELBERG ANTERION":
                    patient = createBiometry(text, side, biometer, selectorConfig.getAnterionOcrSelector(), 7, extensionFile, true, image, 0);
                    break;
            }
            return patient;
        } catch (TesseractException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error while reading image.");
        } catch (IOException | RuntimeException | UnsatisfiedLinkError e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error while reading file.");
        }
    }


    //PRIVATES METHODS
    /**
     * Crée un objet BufferedImage sur base du fichier reçu, car la méthode doOCR reçoit cet objet en paramètre.
     * @param file Le fichier à transformer.
     * @return Le BufferedImage pour l'OCR.
     */
    private static BufferedImage getBufferedImage(MultipartFile file) {
        BufferedImage image = null;

        try {
            //Transformer le PDF en image, si on reçoit un PDF
            if(file.getOriginalFilename() != null && file.getOriginalFilename().endsWith(".pdf")) {
                // Ouvre le fichier PDF
                PDDocument document = PDDocument.load(file.getInputStream());
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                for (int page = 0; page < 1; page++) {
                    //Crée l'image sur base du PDF
                    image = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                }
                document.close();
            } else if(file.getOriginalFilename() != null && file.getOriginalFilename().endsWith(".heic")) {
                throw new RuntimeException("File format HEIF/HEIC is not supported. ");
            } else {
                image = ImageIO.read(file.getInputStream());
            }
        } catch (IOException | RuntimeException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error while reading file.");
        }

        return image;
    }

    private PatientDto createBiometry(String text, String side, String biometer, Map<String, String> ocrSelector, int group, String extensionFile, boolean duplicatedRegex, BufferedImage image, int recursiveCallCounter) {
        List<ExamDto> biometries = new ArrayList<>();
        PatientDto patient = new PatientDto();

        try {
            String dateTime;
            String date = "";
            String time = "";

            if (ocrSelector != null) {
                ExamDto biometryOD = new ExamDto();
                biometryOD.setEyeSide(Enum.EyeSide.OD);
                biometryOD.setImportType(extensionFile);
                biometryOD.setMachine(biometer);
                ExamDto biometryOS = new ExamDto();
                biometryOS.setEyeSide(Enum.EyeSide.OS);
                biometryOS.setImportType(extensionFile);
                biometryOS.setMachine(biometer);

                String[] result;

                //Patient
                if (ocrSelector.containsKey("patient")) {
                    String firstname = getDataFromPattern(text, ocrSelector.get("patient"), 8, false)[0];
                    String lastname = getDataFromPattern(text, ocrSelector.get("patient"), 7, false)[0];
                    patient.setFirstname(firstname);
                    patient.setLastname(lastname);
                    if (ocrSelector.containsKey("birthDate")) {
                        String birthDate = getDataFromPattern(text, ocrSelector.get("birthDate"), 7, false)[0];
                        if (birthDate != null && !birthDate.isEmpty()) {
                            patient.setBirthDate(DateFunction.parseDateLocalDate(birthDate).toString());
                        }
                    }
                    patient.setHobbies(text);
                }

                //Exam date
                if (ocrSelector.containsKey("examDate")) {
                    String examDate = getDataFromPattern(text, ocrSelector.get("examDate"), 7, false)[0];
                    if (examDate != null && !examDate.isEmpty()) {
                        date = DateFunction.parseDateLocalDate(examDate).toString();
                        biometryOD.setExamDate(DateFunction.parseDateLocalDate(examDate).toString());
                        biometryOS.setExamDate(DateFunction.parseDateLocalDate(examDate).toString());
                    }
                }

                //AL
                if (ocrSelector.containsKey("al")) {
                    result = getDataFromPattern(text, ocrSelector.get("al"), group, duplicatedRegex);
                    biometryOD.setAl(formatValueWithXIntegers(result[0], 2));
                    biometryOS.setAl(formatValueWithXIntegers(result[1], 2));
                }

                //ACD
                if (ocrSelector.containsKey("acd")) {
                    result = getDataFromPattern(text, ocrSelector.get("acd"), group, duplicatedRegex);
                    biometryOD.setAcd(formatValueWithXIntegers(result[0], 1));
                    biometryOS.setAcd(formatValueWithXIntegers(result[1], 1));
                }

                //INTERNAL ACD
                if (ocrSelector.containsKey("internalAcd")) {
                    result = getDataFromPattern(text, ocrSelector.get("internalAcd"), group, duplicatedRegex);
                    biometryOD.setInternalAcd(formatToFloat(result[0]));
                    biometryOS.setInternalAcd(formatToFloat(result[1]));
                }

                //PUPIL DIA
                if (ocrSelector.containsKey("pupilDia")) {
                    result = getDataFromPattern(text, ocrSelector.get("pupilDia"), group, duplicatedRegex);
                    biometryOD.setPupilDia(formatValueWithXIntegers(result[0], 1));
                    biometryOS.setPupilDia(formatValueWithXIntegers(result[1], 1));
                }

                //PUPIL MIN
                if (ocrSelector.containsKey("pupilMin")) {
                    result = getDataFromPattern(text, ocrSelector.get("pupilMin"), group, duplicatedRegex);
                    biometryOD.setPupilMin(formatToFloat(result[0]));
                    biometryOS.setPupilMin(formatToFloat(result[1]));
                }

                //PUPIL MAX
                if (ocrSelector.containsKey("pupilMax")) {
                    result = getDataFromPattern(text, ocrSelector.get("pupilMax"), group, duplicatedRegex);
                    biometryOD.setPupilMax(formatToFloat(result[0]));
                    biometryOS.setPupilMax(formatToFloat(result[1]));
                }

                //WTW
                if (ocrSelector.containsKey("wtw")) {
                    result = getDataFromPattern(text, ocrSelector.get("wtw"), group, duplicatedRegex);
                    biometryOD.setWtw(formatValueWithXIntegers(result[0], 2));
                    biometryOS.setWtw(formatValueWithXIntegers(result[1], 2));
                }

                //CORD
                if (ocrSelector.containsKey("cord")) {
                    result = getDataFromPattern(text, ocrSelector.get("cord"), group, duplicatedRegex);
                    biometryOD.setCord(formatValueWithXIntegers(result[0], 1));
                    biometryOS.setCord(formatValueWithXIntegers(result[1], 1));
                }

                //Z40
                if (ocrSelector.containsKey("z40")) {
                    result = getDataFromPattern(text, ocrSelector.get("z40"), group, duplicatedRegex);
                    biometryOD.setZ40(formatValueWithXIntegers(result[0], 1));
                    biometryOS.setZ40(formatValueWithXIntegers(result[1], 1));
                }

                //HOA
                if (ocrSelector.containsKey("hoa")) {
                    result = getDataFromPattern(text, ocrSelector.get("hoa"), group, duplicatedRegex);
                    biometryOD.setHoa(formatValueWithXIntegers(result[0], 1));
                    biometryOS.setHoa(formatValueWithXIntegers(result[1], 1));
                }

                //N
                if (ocrSelector.containsKey("n")) {
                    result = getDataFromPattern(text, ocrSelector.get("n"), group, false);
                    biometryOD.setN(formatValueWithXIntegers(result[0], 1));
                    biometryOS.setN(formatValueWithXIntegers(result[1], 1));
                }

                //K1
                if (ocrSelector.containsKey("k1")) {
                    result = getDataFromPattern(text, ocrSelector.get("k1"), group, duplicatedRegex);
                    biometryOD.setK1(formatValueWithXIntegers(result[0], 2));
                    biometryOS.setK1(formatValueWithXIntegers(result[1], 2));
                }

                //K1Mm
                if (ocrSelector.containsKey("k1Mm")) {
                    result = getDataFromPattern(text, ocrSelector.get("k1Mm"), group, duplicatedRegex);
                    biometryOD.setK1Mm(formatValueWithXIntegers(result[0], 1));
                    biometryOS.setK1Mm(formatValueWithXIntegers(result[1], 1));
                }

                //K1Axis
                if (ocrSelector.containsKey("k1Axis")) {
                    result = getDataFromPattern(text, ocrSelector.get("k1Axis"), group, duplicatedRegex);
                    biometryOD.setK1Axis(formatValueForDegrees(result[0]));
                    biometryOS.setK1Axis(formatValueForDegrees(result[1]));
                }

                //K2
                if (ocrSelector.containsKey("k2")) {
                    result = getDataFromPattern(text, ocrSelector.get("k2"), group, duplicatedRegex);
                    biometryOD.setK2(formatValueWithXIntegers(result[0], 2));
                    biometryOS.setK2(formatValueWithXIntegers(result[1], 2));
                }

                //K2Mm
                if (ocrSelector.containsKey("k2Mm")) {
                    result = getDataFromPattern(text, ocrSelector.get("k2Mm"), group, duplicatedRegex);
                    biometryOD.setK2Mm(formatValueWithXIntegers(result[0], 1));
                    biometryOS.setK2Mm(formatValueWithXIntegers(result[1], 1));
                }

                //K2Axis
                if (ocrSelector.containsKey("k2Axis")) {
                    result = getDataFromPattern(text, ocrSelector.get("k2Axis"), group, duplicatedRegex);
                    biometryOD.setK2Axis(formatValueForDegrees(result[0]));
                    biometryOS.setK2Axis(formatValueForDegrees(result[1]));
                }

                //KAstig
                if (ocrSelector.containsKey("kAstig")) {
                    result = getDataFromPattern(text, ocrSelector.get("kAstig"), group, duplicatedRegex);
                    biometryOD.setK_astig(formatValueWithXIntegers(result[0], 1));
                    biometryOS.setK_astig(formatValueWithXIntegers(result[1], 1));
                }

                //KAstigAxis
                if (ocrSelector.containsKey("kAstigAxis")) {
                    result = getDataFromPattern(text, ocrSelector.get("kAstigAxis"), group, duplicatedRegex);
                    biometryOD.setK_astigAxis(formatValueForDegrees(result[0]));
                    biometryOS.setK_astigAxis(formatValueForDegrees(result[1]));
                }

                //KAvg
                if (ocrSelector.containsKey("kAvg")) {
                    result = getDataFromPattern(text, ocrSelector.get("kAvg"), group, duplicatedRegex);
                    biometryOD.setK_avg(formatValueWithXIntegers(result[0], 2));
                    biometryOS.setK_avg(formatValueWithXIntegers(result[1], 2));
                }

                //RAvg
                if (ocrSelector.containsKey("rAvg")) {
                    result = getDataFromPattern(text, ocrSelector.get("rAvg"), group, duplicatedRegex);
                    biometryOD.setR_avg(formatToFloat(result[0]));
                    biometryOS.setR_avg(formatToFloat(result[1]));
                }

                //K1CorneaBack
                if (ocrSelector.containsKey("k1CorneaBack")) {
                    result = getDataFromPattern(text, ocrSelector.get("k1CorneaBack"), group, duplicatedRegex);
                    biometryOD.setK1CorneaBack(formatToFloat(result[0]));
                    biometryOS.setK1CorneaBack(formatToFloat(result[1]));
                }

                //K2CorneaBack
                if (ocrSelector.containsKey("k2CorneaBack")) {
                    result = getDataFromPattern(text, ocrSelector.get("k2CorneaBack"), group, duplicatedRegex);
                    biometryOD.setK2CorneaBack(formatToFloat(result[0]));
                    biometryOS.setK2CorneaBack(formatToFloat(result[1]));
                }

                //K1AxisCorneaBack
                if (ocrSelector.containsKey("k1AxisCorneaBack")) {
                    result = getDataFromPattern(text, ocrSelector.get("k1AxisCorneaBack"), group, duplicatedRegex);
                    biometryOD.setK1AxisCorneaBack(formatToFloat(result[0]));
                    biometryOS.setK1AxisCorneaBack(formatToFloat(result[1]));
                }

                //K2AxisCorneaBack
                if (ocrSelector.containsKey("k2AxisCorneaBack")) {
                    result = getDataFromPattern(text, ocrSelector.get("k2AxisCorneaBack"), group, duplicatedRegex);
                    biometryOD.setK2AxisCorneaBack(formatToFloat(result[0]));
                    biometryOS.setK2AxisCorneaBack(formatToFloat(result[1]));
                }

                //SiaCyl
                if (ocrSelector.containsKey("siaCyl")) {
                    result = getDataFromPattern(text, ocrSelector.get("siaCyl"), group, duplicatedRegex);
                    biometryOD.setSiaCyl(formatValueWithXIntegers(result[0], 1));
                    biometryOS.setSiaCyl(formatValueWithXIntegers(result[1], 1));
                }

                //SiaAxis
                if (ocrSelector.containsKey("siaAxis")) {
                    result = getDataFromPattern(text, ocrSelector.get("siaAxis"), group, duplicatedRegex);
                    biometryOD.setSiaAxis(formatValueForDegrees(result[0]));
                    biometryOS.setSiaAxis(formatValueForDegrees(result[1]));
                }

                //SNR
                if (ocrSelector.containsKey("snr")) {
                    result = getDataFromPattern(text, ocrSelector.get("snr"), group, duplicatedRegex);
                    biometryOD.setSnr(formatValueWithXIntegers(result[0], 2));
                    biometryOS.setSnr(formatValueWithXIntegers(result[1], 2));
                }

                //LensThickness
                if (ocrSelector.containsKey("lensThickness")) {
                    result = getDataFromPattern(text, ocrSelector.get("lensThickness"), group, duplicatedRegex);
                    biometryOD.setLensThickness(formatValueWithXIntegers(result[0], 1));
                    biometryOS.setLensThickness(formatValueWithXIntegers(result[1], 1));
                }

                //CCT
                if (ocrSelector.containsKey("cct")) {
                    result = getDataFromPattern(text, ocrSelector.get("cct"), group, duplicatedRegex);
                    biometryOD.setCct(formatValueWithXIntegers(result[0], 3));
                    biometryOS.setCct(formatValueWithXIntegers(result[1], 3));
                }

                //CCTMin
                if (ocrSelector.containsKey("cctMin")) {
                    result = getDataFromPattern(text, ocrSelector.get("cctMin"), group, duplicatedRegex);
                    biometryOD.setCctMin(formatToFloat(result[0]));
                    biometryOS.setCctMin(formatToFloat(result[1]));
                }

                //EyeStatus
                if (ocrSelector.containsKey("eyeStatus")) {
                    result = getDataFromPattern(text, ocrSelector.get("eyeStatus"), group, false);
                    biometryOD.setEyeStatus(result[0]);
                    biometryOS.setEyeStatus(result[1]);
                }

                //Je laisse le if, dans le cas où on récupère l'heure un jour sur une image.
                if (time.isEmpty()) {
                    //Temps identique pour toutes les bio n'ayant pas d'heure d'examen ou de calcul
                    dateTime = date + " 01:23:45";
                } else {
                    dateTime = date + " " + time;
                }
                try {
                    biometryOD.setCalculDate(DateFunction.parseDateLocalDateTime(dateTime).toString());
                    biometryOS.setCalculDate(DateFunction.parseDateLocalDateTime(dateTime).toString());
                    biometryOD.setExamDate(DateFunction.parseDateLocalDateTime(dateTime).toString());
                    biometryOS.setExamDate(DateFunction.parseDateLocalDateTime(dateTime).toString());
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }


                //Permet de retourner la biométrie pour le côté demandé
                if (side.equals("OU") || side.equals("OD")) {
                    biometries.add(biometryOD);
                }
                if (side.equals("OU") || side.equals("OS")) {
                    biometries.add(biometryOS);
                }
            }


            //Début tests rotation image

            if(recursiveCallCounter < 3) {
                int nullValuesCounter = 0;
                if(biometries.get(0).getAl() == null) {
                    nullValuesCounter++;
                }
                if(biometries.get(0).getAcd() == null) {
                    nullValuesCounter++;
                }
                if(biometries.get(0).getK1() == null) {
                    nullValuesCounter++;
                }
                if(biometries.get(0).getK2() == null) {
                    nullValuesCounter++;
                }
                if(nullValuesCounter > 2) {
                    recursiveCallCounter += 1;
                    image = rotateImage(image, recursiveCallCounter * 90);
                    /*
                    File output = new File("imageTournee" + (recursiveCallCounter * 90) + "degres.jpg");
                    ImageIO.write(image, "jpg", output);
                    */
                    text = tesseract.doOCR(image).toLowerCase();
                    patient = createBiometry(text, side, biometer, ocrSelector, group, extensionFile, duplicatedRegex, image, recursiveCallCounter);
                    biometries = patient.getExamDtos();
                }
            }

            //Fin tests rotation image

            patient.setExamDtos(biometries);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Error while reading file.");
        }
        return patient;
    }

    /**
     * Retourne les données récupérées sur base du texte donné pour les deux yeux.
     * @param text Le texte créé par l'OCR sur base du fichier reçu.
     * @param regex Le pattern permettant de récupérer la valeur recherchée.
     * @param group Le groupe dans lequel se trouve la donnée recherchée.
     * @return Un tableau contenant les valeurs recherchées pour les deux yeux.
     */
    private String[] getDataFromPattern(String text, String regex, int group, boolean duplicatedRegex) {
        String[] datas = new String[] {"", ""};

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        int counter = 0;
        while(matcher.find() && counter <= 1) {
            //Si les données sont sur la même ligne mais que le titre de la valeur (Ex.: acd, al, wtw, ...) n'est pas dupliqué
            //Comme sur Anterion par exemple (wtw valeurOD valeurOS)
            //Sinon, les données sont sur la même ligne mais que le titre de la valeur est dupliqué
            //Comme sur Pentacam par exemple (wtw valeurOD wtw valeurOS)
            if(duplicatedRegex) {
                datas[0] = matcher.group(group);
                datas[1] = matcher.group(group * 2);
                counter++;
            } else {
                datas[counter] = matcher.group(group);
            }
            counter++;
        }
        //Si une des deux données est à "", attribuer la valeur de l'autre à cette donnée.
        if(datas[0].isEmpty() && !datas[1].isEmpty()) {
            datas[0] = datas[1];
        } else if(!datas[0].isEmpty() && datas[1].isEmpty()) {
            datas[1] = datas[0];
        }
        return datas;
    }

}

