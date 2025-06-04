package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.calcul.CalculDto;
import com.jorami.eyeapp.dto.calcul.CalculMatrixDto;
import com.jorami.eyeapp.dto.calcul.CalculSummaryDto;
import com.jorami.eyeapp.model.Calcul;
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
public class CalculMapperImpl implements CalculMapper {

    @Autowired
    private LensMapper lensMapper;
    @Autowired
    private DiopterMapper diopterMapper;
    @Autowired
    private ConstantMapper constantMapper;
    @Autowired
    private ExamMapper examMapper;

    @Override
    public Calcul toCalcul(CalculDto calculDto) {
        if ( calculDto == null ) {
            return null;
        }

        Calcul.CalculBuilder<?, ?> calcul = Calcul.builder();

        calcul.eyeSide( CalculMapper.stringToEyeSide( calculDto.getEyeSide() ) );
        calcul.targetRefraction( calculDto.getTargetRefraction() );
        calcul.isSecondEye( calculDto.getIsSecondEye() );
        calcul.precPowerSelected( calculDto.getPrecPowerSelected() );
        calcul.se( calculDto.getSe() );
        calcul.idReferencePrecExam( calculDto.getIdReferencePrecExam() );
        calcul.idReferencePrecCalcul( calculDto.getIdReferencePrecCalcul() );
        calcul.exam( examMapper.toExam( calculDto.getExam() ) );
        calcul.lens( lensMapper.toLens( calculDto.getLens() ) );
        calcul.constants( constantMapper.toConstants( calculDto.getConstants() ) );
        calcul.diopters( diopterMapper.toDiopters( calculDto.getDiopters() ) );

        return calcul.build();
    }

    @Override
    public CalculDto toCalculDto(Calcul calcul) {
        if ( calcul == null ) {
            return null;
        }

        CalculDto calculDto = new CalculDto();

        calculDto.setEyeSide( CalculMapper.eyeSideToString( calcul.getEyeSide() ) );
        calculDto.setExam( examMapper.toExamDto( calcul.getExam() ) );
        calculDto.setLens( lensMapper.toLensDto( calcul.getLens() ) );
        calculDto.setConstants( constantMapper.toConstantDtos( calcul.getConstants() ) );
        calculDto.setDiopters( diopterMapper.toDiopterDtos( calcul.getDiopters() ) );
        calculDto.setTargetRefraction( calcul.getTargetRefraction() );
        calculDto.setIsSecondEye( calcul.getIsSecondEye() );
        calculDto.setPrecPowerSelected( calcul.getPrecPowerSelected() );
        calculDto.setSe( calcul.getSe() );
        calculDto.setIdReferencePrecExam( calcul.getIdReferencePrecExam() );
        calculDto.setIdReferencePrecCalcul( calcul.getIdReferencePrecCalcul() );

        return calculDto;
    }

    @Override
    public Calcul toCalcul(CalculMatrixDto calculMatrixDto) {
        if ( calculMatrixDto == null ) {
            return null;
        }

        Calcul.CalculBuilder<?, ?> calcul = Calcul.builder();

        calcul.eyeSide( CalculMapper.stringToEyeSide( calculMatrixDto.getEyeSide() ) );
        calcul.id( calculMatrixDto.getId() );
        calcul.targetRefraction( calculMatrixDto.getTargetRefraction() );
        calcul.isSecondEye( calculMatrixDto.getIsSecondEye() );
        calcul.precPowerSelected( calculMatrixDto.getPrecPowerSelected() );
        calcul.se( calculMatrixDto.getSe() );
        calcul.idReferencePrecExam( calculMatrixDto.getIdReferencePrecExam() );
        calcul.idReferencePrecCalcul( calculMatrixDto.getIdReferencePrecCalcul() );
        calcul.exam( examMapper.toExam( calculMatrixDto.getExam() ) );
        calcul.lens( lensMapper.toLens( calculMatrixDto.getLens() ) );
        calcul.constants( constantMapper.toConstants( calculMatrixDto.getConstants() ) );

        return calcul.build();
    }

    @Override
    public CalculMatrixDto toCalculMatrixDto(Calcul calcul) {
        if ( calcul == null ) {
            return null;
        }

        CalculMatrixDto calculMatrixDto = new CalculMatrixDto();

        calculMatrixDto.setEyeSide( CalculMapper.eyeSideToString( calcul.getEyeSide() ) );
        calculMatrixDto.setId( calcul.getId() );
        calculMatrixDto.setExam( examMapper.toExamDto( calcul.getExam() ) );
        calculMatrixDto.setLens( lensMapper.toLensDto( calcul.getLens() ) );
        calculMatrixDto.setConstants( constantMapper.toConstantDtos( calcul.getConstants() ) );
        calculMatrixDto.setTargetRefraction( calcul.getTargetRefraction() );
        calculMatrixDto.setIsSecondEye( calcul.getIsSecondEye() );
        calculMatrixDto.setPrecPowerSelected( calcul.getPrecPowerSelected() );
        calculMatrixDto.setSe( calcul.getSe() );
        calculMatrixDto.setIdReferencePrecExam( calcul.getIdReferencePrecExam() );
        calculMatrixDto.setIdReferencePrecCalcul( calcul.getIdReferencePrecCalcul() );

        return calculMatrixDto;
    }

    @Override
    public CalculSummaryDto toCalculSummaryDto(Calcul calcul) {
        if ( calcul == null ) {
            return null;
        }

        CalculSummaryDto calculSummaryDto = new CalculSummaryDto();

        if ( calcul.getId() != null ) {
            calculSummaryDto.setId( calcul.getId() );
        }
        calculSummaryDto.setCreatedAt( calcul.getCreatedAt() );
        if ( calcul.getVersion() != null ) {
            calculSummaryDto.setVersion( calcul.getVersion() );
        }

        return calculSummaryDto;
    }

    @Override
    public List<Calcul> toCalculs(List<CalculDto> calculDtos) {
        if ( calculDtos == null ) {
            return null;
        }

        List<Calcul> list = new ArrayList<Calcul>( calculDtos.size() );
        for ( CalculDto calculDto : calculDtos ) {
            list.add( toCalcul( calculDto ) );
        }

        return list;
    }

    @Override
    public List<CalculDto> toCalculDtos(List<Calcul> calculs) {
        if ( calculs == null ) {
            return null;
        }

        List<CalculDto> list = new ArrayList<CalculDto>( calculs.size() );
        for ( Calcul calcul : calculs ) {
            list.add( toCalculDto( calcul ) );
        }

        return list;
    }

    @Override
    public List<CalculSummaryDto> toCalculsSummaryDto(List<Calcul> calculs) {
        if ( calculs == null ) {
            return null;
        }

        List<CalculSummaryDto> list = new ArrayList<CalculSummaryDto>( calculs.size() );
        for ( Calcul calcul : calculs ) {
            list.add( toCalculSummaryDto( calcul ) );
        }

        return list;
    }
}
