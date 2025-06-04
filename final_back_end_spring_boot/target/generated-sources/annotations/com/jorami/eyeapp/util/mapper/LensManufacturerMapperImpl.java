package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.LensManufacturerDto;
import com.jorami.eyeapp.model.LensManufacturer;
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
public class LensManufacturerMapperImpl implements LensManufacturerMapper {

    @Override
    public LensManufacturer toLensManufacturer(LensManufacturerDto lensManufacturerDto) {
        if ( lensManufacturerDto == null ) {
            return null;
        }

        LensManufacturer.LensManufacturerBuilder<?, ?> lensManufacturer = LensManufacturer.builder();

        lensManufacturer.id( lensManufacturerDto.getId() );
        lensManufacturer.version( lensManufacturerDto.getVersion() );
        lensManufacturer.name( lensManufacturerDto.getName() );

        return lensManufacturer.build();
    }

    @Override
    public LensManufacturerDto toLensManufacturerDto(LensManufacturer lensManufacturer) {
        if ( lensManufacturer == null ) {
            return null;
        }

        LensManufacturerDto lensManufacturerDto = new LensManufacturerDto();

        if ( lensManufacturer.getId() != null ) {
            lensManufacturerDto.setId( lensManufacturer.getId() );
        }
        if ( lensManufacturer.getVersion() != null ) {
            lensManufacturerDto.setVersion( lensManufacturer.getVersion() );
        }
        lensManufacturerDto.setName( lensManufacturer.getName() );

        return lensManufacturerDto;
    }

    @Override
    public List<LensManufacturer> toLensManufacturers(List<LensManufacturerDto> lensManufacturerDtos) {
        if ( lensManufacturerDtos == null ) {
            return null;
        }

        List<LensManufacturer> list = new ArrayList<LensManufacturer>( lensManufacturerDtos.size() );
        for ( LensManufacturerDto lensManufacturerDto : lensManufacturerDtos ) {
            list.add( toLensManufacturer( lensManufacturerDto ) );
        }

        return list;
    }

    @Override
    public List<LensManufacturerDto> toLensManufacturerDtos(List<LensManufacturer> lensManufacturers) {
        if ( lensManufacturers == null ) {
            return null;
        }

        List<LensManufacturerDto> list = new ArrayList<LensManufacturerDto>( lensManufacturers.size() );
        for ( LensManufacturer lensManufacturer : lensManufacturers ) {
            list.add( toLensManufacturerDto( lensManufacturer ) );
        }

        return list;
    }
}
