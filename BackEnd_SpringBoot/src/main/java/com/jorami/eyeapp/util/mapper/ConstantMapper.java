package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.ConstantDto;
import com.jorami.eyeapp.model.Constant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConstantMapper {

    ConstantMapper INSTANCE = Mappers.getMapper(ConstantMapper.class);

    Constant toConstant(ConstantDto constantDto);
    ConstantDto toConstantDto(Constant constant);

    List<Constant> toConstants(List<ConstantDto> constantDtos);
    List<ConstantDto> toConstantDtos(List<Constant> constants);

}
