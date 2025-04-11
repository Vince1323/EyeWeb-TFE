package com.jorami.eyeapp.dto.calcul;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CalculSummaryDto {

    private long id;
    private LocalDateTime createdAt;
    private long version;

}
