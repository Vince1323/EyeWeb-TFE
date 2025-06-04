package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.DiopterDto;
import com.jorami.eyeapp.model.Diopter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-03T11:27:17+0200",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class DiopterMapperImpl implements DiopterMapper {

    @Override
    public Diopter toDiopter(DiopterDto diopterDto) {
        if ( diopterDto == null ) {
            return null;
        }

        Diopter.DiopterBuilder<?, ?> diopter = Diopter.builder();

        diopter.id( diopterDto.getId() );
        diopter.version( diopterDto.getVersion() );
        diopter.iolPower( diopterDto.getIolPower() );
        diopter.value( diopterDto.getValue() );
        diopter.formula( diopterDto.getFormula() );

        return diopter.build();
    }

    @Override
    public DiopterDto toDiopterDto(Diopter diopter) {
        if ( diopter == null ) {
            return null;
        }

        DiopterDto diopterDto = new DiopterDto();

        if ( diopter.getId() != null ) {
            diopterDto.setId( diopter.getId() );
        }
        if ( diopter.getVersion() != null ) {
            diopterDto.setVersion( diopter.getVersion() );
        }
        diopterDto.setIolPower( diopter.getIolPower() );
        diopterDto.setValue( diopter.getValue() );
        diopterDto.setFormula( diopter.getFormula() );

        return diopterDto;
    }

    @Override
    public List<Diopter> toDiopters(List<DiopterDto> diopterDtos) {
        if ( diopterDtos == null ) {
            return null;
        }

        List<Diopter> list = new ArrayList<Diopter>( diopterDtos.size() );
        for ( DiopterDto diopterDto : diopterDtos ) {
            list.add( toDiopter( diopterDto ) );
        }

        return list;
    }

    @Override
    public List<DiopterDto> toDiopterDtos(List<Diopter> diopters) {
        if ( diopters == null ) {
            return null;
        }

        List<DiopterDto> list = new ArrayList<DiopterDto>( diopters.size() );
        for ( Diopter diopter : diopters ) {
            list.add( toDiopterDto( diopter ) );
        }

        return list;
    }
}
