package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.LensDto;
import com.jorami.eyeapp.model.Lens;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LensManufacturerMapper.class})
public interface LensMapper {

    LensMapper INSTANCE = Mappers.getMapper(LensMapper.class);

    Lens toLens(LensDto lensDto);
    LensDto toLensDto(Lens lens);

    List<Lens> toLenses(List<LensDto> lensDtos);
    List<LensDto> toLensDtos(List<Lens> lenses);
}
