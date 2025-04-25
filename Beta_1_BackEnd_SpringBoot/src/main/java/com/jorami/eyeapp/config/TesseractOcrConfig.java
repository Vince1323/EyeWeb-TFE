package com.jorami.eyeapp.config;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class TesseractOcrConfig {

    @Bean
    public Tesseract tesseract() {
        Tesseract tesseract = new Tesseract();
        Path dataDirectory = Paths.get("src/tessdata");
        //Path dataDirectory = Paths.get("/usr/local/share/tessdata");  //TODO: Ne pas supprimer !
        tesseract.setDatapath(dataDirectory.toString());
        tesseract.setLanguage("eng");
        tesseract.setVariable("user_defined_dpi", "300");
        return tesseract;
    }

}
