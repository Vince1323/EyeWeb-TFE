package com.jorami.eyeapp.controller;

import com.jorami.eyeapp.service.LensManufacturerService;
import com.jorami.eyeapp.util.mapper.LensManufacturerMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(
        origins = {"http://localhost:4200/", "https://eyewebapp.com/"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        maxAge = 3600L
)
@Transactional
@RestController
@RequestMapping("/lens-manufacturers")
@AllArgsConstructor
@ControllerAdvice
public class LensManufacturerController {

    private final LensManufacturerService lensManufacturer;
    private final LensManufacturerMapper mapper;


    @GetMapping
    public ResponseEntity<?> getAllLensManufacturers() {
        return ResponseEntity.ok().body(mapper.toLensManufacturerDtos(lensManufacturer.getAllLensManufacturers()));
    }

}
