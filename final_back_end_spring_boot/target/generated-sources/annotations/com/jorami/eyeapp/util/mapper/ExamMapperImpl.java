package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.exam.ExamDto;
import com.jorami.eyeapp.dto.exam.ExamSummaryDto;
import com.jorami.eyeapp.model.exam.Exam;
import com.jorami.eyeapp.model.exam.ExamForUpdate;
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
public class ExamMapperImpl implements ExamMapper {

    @Override
    public Exam toExam(ExamDto examDto) {
        if ( examDto == null ) {
            return null;
        }

        Exam.ExamBuilder<?, ?> exam = Exam.builder();

        exam.id( examDto.getId() );
        exam.version( examDto.getVersion() );
        exam.examDate( ExamMapper.stringToLocalDateTime( examDto.getExamDate() ) );
        exam.calculDate( ExamMapper.stringToLocalDateTime( examDto.getCalculDate() ) );
        exam.manifestRefrDate( ExamMapper.stringToLocalDateTime( examDto.getManifestRefrDate() ) );
        exam.machine( ExamMapper.machineToEnum( examDto.getMachine() ) );
        exam.eyeSide( examDto.getEyeSide() );
        exam.examType( examDto.getExamType() );
        exam.examComment( examDto.getExamComment() );
        exam.examQuality( examDto.getExamQuality() );
        exam.eyeStatus( examDto.getEyeStatus() );
        exam.selected( examDto.isSelected() );
        exam.al( examDto.getAl() );
        exam.acd( examDto.getAcd() );
        exam.internalAcd( examDto.getInternalAcd() );
        exam.pupilDia( examDto.getPupilDia() );
        exam.pupilMin( examDto.getPupilMin() );
        exam.pupilMax( examDto.getPupilMax() );
        exam.wtw( examDto.getWtw() );
        exam.cord( examDto.getCord() );
        exam.z40( examDto.getZ40() );
        exam.hoa( examDto.getHoa() );
        exam.kappaAngle( examDto.getKappaAngle() );
        exam.n( examDto.getN() );
        exam.k1( examDto.getK1() );
        exam.k1Mm( examDto.getK1Mm() );
        exam.k1Axis( examDto.getK1Axis() );
        exam.k2( examDto.getK2() );
        exam.k2Mm( examDto.getK2Mm() );
        exam.k2Axis( examDto.getK2Axis() );
        exam.k_astig( examDto.getK_astig() );
        exam.k_astigAxis( examDto.getK_astigAxis() );
        exam.k_avg( examDto.getK_avg() );
        exam.r_avg( examDto.getR_avg() );
        exam.k1CorneaBack( examDto.getK1CorneaBack() );
        exam.k2CorneaBack( examDto.getK2CorneaBack() );
        exam.k1AxisCorneaBack( examDto.getK1AxisCorneaBack() );
        exam.k2AxisCorneaBack( examDto.getK2AxisCorneaBack() );
        exam.siaCyl( examDto.getSiaCyl() );
        exam.siaAxis( examDto.getSiaAxis() );
        exam.snr( examDto.getSnr() );
        exam.lensThickness( examDto.getLensThickness() );
        exam.cct( examDto.getCct() );
        exam.cctMin( examDto.getCctMin() );
        exam.asphQf( examDto.getAsphQf() );
        exam.asphQb( examDto.getAsphQb() );
        exam.alStatus( examDto.getAlStatus() );
        exam.alError( examDto.getAlError() );
        exam.k_preRefrAvg( examDto.getK_preRefrAvg() );
        exam.r_preRefrAvg( examDto.getR_preRefrAvg() );
        exam.manifestRefrSph( examDto.getManifestRefrSph() );
        exam.manifestRefrCyl( examDto.getManifestRefrCyl() );
        exam.manifestRefrAxis( examDto.getManifestRefrAxis() );
        exam.manifestRefrVd( examDto.getManifestRefrVd() );
        exam.targetRefrSph( examDto.getTargetRefrSph() );
        exam.targetRefrCyl( examDto.getTargetRefrCyl() );
        exam.importType( examDto.getImportType() );

        return exam.build();
    }

