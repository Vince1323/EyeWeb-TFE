package com.jorami.eyeapp.controller;

import com.jorami.eyeapp.dto.patient.PatientDto;
import com.jorami.eyeapp.service.OcrService;
import com.jorami.eyeapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.jorami.eyeapp.model.Enum;

import java.util.List;
import java.util.Map;

import static com.jorami.eyeapp.exception.ConstantMessage.*;

@CrossOrigin(
        origins = {"http://localhost:4200/", "https://eyewebapp.com/"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        maxAge = 3600L
)
@Transactional
@RestController
@RequestMapping("/ocr")
@AllArgsConstructor
public class OcrController {

    private final OcrService ocrService;
    private final UserService userService;


    //POST
    @PostMapping("")
    public ResponseEntity<?> recognizeText(@RequestParam("file") MultipartFile file, @RequestParam("biometer") String biometer, @RequestParam("side") String side, @RequestParam("organizationsId") List<Long> organizationsId) {
        if(!userService.hasOrganizationsId(organizationsId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(JSON_KEY, UNAUTHORIZED_ORGANIZATION));
        }
        if(file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(JSON_KEY, EMPTY_FILE));
        }
        if(biometer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(JSON_KEY, DEVICE_NAME_UNSPECIFIED));
        }
        Enum.Biometer biometerEnum = null;
        try {
            biometerEnum = Enum.Biometer.getKeyFromValue(biometer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(JSON_KEY, "This biometer does not exist."));
        }

        if(biometerEnum != Enum.Biometer.OCULUS_PENTACAM_AXL && biometerEnum != Enum.Biometer.ZEISS_IOLMASTER_500 &&
                biometerEnum != Enum.Biometer.ZEISS_IOLMASTER_700 && biometerEnum != Enum.Biometer.MOVU_ALCON_ARGOS &&
                biometerEnum != Enum.Biometer.TOPCON_ALADDIN && biometerEnum != Enum.Biometer.HEIDELBERG_ANTERION) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(JSON_KEY, "This biometer is not already implemented."));
        }
        if(side.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(JSON_KEY, SIDE_EYE_UNSPECIFIED));
        }

        PatientDto patientDto = ocrService.recognizeText(file, biometerEnum.getValue(), side, organizationsId);
        return ResponseEntity.status(HttpStatus.OK).body(patientDto);
    }

}