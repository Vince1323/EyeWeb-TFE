package com.jorami.eyeapp.strategy.Implementation;

import com.jorami.eyeapp.model.User;
import com.jorami.eyeapp.strategy.EmailStrategy;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ValidationEmailStrategy implements EmailStrategy {

    // Prépare un e-mail contenant un code de validation
    @Override
    public String prepareEmailContent(User user, String code) throws IOException {
        String content = loadTemplate("templates/code_validation_template.html");
        return content.replace("[[Name]]", user.getFullName())
                .replace("[[CODE]]", code)
                .replace("[[Title]]", EMAIL_VALIDATION_TITLE)
                .replace("[[Message]]", EMAIL_VALIDATION_MESSAGE);
    }

    @Override
    public String getSubject() {
        return "Email validation";
    }

    private String loadTemplate(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(new ClassPathResource(path).getURI())));
    }
}