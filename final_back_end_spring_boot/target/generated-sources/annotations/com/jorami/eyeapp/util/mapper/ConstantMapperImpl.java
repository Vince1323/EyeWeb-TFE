package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.ConstantDto;
import com.jorami.eyeapp.model.Constant;
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
public class ConstantMapperImpl implements ConstantMapper {

    @Override
    public Constant toConstant(ConstantDto constantDto) {
        if ( constantDto == null ) {
            return null;
        }

        Constant.ConstantBuilder<?, ?> constant = Constant.builder();

        constant.id( constantDto.getId() );
        constant.version( constantDto.getVersion() );
        constant.constantType( constantDto.getConstantType() );
        constant.value( constantDto.getValue() );
        constant.formula( constantDto.getFormula() );

        return constant.build();
    }

    @Override
    public ConstantDto toConstantDto(Constant constant) {
        if ( constant == null ) {
            return null;
        }

        ConstantDto constantDto = new ConstantDto();

        if ( constant.getId() != null ) {
            constantDto.setId( constant.getId() );
        }
        if ( constant.getVersion() != null ) {
            constantDto.setVersion( constant.getVersion() );
        }
        constantDto.setConstantType( constant.getConstantType() );
        constantDto.setValue( constant.getValue() );
        constantDto.setFormula( constant.getFormula() );

        return constantDto;
    }

    @Override
    public List<Constant> toConstants(List<ConstantDto> constantDtos) {
        if ( constantDtos == null ) {
            return null;
        }

        List<Constant> list = new ArrayList<Constant>( constantDtos.size() );
        for ( ConstantDto constantDto : constantDtos ) {
            list.add( toConstant( constantDto ) );
        }

        return list;
    }

    @Override
    public List<ConstantDto> toConstantDtos(List<Constant> constants) {
        if ( constants == null ) {
            return null;
        }

        List<ConstantDto> list = new ArrayList<ConstantDto>( constants.size() );
        for ( Constant constant : constants ) {
            list.add( toConstantDto( constant ) );
        }

        return list;
    }
}
