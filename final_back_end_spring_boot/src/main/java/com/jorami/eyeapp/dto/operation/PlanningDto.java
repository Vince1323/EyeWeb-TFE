package com.jorami.eyeapp.dto.operation;

import lombok.Data;

@Data
public class PlanningDto {
    private Long id;
    private Long examId;
    private String eyeSide;
    private String planningDate;
    private String location;
    private String surgeon;
    private String notes;
}
