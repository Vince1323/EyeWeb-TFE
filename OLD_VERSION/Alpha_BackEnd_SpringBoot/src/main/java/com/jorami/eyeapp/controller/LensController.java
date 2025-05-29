package com.jorami.eyeapp.controller;

import com.jorami.eyeapp.dto.LensDto;
import com.jorami.eyeapp.service.LensService;
import com.jorami.eyeapp.util.mapper.LensMapper;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
        origins = {"http://localhost:4200/", "https://eyewebapp.com/"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        maxAge = 3600L
)
@Transactional
@RestController
@RequestMapping("/lenses")
@AllArgsConstructor
public class LensController {

    private final LensService lensService;
    private final LensMapper lensMapper;


    /**
     * returns all lenses from a manufacturer by using his id
     * @param lensManufacturerId
     * @return
     */
    @GetMapping("/manufacturers/{id}")
    public List<LensDto> getLensesByManufacturerId(@PathVariable("id") Long lensManufacturerId) {
        return lensMapper.toLensDtos(lensService.getLensesByLensManufacturer(lensManufacturerId));
    }

    @PostMapping
    public void importLensesFromWebSite() {
        lensService.importLensesFromWebSite();
    }

}
