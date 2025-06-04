package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.operation.PlanningDto;
import com.jorami.eyeapp.model.exam.Exam;
import com.jorami.eyeapp.model.operation.Planning;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-03T11:27:17+0200",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class PlanningMapperImpl implements PlanningMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_0159776256 = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );

    @Override
    public PlanningDto toDto(Planning entity) {
        if ( entity == null ) {
            return null;
        }

        PlanningDto planningDto = new PlanningDto();

        planningDto.setExamId( entityExamId( entity ) );
        if ( entity.getPlanningDate() != null ) {
            planningDto.setPlanningDate( dateTimeFormatter_yyyy_MM_dd_0159776256.format( entity.getPlanningDate() ) );
        }
        planningDto.setId( entity.getId() );
        planningDto.setEyeSide( entity.getEyeSide() );
        planningDto.setLocation( entity.getLocation() );
        planningDto.setSurgeon( entity.getSurgeon() );
        planningDto.setNotes( entity.getNotes() );

        return planningDto;
    }

    @Override
    public Planning toEntity(PlanningDto dto) {
        if ( dto == null ) {
            return null;
        }

        Planning planning = new Planning();

        if ( dto.getPlanningDate() != null ) {
            planning.setPlanningDate( LocalDate.parse( dto.getPlanningDate(), dateTimeFormatter_yyyy_MM_dd_0159776256 ) );
        }
        planning.setId( dto.getId() );
        planning.setEyeSide( dto.getEyeSide() );
        planning.setLocation( dto.getLocation() );
        planning.setSurgeon( dto.getSurgeon() );
        planning.setNotes( dto.getNotes() );

        return planning;
    }

    private Long entityExamId(Planning planning) {
        Exam exam = planning.getExam();
        if ( exam == null ) {
            return null;
        }
        return exam.getId();
    }
}
