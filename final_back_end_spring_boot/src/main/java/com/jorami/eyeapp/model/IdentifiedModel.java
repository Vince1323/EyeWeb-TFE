package com.jorami.eyeapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
@EntityListeners({AuditingEntityListener.class})
public abstract class IdentifiedModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(insertable = false)
    private String modifiedBy;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime modifiedAt;

    @JsonIgnore
    @ColumnDefault("false")
    private Boolean deleted;

   // @PrePersist
    // public void prePersist() {
      //  if (this.deleted == null) {
       //     this.deleted = false;
      //  }
   // }


    @Version
    private Long version;

   // public Boolean getDeleted() {
     //   return deleted != null ? deleted : false;
   // }

 //   public void setDeleted(Boolean deleted) {
     //   this.deleted = deleted;
   // }
}
