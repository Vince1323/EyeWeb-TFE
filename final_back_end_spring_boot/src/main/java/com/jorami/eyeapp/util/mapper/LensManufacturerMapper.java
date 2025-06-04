package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.LensManufacturerDto;
import com.jorami.eyeapp.model.LensManufacturer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LensManufacturerMapper {

    LensManufacturerMapper INSTANCE = Mappers.getMapper(LensManufacturerMapper.class);

    LensManufacturer toLensManufacturer(LensManufacturerDto lensManufacturerDto);
    LensManufacturerDto toLensManufacturerDto(LensManufacturer lensManufacturer);

    List<LensManufacturer> toLensManufacturers(List<LensManufacturerDto> lensManufacturerDtos);
    List<LensManufacturerDto> toLensManufacturerDtos(List<LensManufacturer> lensManufacturers);
}
