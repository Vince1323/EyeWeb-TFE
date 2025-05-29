package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.calcul.CalculDto;
import com.jorami.eyeapp.dto.calcul.CalculMatrixDto;
import com.jorami.eyeapp.dto.calcul.CalculSummaryDto;
import com.jorami.eyeapp.model.Calcul;
import com.jorami.eyeapp.model.Diopter;
import com.jorami.eyeapp.model.Enum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.*;

@Mapper(componentModel = "spring", uses = {LensMapper.class, DiopterMapper.class, ConstantMapper.class, ExamMapper.class})
public interface CalculMapper {

    CalculMapper INSTANCE = Mappers.getMapper(CalculMapper.class);

    @Mapping(source = "eyeSide", target = "eyeSide", qualifiedByName = "stringToEyeSide")
    Calcul toCalcul(CalculDto calculDto);
    @Mapping(source = "eyeSide", target = "eyeSide", qualifiedByName = "eyeSideToString")
    CalculDto toCalculDto(Calcul calcul);

    @Mapping(source = "eyeSide", target = "eyeSide", qualifiedByName = "stringToEyeSide")
    Calcul toCalcul(CalculMatrixDto calculMatrixDto);
    @Mapping(source = "eyeSide", target = "eyeSide", qualifiedByName = "eyeSideToString")
    CalculMatrixDto toCalculMatrixDto(Calcul calcul);
    CalculSummaryDto toCalculSummaryDto(Calcul calcul);

    List<Calcul> toCalculs(List<CalculDto> calculDtos);
    List<CalculDto> toCalculDtos(List<Calcul> calculs);
    List<CalculSummaryDto> toCalculsSummaryDto(List<Calcul> calculs);

    @Named("stringToEyeSide")
    static Enum.EyeSide stringToEyeSide(String eyeSide) {
        if (eyeSide == null) {
            return null;
        }
        try {
            return Enum.EyeSide.valueOf(eyeSide.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Named("eyeSideToString")
    static String eyeSideToString(Enum.EyeSide eyeSide) {
        return eyeSide != null ? eyeSide.name() : null;
    }


    static CalculMatrixDto convertCalculToCalculMatrixDto(CalculMapper mapper,Calcul calcul) {
        final List<String> customOrder = Arrays.asList("Barrett", "Kane", "Pearl", "Cooke", "Evo", "HofferQst", "Srkt", "HofferQ", "Holladay", "Haigis");
        CalculMatrixDto calculMatrixDto = mapper.toCalculMatrixDto(calcul);

        /*
        calcul.getDiopters().forEach(diopter -> {
            if (!diopter.getIolPower().contains(".")) {
                diopter.setIolPower(diopter.getIolPower() + ".0");
            }
        });
         */
        Optional<Float> selectedPower = calcul.getDiopters().stream()
                .filter(Diopter::isSelected)
                .map(Diopter::getIolPower)
                .findFirst();

        calculMatrixDto.setSelectedPower(selectedPower.orElse(null));

        List<Float> sortedPowers = calcul.getDiopters().stream()
                .map(Diopter::getIolPower)
                .distinct()
                .sorted()
                .toList();

        List<String> sortedFormulas = calcul.getDiopters().stream()
                .map(Diopter::getFormula)
                .distinct()
                .sorted()
                .toList();

        sortedFormulas = customOrder.stream()
                .filter(sortedFormulas::contains)
                .toList();

        Map<Float, Integer> powerIndexes = new HashMap<>();
        Map<String, Integer> formulaIndexes = new HashMap<>();
        for (int i = 0; i < sortedPowers.size(); i++) {
            powerIndexes.put(sortedPowers.get(i), i);
        }
        for (int i = 0; i < sortedFormulas.size(); i++) {
            formulaIndexes.put(sortedFormulas.get(i), i);
        }

        Float[][] valuesMatrix = new Float[sortedPowers.size()][sortedFormulas.size()+1];

        for (Diopter diopter : calcul.getDiopters()) {
            Integer powerIndex = powerIndexes.get(diopter.getIolPower());
            Integer formulaIndex = formulaIndexes.get(diopter.getFormula());
            if (powerIndex != null && formulaIndex != null) {
                valuesMatrix[powerIndex][0] = diopter.getIolPower();
                valuesMatrix[powerIndex][formulaIndex+1] = diopter.getValue();
            }
        }

        calculMatrixDto.setPowers(sortedPowers);
        calculMatrixDto.setFormulas(sortedFormulas);
        calculMatrixDto.setValueMatrix(Arrays.asList(valuesMatrix));

        return calculMatrixDto;
    }

}