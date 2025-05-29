package com.jorami.eyeapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Audited
@SuperBuilder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Where(clause = "deleted = FALSE")
@Entity
@Table(name="lens")
public class Lens extends IdentifiedModel {

    private String name;
    private String commentTradeName;

    // Valeur nominale
    private Float nominal;
    private Float haigisA0;
    private Float haigisA1;
    private Float haigisA2;
    private Float hofferQPACD;
    private Float holladay1SF;
    // aConstant
    private Float srkta;

    // Valeur optimisée
    private Float haigisA0Optimized;
    private Float haigisA1Optimized;
    private Float haigisA2Optimized;
    private Float hofferQPACDOptimized;
    private Float holladay1SFOptimized;
    private Float cookeOptimized;
    // aConstant
    private Float srktaOptimized;
    private Float castropCOptimized;
    private Float castropHOptimized;
    private Float castropROptimized;

    @ManyToOne
    @JoinColumn(name="lens_manufacturer_id")
    @JsonIgnore
    private LensManufacturer lensManufacturer;

}