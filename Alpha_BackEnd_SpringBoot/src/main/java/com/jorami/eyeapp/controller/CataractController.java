package com.jorami.eyeapp.controller;

import com.jorami.eyeapp.dto.exam.ExamDto;
import com.jorami.eyeapp.dto.exam.ExamSummaryDto;
import com.jorami.eyeapp.service.CataractService;
import com.jorami.eyeapp.util.mapper.ExamMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import com.jorami.eyeapp.model.Enum;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
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
@RequestMapping("/cataract")
@AllArgsConstructor
@ControllerAdvice
public class CataractController {

    private final CataractService cataractService;
    private final ExamMapper mapper;


    // Endpoint modifié !
    @GetMapping("/{id}/organizations/{organizationsId}")
    public ResponseEntity<?> getExamById(@PathVariable("id") Long examId, @PathVariable("organizationsId") List<Long> organizationsId) {
        ExamDto biometryDto = mapper.toExamDto(cataractService.getExamById(examId, organizationsId));
        return ResponseEntity.ok(biometryDto);
    }

    @GetMapping("/patients/{patientId}/organizations/{organizationsId}")
    public ResponseEntity<?> getAllExamsByPatientId(@PathVariable("patientId") Long patientId, @PathVariable("organizationsId") List<Long> organizationsId, @RequestParam("eyeSide") String eyeSide, @RequestParam(name = "isSelected", required = false) Boolean isSelected) {
        List<ExamDto> biometryDtos = mapper.toExamDtos(cataractService.getAllBiometriesByPatientIdAndEyeSide(patientId, Enum.EyeSide.valueOf(eyeSide), isSelected, organizationsId));
        return ResponseEntity.ok(biometryDtos);
    }

    @GetMapping("/patients/{patientId}/organizations/{organizationsId}/lists")
    public ResponseEntity<?> getAllExamsListByPatientId(@PathVariable("patientId") Long patientId, @PathVariable("organizationsId") List<Long> organizationsId, @RequestParam("eyeSide") String eyeSide, @RequestParam(name = "isSelected", required = false) Boolean isSelected) {
        List<ExamSummaryDto> biometryDtos = mapper.toExamSummaryDtos(cataractService.getAllBiometriesByPatientIdAndEyeSide(patientId, Enum.EyeSide.valueOf(eyeSide), isSelected, organizationsId));
        return ResponseEntity.ok(biometryDtos);
    }

    @GetMapping("/patients/{patientId}/organizations/{organizationsId}/prec/lists")
    public ResponseEntity<?> getAllBiometriesByPatientIdEyeSideCalculPowerTrue(@PathVariable("patientId") Long patientId, @PathVariable("organizationsId") List<Long> organizationsId, @RequestParam("eyeSide") String eyeSide, @RequestParam(name = "isSelected", required = false) Boolean isSelected) {
        List<ExamSummaryDto> biometryDtos = mapper.toExamSummaryDtos(cataractService.getAllBiometriesByPatientIdEyeSideCalculPowerTrue(patientId, Enum.EyeSide.valueOf(eyeSide), isSelected, organizationsId));
        return ResponseEntity.ok(biometryDtos);
    }

    @GetMapping(value = "/import/logs", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamLogs(@RequestHeader HttpHeaders headers) {
        String authorizationHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        return cataractService.streamLogs(authorizationHeader);
    }

    @PostMapping("/organizations/{organizationsId}")
    public ResponseEntity<?> addExam(@Valid @RequestBody ExamDto examDto, @PathVariable("organizationsId") List<Long> organizationsId) {
        cataractService.checkIfExist(mapper.toExam(examDto), examDto.getPatientId(), organizationsId);
        ExamDto biometryAddDto = mapper.toExamDto(cataractService.addExam(mapper.toExam(examDto), examDto.getPatientId(), organizationsId));
        return ResponseEntity.ok(biometryAddDto);
    }

    @PostMapping("/add/{patientId}/organizations/{organizationsId}")
    public ResponseEntity<?> addExams(@Valid @RequestBody List<ExamDto> examDtos,
                                      @PathVariable("patientId") Long patientId,
                                      @PathVariable("organizationsId") List<Long> organizationsId) {
        try {
            cataractService.loopAddExams(mapper.toExams(examDtos), patientId, organizationsId);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            e.printStackTrace(); // ou mieux : log.error("Erreur lors de l'ajout des examens", e);
            return ResponseEntity.internalServerError().body("Erreur lors de l'ajout des examens : " + e.getMessage());
        }
    }

    @PostMapping("/imports")
    public ResponseEntity<?> importBiometries(@RequestHeader HttpHeaders headers, @RequestParam("file") MultipartFile file, @RequestParam("biometer") String biometer, @RequestParam("organizationsId") List<Long> organizationsId) throws IOException, InterruptedException {
        String authorizationHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        cataractService.importBiometries(authorizationHeader, file, biometer, organizationsId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/patients/{patientId}/organizations/{organizationsId}/average")
    public ResponseEntity<?> calculAverage(@PathVariable("patientId")Long patientId, @PathVariable("organizationsId") List<Long> organizationId, @Valid @RequestBody List<ExamDto> examsDto) {
        ExamDto biometryDto = mapper.toExamDto(cataractService.calculAverage(patientId, mapper.toExams(examsDto), organizationId));
        return ResponseEntity.ok(biometryDto);
    }

    // Endpoint modifié !
    @PutMapping("/organizations/{organizationsId}/update")
    public ResponseEntity<?> editExam(@Valid @RequestBody ExamDto examDto, @PathVariable("organizationsId") List<Long> orgnizationsId) {
        ExamDto editedExam = mapper.toExamDto(cataractService.editExam(mapper.toExam(examDto), orgnizationsId));
        return ResponseEntity.ok(editedExam);
    }

    @PutMapping("/organizations/{organizationsId}/select")
    public ResponseEntity<?> editSelectExam(@Valid @RequestBody ExamDto examDto, @PathVariable("organizationsId") List<Long> orgnizationsId) {
        ExamDto editedExam = mapper.toExamDto(cataractService.editSelectedExam(mapper.toExam(examDto), orgnizationsId));
        return ResponseEntity.ok(editedExam);
    }

    // Endpoint modifié !
    @DeleteMapping("/{id}/organizations/{organizationsId}/delete")
    public ResponseEntity<?> deleteExam(@PathVariable("id") Long examId, @PathVariable("organizationsId") List<Long> orgnizationsId) {
        cataractService.deleteExam(examId, orgnizationsId);
        return ResponseEntity.ok(Map.of(JSON_KEY, DELETE_OK));
    }

}
