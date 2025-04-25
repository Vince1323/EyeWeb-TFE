package com.jorami.eyeapp.exception;

import org.jboss.logging.Logger;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.jorami.eyeapp.exception.ConstantMessage.JSON_KEY;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class);


    //400 BAD REQUEST---------------------------------------------------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception e) {
        LOGGER.error(HttpStatus.BAD_REQUEST + " - " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    /**
     * Fonctionne avec l'annotation @Valid.
     * Récupère toutes les erreurs de l'objet à créer/modifier sur base des règles de validation de
     * chaque propriété de l'objet reçu.
     * @param e La liste des erreurs de validation.
     * @return Une Map contenant, pour chaque propriété ayant une erreur de validation,
     * son message d'erreur et le code HTTP 400.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException e) {
        LOGGER.error(e.getMessage(), e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(JSON_KEY, errors));
    }

    /**
     * Indique qu'un paramètre est manquant ou une valeur de paramètre est invalide.
     * @param e Le message d'erreur.
     * @return Un message d'erreur et le code HTTP 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.error(HttpStatus.BAD_REQUEST + " - " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(JSON_KEY, e.getMessage()));
    }

    /**
     * Indique qu'un paramètre est manquant ou une valeur de paramètre est invalide.
     * @param e Le message d'erreur.
     * @return Un message d'erreur et le code HTTP 400.
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException e) {
        LOGGER.error(HttpStatus.BAD_REQUEST + " - " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(JSON_KEY, e.getMessage()));
    }

    /**
     * Indique qu'un paramètre de type incorrect a été fourni dans la requête.
     * @param e Le message d'erreur.
     * @return Un message d'erreur et le code HTTP 400.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        LOGGER.error(HttpStatus.BAD_REQUEST + " - " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(JSON_KEY, e.getMessage()));
    }


    //401 UNAUTHORIZED--------------------------------------------------------------------------------------------------
    /**
     * Indique que les informations d'identification fournies sont incorrectes.
     * @param e Le détail de l'exception reçue.
     * @return Un message d'erreur et le code HTTP 401.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e) {
        LOGGER.error(HttpStatus.UNAUTHORIZED + " - " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(JSON_KEY, e.getMessage()));
    }

    /**
     * Indique que l'utilisateur n'est pas autorisé à accéder à cette ressource.
     * @param e Le message d'erreur.
     * @return Un message d'erreur et le code HTTP 401.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleNoSuchElementException(ResponseStatusException e) {
        LOGGER.error(HttpStatus.UNAUTHORIZED + " - " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(JSON_KEY, e.getMessage()));
    }


    //403 FORBIDDEN ----------------------------------------------------------------------------------------------------
    /**
     * Indique que l'utilisateur n'a pas l'accès à cette ressource.
     * @param e Le message d'erreur.
     * @return Un message d'erreur et le code HTTP 403.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        LOGGER.error(HttpStatus.FORBIDDEN + " - " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(JSON_KEY, e.getMessage()));
    }


    //404 NOT FOUND-----------------------------------------------------------------------------------------------------
    /**
     * Indique que l'objet recherché n'existe pas.
     * @param e L'identifiant de l'objet recherché.
     * @return Un message d'erreur et le code HTTP 404.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
        //TODO: Nouvelle manière de faire?
        LOGGER.error(HttpStatus.NOT_FOUND + " - " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }


    //409 CONFLICT------------------------------------------------------------------------------------------------------
    /**
     * Indique que l'objet a déjà été modifié par un autre utilisateur.
     * @param e Les deux objets concaténés et séparés par ";". Le premier est la source, le second est celui modifié.
     * @return Un message d'erreur et le code HTTP 409.
     */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<?> handleConflictException(OptimisticLockingFailureException e) {
        LOGGER.error(HttpStatus.CONFLICT + " - " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(JSON_KEY, e.getMessage()));
    }


    //500 INTERNAL SERVER ERROR-----------------------------------------------------------------------------------------
    /**
     * Indique qu'une erreur interne est survenue.
     * @param e Le détail de l'exception reçue.
     * @return Un message d'erreur et le code HTTP 500.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        LOGGER.error(HttpStatus.INTERNAL_SERVER_ERROR + " - " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(JSON_KEY, e.getMessage()));
    }

}
