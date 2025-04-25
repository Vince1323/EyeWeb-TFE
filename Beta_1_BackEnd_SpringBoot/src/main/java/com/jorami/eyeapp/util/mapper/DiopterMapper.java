package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.DiopterDto;
import com.jorami.eyeapp.model.Diopter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiopterMapper {

    DiopterMapper INSTANCE = Mappers.getMapper(DiopterMapper.class);

    Diopter toDiopter(DiopterDto diopterDto);
    DiopterDto toDiopterDto(Diopter diopter);

    List<Diopter> toDiopters(List<DiopterDto> diopterDtos);
    List<DiopterDto> toDiopterDtos(List<Diopter> diopters);

}
