package com.jorami.eyeapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO utilisé pour retourner des informations d'erreur standardisées aux clients.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private int status; // Code HTTP
    private String message; // Message principal
    private String timestamp; // Date de l'erreur
    private List<String> errors; // Liste détaillée des erreurs (facultative)

    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now().toString();
        this.errors = errors;
    }

    public ErrorResponse(int status, String message) {
        this(status, message, null);
    }
}
