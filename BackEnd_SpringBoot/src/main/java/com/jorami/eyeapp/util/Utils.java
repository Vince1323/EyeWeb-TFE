package com.jorami.eyeapp.util;

import com.google.gson.Gson;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.jboss.logging.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Utils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER_HYPHEN_FILE = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS");
    private static final Logger logger = Logger.getLogger(Utils.class);


    /**
     * Retourne les noms des propriétés nulles d'un objet. Trie uniquement les propriétés non nulles
     * @param source L'objet dont les propriétés seront vérifiées pour les valeurs nulles.
     * @return Un tableau de chaînes contenant les noms des propriétés nulles de l'objet source.
     */
    public static String[] getNullPropertyNames(Object source) {
        BeanWrapperImpl src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * Met à jour les propriétés de l'objet cible à null si les propriétés correspondantes
     * dans l'objet source sont null. Cette méthode parcourt toutes les propriétés de l'objet source
     * et, pour chaque propriété qui est null, définit la propriété correspondante dans l'objet cible à null.
     * Seules les propriétés modifiables sont mises à jour.
     *
     * @param source l'objet source à partir duquel copier les propriétés null
     * @param target l'objet cible vers lequel les propriétés null doivent être copiées
     */
    public static void updateNullProperties(Object source, Object target) {
        BeanWrapper src = new BeanWrapperImpl(source);
        BeanWrapper trg = new BeanWrapperImpl(target);

        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        for (PropertyDescriptor pd : pds) {
            String propName = pd.getName();
            Object srcValue = src.getPropertyValue(propName);
            if (srcValue == null && src.isWritableProperty(propName) && trg.isWritableProperty(propName)) {
                trg.setPropertyValue(propName, null);
            }
        }
    }

    /**
     * Génère un code aléatoire à six chiffres.
     * <p>
     * Cette méthode crée un nombre entier aléatoire à six chiffres, pouvant être utilisé comme code de vérification
     * ou pour tout autre besoin nécessitant un nombre aléatoire dans cette plage de taille.
     * Le code généré sera toujours compris entre 100000 et 999999.
     *
     * @return Le code à six chiffres sous forme de chaîne de caractères.
     */
    public static String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);

        return Integer.toString(code);
    }

    public static <R> R loadSelectorConfig(String jsonFile, Class<R> resultClass) throws IOException {
        String resourcePath = "/" + jsonFile + ".json";

        //Utilise le ClassLoader pour charger le fichier de ressources.
        try (InputStream inputStream = Utils.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                //Gestion de l'erreur si le fichier n'est pas trouvé.
                throw new FileNotFoundException("Resource file not found: " + resourcePath);
            }

            //Utilise inputStream pour lire le fichier depuis le package resources.
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            return gson.fromJson(reader, resultClass);
        } catch (IOException e) {
            throw new IOException("Failed to load resource file: " + resourcePath, e);
        }
    }

    public static Float calculK2Axis(Float k1Axis) {
        float k2Axis = 0.0f;
        if(k1Axis != null) {
            if(k1Axis > 90) {
                k2Axis = (k1Axis - 90);
            } else {
                k2Axis = (k1Axis + 90);
            }
        }
        return k2Axis;
    }

    public static Float calculKAvg(Float k1, Float k2) {
        float kAvg = 0.0f;
        if(k1 != null && k2 != null) {
            kAvg = (k1 + k2) / 2;
        }
        return kAvg;
    }

    public static Float calculKAstig(Float k1, Float k2) {
        float kAstig = 0.0f;
        if(k1 != null && k2 != null) {
            kAstig = k2 - k1;
        }
        return kAstig;
    }

    public static String calculatePACD(double aConstant) {
        return String.valueOf(0.58357 * aConstant - 63.896);
    }

    public static List<List<String>> getFileDatas(MultipartFile file) throws IOException {
        try {
            File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            FileOutputStream fos = new FileOutputStream(convertedFile);
            fos.write(file.getBytes());
            fos.close();

            Path source = Paths.get(convertedFile.getAbsolutePath());
            String newPath = LocalDateTime.now().format(Utils.DATE_TIME_FORMATTER_HYPHEN_FILE) + FileNameUtils.getExtension(convertedFile.getAbsolutePath());
            Files.move(source, source.resolveSibling(newPath), StandardCopyOption.REPLACE_EXISTING);

            if(file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
                return Utils.getListFromExcelXLSX(newPath);
            } else if(file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
                return Utils.getListFromExcelCSV(newPath);
            }
            throw new IOException("Unsupported file format.");
        } catch(RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Float formatValueWithXIntegers(String value, int nbIntegers) {
        value = value.replaceFirst(",|\\s", ".");
        try {
            Float valueParsee = Float.parseFloat(value);

            if(!value.contains(".") && value.length() >= nbIntegers + 1) {
                if(value.contains("-")) {
                    value = value.substring(0, nbIntegers + 1) + "." + value.substring(nbIntegers + 1);
                } else {
                    value = value.substring(0, nbIntegers) + "." + value.substring(nbIntegers);
                }
            }

            value = String.format("%.5f", Float.parseFloat(value));
            value = value.replaceFirst(",|\\s", ".");
        } catch (Exception e) {
            logger.error(e.getMessage() + " value: " + value);
        }

        return formatToFloat(value);
    }

    public static Float formatValueForDegrees(String value) {
        try {
            Float valueParsee = Float.parseFloat(value);

            value = valueParsee.toString();

            if(valueParsee > 180) {
                value = value.substring(0, value.length() - 3);
            }
        } catch (Exception e) {
            logger.error(e.getMessage() + " value: " + value);
        }

        return formatToFloat(value);
    }


    public static List<List<String>> getListFromExcelXLSX(String _fileName) throws IOException {
        List<List<String>> array = new ArrayList<>();
        List<String> values = new ArrayList<>();
        String value;
        try {
            FileInputStream file = new FileInputStream(_fileName);

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFCell cellT;

            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                values.clear();
                for (int j = 0; j <= sheet.getRow(i).getLastCellNum(); j++) {
                    cellT = sheet.getRow(i).getCell(j);
                    if (cellT == null) {
                        value = "/";
                    } else {
                        switch (cellT.getCellType()) {
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cellT)) {
                                    //Dans le cas d'une date normale sans heure : en LocalDateTime → heure fixée à minuit. Donc vérifier que heure != de minuit pour handle les heures.
                                    if (cellT.getLocalDateTimeCellValue().getMinute() != 0 || cellT.getLocalDateTimeCellValue().getHour() != 0 || cellT.getLocalDateTimeCellValue().getSecond() != 0) {
                                        LocalDateTime test = cellT.getLocalDateTimeCellValue();
                                        value = DateFunction.formatDateTime(test, "HH:mm:ss");
                                    } else {
                                        LocalDateTime test = cellT.getLocalDateTimeCellValue();
                                        value = DateFunction.formatDateTime(test, "dd-MM-yyyy");
                                    }

                                } else {
                                    value = BigDecimal.valueOf(cellT.getNumericCellValue()).toPlainString();
                                }
                                break;
                            case STRING:
                                value = cellT.getStringCellValue();
                                break;
                            case BOOLEAN:
                                value = String.valueOf(cellT.getBooleanCellValue());
                                break;
                            case FORMULA:
                                value = cellT.getCellFormula();
                                break;
                            case BLANK, _NONE, ERROR:
                                value = "/";
                                break;
                            default:
                                value = "/";
                                break;
                        }
                    }
                    value = value.trim();
                    if (value.isEmpty()) {
                        values.add("/");
                    } else {
                        values.add(value);
                    }
                }
                array.add(List.copyOf(values));
            }

            file.close();
            workbook.close();
            File fileToDelete = new File(_fileName);
            fileToDelete.delete();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        return array;
    }

    public static List<List<String>> getListFromExcelCSV(String fileName) throws IOException {
        List<List<String>> array = new ArrayList<>();
        int a = 0;
        Charset charset = StandardCharsets.UTF_8; // Définir l'encodage

        try (BufferedReader br = new BufferedReader(new FileReader(fileName, charset))) {
            String line;

            while ((line = br.readLine()) != null) {
                List<String> values = new ArrayList<>();
                String[] columns = line.split(";");
                if(a == 0) {
                    values = Arrays.asList(columns);

                    // Décoder les titres des colonnes
                    for (int i = 0; i < values.size(); i++) {
                        try {
                            values.set(i, charset.newDecoder().decode(ByteBuffer.wrap(values.get(i).getBytes(StandardCharsets.ISO_8859_1))).toString().replace("?", "é").trim());
                        } catch (Exception e) {
                            try {
                                values.set(i, charset.newDecoder().decode(ByteBuffer.wrap(values.get(i).getBytes(StandardCharsets.UTF_8))).toString().replace("?", "é").trim());
                            } catch (Exception ex) {
                                values.set(i, charset.newDecoder().decode(ByteBuffer.wrap(values.get(i).getBytes(StandardCharsets.UTF_16))).toString().replace("?", "é").trim());
                            }
                        }
                    }
                    a++;
                } else {
                    for (String column : columns) {
                        // Ajoute la valeur telle quelle à la liste (peut être modifié selon les besoins)
                        if(column.contains(",")) {
                            column = column.replace(',', '.');
                        }
                        values.add(column.trim());
                    }
                }

                array.add(List.copyOf(values));
            }
            br.close();
            File fileToDelete = new File(fileName);
            fileToDelete.delete();
        } catch (IOException e) {
            // Gérer les exceptions (log, etc.)
            throw new IOException(e.getMessage());
        }
        return array;
    }

    public static Float formatToFloat(String value) {
        try {
            return Float.parseFloat(value.replace(',', '.').trim());
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

}

