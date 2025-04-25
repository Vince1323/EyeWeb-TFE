package com.jorami.eyeapp.model.consultation;

import com.jorami.eyeapp.model.IdentifiedModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Audited
@Where(clause = "deleted = FALSE")
@Entity
@Table(name="free_edge")
public class FreeEdge extends IdentifiedModel {

    private boolean normal;
    private boolean telangiectasia;
    private int meibomiusGlandsDysfunction = -1;
    private boolean other;
    private String otherText;

}
