package com.jorami.eyeapp.model.consultation;

import com.jorami.eyeapp.model.IdentifiedModel;
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
@Where(clause = "deleted = FALSE")
@Entity
@Table(name="stereoscopic_vision")
public class StereoscopicVision extends IdentifiedModel {
    private Float synoptophore;
    private Float randot;
    private Float nearOther;
    private Float titmus;
    private Float tno;
    private Float distanceOther;
}
