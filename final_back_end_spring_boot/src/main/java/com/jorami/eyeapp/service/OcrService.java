package com.jorami.eyeapp.service;

import com.jorami.eyeapp.dto.patient.PatientDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OcrService {

    String getStringFromOCR(MultipartFile file);
    PatientDto recognizeText(MultipartFile file, String biometer, String side, List<Long> organizationsId);

}
