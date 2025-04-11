package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.exam.ExamSummaryDto;
import com.jorami.eyeapp.model.Enum;
import com.jorami.eyeapp.model.exam.Exam;
import com.jorami.eyeapp.dto.exam.ExamDto;
import com.jorami.eyeapp.model.exam.ExamForUpdate;
import com.jorami.eyeapp.util.DateFunction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamMapper {

    ExamMapper INSTANCE = Mappers.getMapper(ExamMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "version", target = "version")
    @Mapping(source = "examDate", target = "examDate", qualifiedByName = "stringToLocalDateTime")
    @Mapping(source = "calculDate", target = "calculDate", qualifiedByName = "stringToLocalDateTime")
    @Mapping(source = "manifestRefrDate", target = "manifestRefrDate", qualifiedByName = "stringToLocalDateTime")
    @Mapping(source = "machine", target = "machine", qualifiedByName = "machineToEnum")
    Exam toExam(ExamDto examDto);

    @Mapping(source = "examDate", target = "examDate", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "calculDate", target = "calculDate", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "manifestRefrDate", target = "manifestRefrDate", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "machine", target = "machine", qualifiedByName = "enumToMachine")
    ExamDto toExamDto(Exam exam);

    @Mapping(source = "calculDate", target = "calculDate", qualifiedByName = "localDateTimeToString")
    ExamSummaryDto toExamSummaryDto(Exam exam);

    ExamForUpdate toExamForUpdate(Exam exam);

    List<Exam> toExams(List<ExamDto> examDtos);
    List<ExamDto> toExamDtos(List<Exam> exams);
    List<ExamSummaryDto> toExamSummaryDtos(List<Exam> exams);

    @Named("stringToLocalDateTime")
    static LocalDateTime stringToLocalDateTime(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        return DateFunction.parseDateLocalDateTime(date);
    }

    @Named("localDateTimeToString")
    static String localDateToString(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return DateFunction.findFormatLocalDateTimeAndParseString(date);
    }

    @Named("machineToEnum")
    static Enum.Biometer machineToEnum(String machine) {
        if (machine == null) {
            return null;
        }
        return Enum.Biometer.getKeyFromValue(machine);
    }

    @Named("enumToMachine")
    static String enumToMachine(Enum.Biometer machine) {
        if (machine == null) {
            return null;
        }
        return machine.getValue();
    }
}