    @Override
    public ExamDto toExamDto(Exam exam) {
        if ( exam == null ) {
            return null;
        }

        ExamDto examDto = new ExamDto();

        examDto.setExamDate( ExamMapper.localDateToString( exam.getExamDate() ) );
        examDto.setCalculDate( ExamMapper.localDateToString( exam.getCalculDate() ) );
        examDto.setManifestRefrDate( ExamMapper.localDateToString( exam.getManifestRefrDate() ) );
        examDto.setMachine( ExamMapper.enumToMachine( exam.getMachine() ) );
        examDto.setPatientId( ExamMapper.getPatientIdFromLinks( exam ) );
        examDto.setId( exam.getId() );
        examDto.setVersion( exam.getVersion() );
        examDto.setEyeSide( exam.getEyeSide() );
        examDto.setExamType( exam.getExamType() );
        examDto.setExamComment( exam.getExamComment() );
        examDto.setExamQuality( exam.getExamQuality() );
        examDto.setEyeStatus( exam.getEyeStatus() );
        examDto.setSelected( exam.isSelected() );
        examDto.setAl( exam.getAl() );
        examDto.setAcd( exam.getAcd() );
        examDto.setInternalAcd( exam.getInternalAcd() );
        examDto.setPupilDia( exam.getPupilDia() );
        examDto.setPupilMin( exam.getPupilMin() );
        examDto.setPupilMax( exam.getPupilMax() );
        examDto.setWtw( exam.getWtw() );
        examDto.setCord( exam.getCord() );
        examDto.setZ40( exam.getZ40() );
        examDto.setHoa( exam.getHoa() );
        examDto.setKappaAngle( exam.getKappaAngle() );
        examDto.setN( exam.getN() );
        examDto.setK1( exam.getK1() );
        examDto.setK1Mm( exam.getK1Mm() );
        examDto.setK1Axis( exam.getK1Axis() );
        examDto.setK2( exam.getK2() );
        examDto.setK2Mm( exam.getK2Mm() );
        examDto.setK2Axis( exam.getK2Axis() );
        examDto.setK_astig( exam.getK_astig() );
        examDto.setK_astigAxis( exam.getK_astigAxis() );
        examDto.setK_avg( exam.getK_avg() );
        examDto.setR_avg( exam.getR_avg() );
        examDto.setK1CorneaBack( exam.getK1CorneaBack() );
        examDto.setK2CorneaBack( exam.getK2CorneaBack() );
        examDto.setK1AxisCorneaBack( exam.getK1AxisCorneaBack() );
        examDto.setK2AxisCorneaBack( exam.getK2AxisCorneaBack() );
        examDto.setSiaCyl( exam.getSiaCyl() );
        examDto.setSiaAxis( exam.getSiaAxis() );
        examDto.setSnr( exam.getSnr() );
        examDto.setLensThickness( exam.getLensThickness() );
        examDto.setCct( exam.getCct() );
        examDto.setCctMin( exam.getCctMin() );
        examDto.setAsphQf( exam.getAsphQf() );
        examDto.setAsphQb( exam.getAsphQb() );
        examDto.setAlStatus( exam.getAlStatus() );
        examDto.setAlError( exam.getAlError() );
        examDto.setK_preRefrAvg( exam.getK_preRefrAvg() );
        examDto.setR_preRefrAvg( exam.getR_preRefrAvg() );
        examDto.setManifestRefrSph( exam.getManifestRefrSph() );
        examDto.setManifestRefrCyl( exam.getManifestRefrCyl() );
        examDto.setManifestRefrAxis( exam.getManifestRefrAxis() );
        examDto.setManifestRefrVd( exam.getManifestRefrVd() );
        examDto.setTargetRefrSph( exam.getTargetRefrSph() );
        examDto.setTargetRefrCyl( exam.getTargetRefrCyl() );
        examDto.setImportType( exam.getImportType() );

        return examDto;
    }

    @Override
    public ExamSummaryDto toExamSummaryDto(Exam exam) {
        if ( exam == null ) {
            return null;
        }

        ExamSummaryDto examSummaryDto = new ExamSummaryDto();

        examSummaryDto.setCalculDate( ExamMapper.localDateToString( exam.getCalculDate() ) );
        examSummaryDto.setId( exam.getId() );
        examSummaryDto.setVersion( exam.getVersion() );

        return examSummaryDto;
    }

    @Override
    public ExamForUpdate toExamForUpdate(Exam exam) {
        if ( exam == null ) {
            return null;
        }

        ExamForUpdate examForUpdate = new ExamForUpdate();

        examForUpdate.setAl( exam.getAl() );
        examForUpdate.setAcd( exam.getAcd() );
        examForUpdate.setPupilDia( exam.getPupilDia() );
        examForUpdate.setWtw( exam.getWtw() );
        examForUpdate.setCord( exam.getCord() );
        examForUpdate.setZ40( exam.getZ40() );
        examForUpdate.setCct( exam.getCct() );
        examForUpdate.setK1( exam.getK1() );
        examForUpdate.setK1Axis( exam.getK1Axis() );
        examForUpdate.setK2( exam.getK2() );
        examForUpdate.setK2Axis( exam.getK2Axis() );
        examForUpdate.setK_astig( exam.getK_astig() );
        examForUpdate.setK_avg( exam.getK_avg() );
        examForUpdate.setLensThickness( exam.getLensThickness() );
        examForUpdate.setSnr( exam.getSnr() );
        examForUpdate.setMachine( exam.getMachine() );

        return examForUpdate;
    }

    @Override
    public List<Exam> toExams(List<ExamDto> examDtos) {
        if ( examDtos == null ) {
            return null;
        }

        List<Exam> list = new ArrayList<Exam>( examDtos.size() );
        for ( ExamDto examDto : examDtos ) {
            list.add( toExam( examDto ) );
        }

        return list;
    }

    @Override
    public List<ExamDto> toExamDtos(List<Exam> exams) {
        if ( exams == null ) {
            return null;
        }

        List<ExamDto> list = new ArrayList<ExamDto>( exams.size() );
        for ( Exam exam : exams ) {
            list.add( toExamDto( exam ) );
        }

        return list;
    }

    @Override
    public List<ExamSummaryDto> toExamSummaryDtos(List<Exam> exams) {
        if ( exams == null ) {
            return null;
        }

        List<ExamSummaryDto> list = new ArrayList<ExamSummaryDto>( exams.size() );
        for ( Exam exam : exams ) {
            list.add( toExamSummaryDto( exam ) );
        }

        return list;
    }
}
