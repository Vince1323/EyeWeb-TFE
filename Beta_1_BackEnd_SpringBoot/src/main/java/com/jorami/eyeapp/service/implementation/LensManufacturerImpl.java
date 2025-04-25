package com.jorami.eyeapp.service.implementation;

import com.jorami.eyeapp.model.LensManufacturer;
import com.jorami.eyeapp.repository.LensManufacturerRepository;
import com.jorami.eyeapp.service.LensManufacturerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static com.jorami.eyeapp.exception.ConstantMessage.ITEM_NOT_FOUND;

@Service
@AllArgsConstructor
public class LensManufacturerImpl implements LensManufacturerService {

    private final LensManufacturerRepository lensManufacturerRepository;


    @Override
    public List<LensManufacturer> getAllLensManufacturers() {
        List<LensManufacturer> lensManufacturers = lensManufacturerRepository.findAllLensManufacturers();
        if(lensManufacturers.isEmpty()) {
            throw new NoSuchElementException(ITEM_NOT_FOUND);
        }
        return lensManufacturers;
    }

}
