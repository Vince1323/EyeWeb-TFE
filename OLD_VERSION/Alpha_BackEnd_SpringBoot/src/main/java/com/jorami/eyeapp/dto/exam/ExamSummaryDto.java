package com.jorami.eyeapp.dto.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExamSummaryDto {

    private Long id;
    private String calculDate;
    private Long version;

}
