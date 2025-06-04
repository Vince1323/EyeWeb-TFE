package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.LensDto;
import com.jorami.eyeapp.model.Lens;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-03T11:27:17+0200",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class LensMapperImpl implements LensMapper {

    @Autowired
    private LensManufacturerMapper lensManufacturerMapper;

    @Override
    public Lens toLens(LensDto lensDto) {
        if ( lensDto == null ) {
            return null;
        }

        Lens.LensBuilder<?, ?> lens = Lens.builder();

        lens.id( lensDto.getId() );
        lens.version( lensDto.getVersion() );
        lens.name( lensDto.getName() );
        lens.commentTradeName( lensDto.getCommentTradeName() );
        lens.nominal( lensDto.getNominal() );
        lens.haigisA0( lensDto.getHaigisA0() );
        lens.haigisA1( lensDto.getHaigisA1() );
        lens.haigisA2( lensDto.getHaigisA2() );
        lens.hofferQPACD( lensDto.getHofferQPACD() );
        lens.holladay1SF( lensDto.getHolladay1SF() );
        lens.srkta( lensDto.getSrkta() );
        lens.haigisA0Optimized( lensDto.getHaigisA0Optimized() );
        lens.haigisA1Optimized( lensDto.getHaigisA1Optimized() );
        lens.haigisA2Optimized( lensDto.getHaigisA2Optimized() );
        lens.hofferQPACDOptimized( lensDto.getHofferQPACDOptimized() );
        lens.holladay1SFOptimized( lensDto.getHolladay1SFOptimized() );
        lens.cookeOptimized( lensDto.getCookeOptimized() );
        lens.srktaOptimized( lensDto.getSrktaOptimized() );
        lens.castropCOptimized( lensDto.getCastropCOptimized() );
        lens.castropHOptimized( lensDto.getCastropHOptimized() );
        lens.castropROptimized( lensDto.getCastropROptimized() );
        lens.lensManufacturer( lensManufacturerMapper.toLensManufacturer( lensDto.getLensManufacturer() ) );

        return lens.build();
    }

    @Override
    public LensDto toLensDto(Lens lens) {
        if ( lens == null ) {
            return null;
        }

        LensDto lensDto = new LensDto();

        if ( lens.getId() != null ) {
            lensDto.setId( lens.getId() );
        }
        if ( lens.getVersion() != null ) {
            lensDto.setVersion( lens.getVersion() );
        }
        lensDto.setName( lens.getName() );
        lensDto.setCommentTradeName( lens.getCommentTradeName() );
        lensDto.setNominal( lens.getNominal() );
        lensDto.setHaigisA0( lens.getHaigisA0() );
        lensDto.setHaigisA1( lens.getHaigisA1() );
        lensDto.setHaigisA2( lens.getHaigisA2() );
        lensDto.setHofferQPACD( lens.getHofferQPACD() );
        lensDto.setHolladay1SF( lens.getHolladay1SF() );
        lensDto.setSrkta( lens.getSrkta() );
        lensDto.setHaigisA0Optimized( lens.getHaigisA0Optimized() );
        lensDto.setHaigisA1Optimized( lens.getHaigisA1Optimized() );
        lensDto.setHaigisA2Optimized( lens.getHaigisA2Optimized() );
        lensDto.setHofferQPACDOptimized( lens.getHofferQPACDOptimized() );
        lensDto.setHolladay1SFOptimized( lens.getHolladay1SFOptimized() );
        lensDto.setCookeOptimized( lens.getCookeOptimized() );
        lensDto.setSrktaOptimized( lens.getSrktaOptimized() );
        lensDto.setCastropCOptimized( lens.getCastropCOptimized() );
        lensDto.setCastropHOptimized( lens.getCastropHOptimized() );
        lensDto.setCastropROptimized( lens.getCastropROptimized() );
        lensDto.setLensManufacturer( lensManufacturerMapper.toLensManufacturerDto( lens.getLensManufacturer() ) );

        return lensDto;
    }

    @Override
    public List<Lens> toLenses(List<LensDto> lensDtos) {
        if ( lensDtos == null ) {
            return null;
        }

        List<Lens> list = new ArrayList<Lens>( lensDtos.size() );
        for ( LensDto lensDto : lensDtos ) {
            list.add( toLens( lensDto ) );
        }

        return list;
    }

    @Override
    public List<LensDto> toLensDtos(List<Lens> lenses) {
        if ( lenses == null ) {
            return null;
        }

        List<LensDto> list = new ArrayList<LensDto>( lenses.size() );
        for ( Lens lens : lenses ) {
            list.add( toLensDto( lens ) );
        }

        return list;
    }
}
