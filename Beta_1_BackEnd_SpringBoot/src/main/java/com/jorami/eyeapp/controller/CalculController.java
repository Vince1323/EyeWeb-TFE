package com.jorami.eyeapp.controller;

import com.jorami.eyeapp.dto.calcul.CalculDto;
import com.jorami.eyeapp.dto.calcul.CalculSummaryDto;
import com.jorami.eyeapp.model.Enum;
import com.jorami.eyeapp.service.CalculService;
import com.jorami.eyeapp.util.mapper.CalculMapper;
import lombok.AllArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/calculs")
@AllArgsConstructor
@ControllerAdvice
public class CalculController {

    private final CalculService calculService;
    private CalculMapper mapper;


    @GetMapping("/{id}/organizations/{organizationsId}")
    public ResponseEntity<?> getCalculById(@PathVariable("id") Long id, @PathVariable("organizationsId") List<Long> organizationsId) {
        return ResponseEntity.ok(CalculMapper.convertCalculToCalculMatrixDto(mapper, calculService.getCalculById(id, organizationsId)));
    }

    @GetMapping("/patients/{patientId}/organizations/{organizationsId}")
    public ResponseEntity<?> getAllCalculsByPatientId(@PathVariable("patientId") Long patientId, @PathVariable("organizationsId") List<Long> organizationsId, @RequestParam("eyeSide") String eyeSide) {
        List<CalculSummaryDto> calculDtos = mapper.toCalculsSummaryDto(calculService.getAllCalculsByPatientIdAndEyeSide(patientId, Enum.EyeSide.valueOf(eyeSide), organizationsId));
        return ResponseEntity.ok(calculDtos);
    }

    @GetMapping("/exam/{examId}/organizations/{organizationsId}/prec/list")
    public ResponseEntity<?> getAllCalculsByExamIdEyeSidePowerTrue(@PathVariable("examId") Long examId, @PathVariable("organizationsId") List<Long> organizationsId, @RequestParam("eyeSide") String eyeSide) {
        List<CalculSummaryDto> calculDtos = mapper.toCalculsSummaryDto(calculService.getAllCalculsByExamIdEyeSidePowerTrue(examId, Enum.EyeSide.valueOf(eyeSide), organizationsId));
        return ResponseEntity.ok(calculDtos);
    }

    @PutMapping("/organizationsId/{organizationsId}/select-power")
    public ResponseEntity<?> selectPower(@RequestParam("idCalcul") Long idCalcul, @RequestParam("power") Float power, @PathVariable("organizationsId") List<Long> organizationsId) {
        calculService.updateSelectedDiopters(idCalcul, power, organizationsId);
        return ResponseEntity.ok(Map.of(JSON_KEY, UPDATED));
    }

    @PostMapping("/organizationsId/{organizationsId}")
    public ResponseEntity<?> injectionOnEachWebSite(@RequestBody List<CalculDto> calculsDto, @PathVariable("organizationsId") List<Long> organizationsId) {
        calculService.loopOnCalculs(mapper.toCalculs(calculsDto), organizationsId);
        return ResponseEntity.ok(Map.of(JSON_KEY, ADDED));
    }
}
