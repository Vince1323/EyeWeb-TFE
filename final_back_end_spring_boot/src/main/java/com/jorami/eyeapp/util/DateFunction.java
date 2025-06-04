package com.jorami.eyeapp.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Classe utilitaire permettant d'analyser, parser et formater des dates sous différents formats.
 */
public class DateFunction {

    /**
     * Une map de regex permettant d'associer un format de date à un motif reconnu.
     * Clé : expression régulière ; Valeur : format Java (ex. "dd-MM-yyyy").
     */
    private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {{
        put("^\\d{8}$", "yyyyMMdd");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
        put("^\\d{1,2}\\.\\d{1,2}\\.\\d{4}$", "dd.MM.yyyy");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
        //put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "dd/MM/yyyy");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
        put("^\\d{12}$", "yyyyMMddHHmm");
        put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
        put("^\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\s\\d{1,2}:\\d{2}$", "dd.MM.yyyy HH:mm");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
        put("^\\d{14}$", "yyyyMMddHHmmss");
        put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
        put("^\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd.MM.yyyy HH:mm:ss");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}t\\d{1,2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{4}$", "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}t\\d{1,2}:\\d{2}:\\d{2}\\.\\d{3}z$", "yyyy-MM-dd'T'HH:mm:ss.SSS");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}t\\d{1,2}:\\d{2}:\\d{2}\\.\\d{2}z$", "yyyy-MM-dd'T'HH:mm:ss.SSS");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}t\\d{1,2}:\\d{2}:\\d{2}\\.\\d{1}z$", "yyyy-MM-dd'T'HH:mm:ss.SSS");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}t\\d{1,2}:\\d{2}:\\d{2}z$", "yyyy-MM-dd'T'HH:mm:ss");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}t\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd'T'HH:mm:ss");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}t\\d{1,2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{2}:\\d{2}$","yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}\\s(am|pm)$", "MM/dd/yyyy HH:mm:ss a");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}\\.\\d{7}$","yyyy-MM-dd HH:mm:ss.SSS");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}[tT]\\d{1,2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2}","yyyy-MM-dd'T'HH:mm:ss");
        put("^[a-zA-Z]{3}\\s[a-zA-Z]{3}\\s\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\s[a-zA-Z]{3,4}\\s\\d{4}$","EEE MMM d H:mm:ss zzz yyyy");
    }};

    // Détermine le format à utiliser en fonction du contenu de la chaîne fournie
    public static String determineDateFormat(String dateString) {
        for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
            if (dateString.toLowerCase().trim().matches(regexp)) {
                return DATE_FORMAT_REGEXPS.get(regexp);
            }
        }
        return null;
    }

    // Transforme une chaîne en LocalDateTime selon le format détecté automatiquement
    public static LocalDateTime parseDateLocalDateTime(String dateTimeString) {
        try {
            String dateFormat = determineDateFormat(dateTimeString);
            if (dateFormat == null) return null;
            return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(dateFormat));
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Transforme une chaîne en LocalDate selon le format détecté automatiquement
    public static LocalDate parseDateLocalDate(String dateTimeString) {
        try {
            String dateFormat = determineDateFormat(dateTimeString);
            if (dateFormat == null) return null;
            return LocalDate.parse(dateTimeString, DateTimeFormatter.ofPattern(dateFormat));
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Formatage d’un LocalDateTime en String selon un format donné
    public static String formatDateTime(LocalDateTime dateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }

    // Trouve et applique un format adapté à un LocalDate
    public static String findFormatLocalDateAndParseString(LocalDate date) {
        String format = determineDateFormat(date.toString());
        if (format == null) return null;
        return date.format(DateTimeFormatter.ofPattern(format));
    }

    // Trouve et applique un format adapté à un LocalDateTime
    public static String findFormatLocalDateTimeAndParseString(LocalDateTime date) {
        String format = determineDateFormat(date.toString());
        if (format == null) return null;
        return date.format(DateTimeFormatter.ofPattern(format));
    }
}
