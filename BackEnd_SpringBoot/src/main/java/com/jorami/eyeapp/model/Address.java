package com.jorami.eyeapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Audited
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Where(clause = "deleted = FALSE")
@Entity
@Table(name = "address")
public class Address extends IdentifiedModel {
    private String street;
    private String streetNumber;
    @Column(name = "box_number", nullable = true)
    private String boxNumber;
    private String zipCode;
    private String city;
    private String country;
}