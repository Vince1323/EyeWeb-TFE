package com.jorami.eyeapp.controller;

// === Importation du DTO qui contient les données du patient à retourner ===
import com.jorami.eyeapp.dto.patient.PatientDto;

// === Services ===
import com.jorami.eyeapp.service.OcrService;       // Service métier chargé de la reconnaissance optique (OCR)
import com.jorami.eyeapp.service.UserService;      // Utilisé ici pour vérifier les droits d’accès à une organisation

// === Lombok : génère un constructeur avec les champs final ===
import lombok.AllArgsConstructor;

// === Spring ===
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// === Enum contenant les types de biomètres autorisés ===
import com.jorami.eyeapp.model.Enum;

// === Java utilitaires ===
import java.util.List;
import java.util.Map;

// === Constantes de messages d’erreur utilisées dans les réponses ===
import static com.jorami.eyeapp.exception.ConstantMessage.*;

@CrossOrigin(
        origins = {"http://localhost:4200/", "https://eyewebapp.com/"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        maxAge = 3600L
)
// Active une transaction pour chaque requête traitée ici
@Transactional
// Déclare un contrôleur REST (réponses automatiquement encodées en JSON)
@RestController
// Préfixe commun pour toutes les routes : /ocr
@RequestMapping("/ocr")
// Génère automatiquement le constructeur avec les deux dépendances
@AllArgsConstructor
public class OcrController {

    private final OcrService ocrService;         // Service principal pour la reconnaissance de texte OCR
    private final UserService userService;       // Sert à vérifier les droits sur les organizations

    /**
     * Endpoint principal appelé quand on veut faire reconnaître un fichier biométrique.
     * Il prend en paramètres :
     * - un fichier (PDF ou image)
     * - un identifiant de biomètre (ex: ZEISS_IOLMASTER_700)
     * - un côté (OD/OS)
     * - une ou plusieurs ID d’organisations
     *
     * @return un PatientDto contenant les infos reconnues dans le fichier
     */
    @PostMapping("")
    public ResponseEntity<?> recognizeText(
            @RequestParam("file") MultipartFile file,
            @RequestParam("biometer") String biometer,
            @RequestParam("side") String side,
            @RequestParam("organizationsId") List<Long> organizationsId) {

        // 1. Vérifie que l'utilisateur a bien accès à cette ou ces organisations
        if (!userService.hasOrganizationsId(organizationsId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(JSON_KEY, UNAUTHORIZED_ORGANIZATION));
        }

        // 2. Vérifie que le fichier n’est pas vide
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(JSON_KEY, EMPTY_FILE));
        }

        // 3. Vérifie que le nom du biomètre a été fourni
        if (biometer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(JSON_KEY, DEVICE_NAME_UNSPECIFIED));
        }

        // 4. Tente de convertir le nom du biomètre en Enum (sinon erreur)
        Enum.Biometer biometerEnum = null;
        try {
            biometerEnum = Enum.Biometer.getKeyFromValue(biometer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(JSON_KEY, "This biometer does not exist."));
        }

        // 5. Vérifie que le biomètre est bien implémenté dans l'app
        if (biometerEnum != Enum.Biometer.OCULUS_PENTACAM_AXL &&
                biometerEnum != Enum.Biometer.ZEISS_IOLMASTER_500 &&
                biometerEnum != Enum.Biometer.ZEISS_IOLMASTER_700 &&
                biometerEnum != Enum.Biometer.MOVU_ALCON_ARGOS &&
                biometerEnum != Enum.Biometer.TOPCON_ALADDIN &&
                biometerEnum != Enum.Biometer.HEIDELBERG_ANTERION) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(JSON_KEY, "This biometer is not already implemented."));
        }

        // 6. Vérifie que le côté de l'œil est spécifié
        if (side.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(JSON_KEY, SIDE_EYE_UNSPECIFIED));
        }

        // 7. Lance la reconnaissance OCR et retourne les données du patient extraites
        PatientDto patientDto = ocrService.recognizeText(file, biometerEnum.getValue(), side, organizationsId);
        return ResponseEntity.status(HttpStatus.OK).body(patientDto);
    }

}
