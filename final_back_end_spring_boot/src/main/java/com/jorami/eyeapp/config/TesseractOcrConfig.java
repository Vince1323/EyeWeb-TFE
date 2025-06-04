package com.jorami.eyeapp.config;

// Importation de la librairie tess4j pour l’OCR
import net.sourceforge.tess4j.Tesseract; // Classe principale de la librairie Tesseract OCR

// Importation des annotations de configuration Spring
import org.springframework.context.annotation.Bean; // Permet de déclarer un bean manuellement
import org.springframework.context.annotation.Configuration; // Spécifie que cette classe est une classe de configuration Spring

// Importation Java standard pour manipuler les chemins de fichiers
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration pour le moteur OCR Tesseract.
 * Ce bean permet d'utiliser la reconnaissance de texte à partir d'images dans l'application.
 */
@Configuration
public class TesseractOcrConfig {

    /**
     * Initialise et configure un bean Tesseract.
     * @return une instance configurée du moteur OCR Tess4J
     */
    @Bean
    public Tesseract tesseract() {
        Tesseract tesseract = new Tesseract();

        // Chemin vers le dossier contenant les fichiers de données de langue (*.traineddata)
        Path dataDirectory = Paths.get("src/tessdata");
        // Pour les environnements Linux, un chemin alternatif est commenté ici :
        // Path dataDirectory = Paths.get("/usr/local/share/tessdata");  //TODO: Ne pas supprimer !

        // Définir le chemin des données de langue
        tesseract.setDatapath(dataDirectory.toString());

        // Définir la langue de reconnaissance ("eng" = anglais)
        tesseract.setLanguage("eng");

        // Définir une valeur DPI pour améliorer la qualité de reconnaissance
        tesseract.setVariable("user_defined_dpi", "300");

        return tesseract;
    }
}
