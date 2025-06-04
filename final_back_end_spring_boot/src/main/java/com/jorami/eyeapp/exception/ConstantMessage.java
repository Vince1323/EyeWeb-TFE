package com.jorami.eyeapp.exception;
/**
 * Interface regroupant tous les messages constants de l'application.
 * Utile pour centraliser les messages d'erreurs et éviter les répétitions.
 */
public interface ConstantMessage {

    //VALIDATIONS
    String VALIDATION_NOT_BLANK = "Field cannot be empty.";
    String VALIDATION_EMAIL = "Invalid email format.";
    String VALIDATION_PASSWORD_MIN_LENGTH = "Password should be 8 characters long minimum.";
    String VALIDATION_PASSWORD_MAX_LENGTH = "Password should be shorter than 100 characters.";
    String SIZE_VALIDATION = "Size must be between 0 and ";



    //EXCEPTIONS
    //Clé pour messages JSON
    String JSON_KEY = "message";


    //200 OK
    String DELETE_OK = "Item has been deleted.";
    String ADDED = "Has been added.";
    String UPDATED = "Has been updated.";


    //400 BAD_REQUEST
    String USER_NOT_FOUND = "User not found.";
    String ORGANIZATION_NOT_FOUND = "Organization not found.";
    String ADD_USER_ERROR = "Add 'throws RuntimeException' to method signature";
    String IMPORT_ERROR = "Error while importing biometries.";
    String EMPTY_FILE = "Invalid or empty file.";
    String BIOMETER_UNSPECIFIED = "Invalid or unspecified biometer.";
    String BIOMETER_NOT_IMPLEMENTED = "This feature has not yet been implemented. (Other Biometry)";
    String DEVICE_NAME_UNSPECIFIED = "Invalid or unspecified device name.";
    String SIDE_EYE_UNSPECIFIED = "Invalid or unspecified eye side.";
    String CALCUL_UNSPECIFIED = "Invalid or unspecified calcul item.";
    String CALCUL_FIELDS_EMPTY = "K1, K2, Used Axial Length or Anterior Chamber Depth field's can't be empty.";


    //401 UNAUTHORIZED
    String UNAUTHORIZED_EMAIL_PASSWORD = "Your email or password is incorrect.";
    String CODE_EMAIL_VALIDATION = "Must be a correct code";
    String USER_ALREADY_EXISTS = "The user you are trying to create already exists.";
    String UNAUTHORIZED_ORGANIZATION = "You are not authorized to access this item.";
    String VALIDATE_TOKEN = "Token is not valid or expired.";

    //403 FORBIDDEN
    String USER_ACCESS = "User already exists but is not validated.";


    //404 NOT_FOUND
    String LIST_ITEM_NOT_FOUND = "The list of items you are searching is not found.";
    String ITEM_NOT_FOUND = "The item your are searching is not found.";
    String ROLE_NOT_FOUND = "Role USER was not initialized.";


    //409 CONFLICT
    String ITEM_ALREADY_EXISTS = "The item you are trying to create already exists. Please create a new one.";
    String ERROR_DELETE = "Error while deleting.";


    //500 INTERNAL_SERVER_ERROR
    String INTERNAL_ERROR = "An internal server error has occurred.";

}