package com.jorami.eyeapp.strategy;

import com.jorami.eyeapp.model.User;
import java.io.IOException;

public interface EmailStrategy {

    // Constantes réutilisables dans les templates d’e-mails
    String EMAIL_VALIDATION_TITLE = "Check validation";
    String EMAIL_VALIDATION_MESSAGE = "Please enter the code below to validate your email.";
    String RESET_PASSWORD_TITLE = "Reset password";
    String RESET_PASSWORD_MESSAGE = "Use the code below to authorize the reset of your password.";

    /**
     * Méthode pour générer dynamiquement le contenu d’un e-mail.
     * @param user L'utilisateur concerné
     * @param code Le code à insérer dans l’e-mail (validation ou réinitialisation)
     * @return Le contenu HTML prêt à être envoyé
     */
    String prepareEmailContent(User user, String code) throws IOException;

    /**
     * Sujet de l’e-mail (chaque stratégie a un titre spécifique)
     */
    String getSubject();
}
