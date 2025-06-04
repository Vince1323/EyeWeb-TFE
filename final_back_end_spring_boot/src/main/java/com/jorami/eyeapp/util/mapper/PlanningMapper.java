package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.operation.PlanningDto;
import com.jorami.eyeapp.model.operation.Planning;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlanningMapper {

    @Mapping(source = "exam.id", target = "examId")
    @Mapping(source = "planningDate", target = "planningDate", dateFormat = "yyyy-MM-dd")
    PlanningDto toDto(Planning entity);

    @Mapping(target = "exam", ignore = true)
    @Mapping(source = "planningDate", target = "planningDate", dateFormat = "yyyy-MM-dd")
    Planning toEntity(PlanningDto dto);
}
