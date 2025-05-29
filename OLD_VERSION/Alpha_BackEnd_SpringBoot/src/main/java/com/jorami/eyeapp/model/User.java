package com.jorami.eyeapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jorami.eyeapp.auth.config.CustomAuthorityDeserializer;
import com.jorami.eyeapp.auth.model.Token;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Audited
@Table(name = "_user")
@EntityListeners({AuditingEntityListener.class})
public class User extends IdentifiedModel implements UserDetails {

    private String firstname;
    private String lastname;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "birth_date", nullable = true)
    private LocalDate birthDate;
    private String phone;
    @Column(unique = true)
    private String email;
    private String password;
    @ColumnDefault("false")
    private boolean verified;
    @ColumnDefault("false")
    private boolean validEmail;
    private boolean hasReadTermsAndConditions;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    @NotAudited
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @NotAudited
    private List<Token> tokens;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    @NotAudited
    private List<UserOrganizationRole> userOrganizationRoles;


    /**
     * @return l'email de ce Principal.
     */
    public String getName() {
        return email;
    }

    /**
     * Renvoie la collection des autorisations accordées à l'utilisateur.
     *
     * @return La collection des autorisations, représentées par des instances `GrantedAuthority`.
     */
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.getName()));
    }

    /**
     * @return le mot de passe de cet utilisateur.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * @return l'email de cet utilisateur.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * @return 'true' si le compte n'est pas expiré, 'false' sinon.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return 'true' si les informations d'identification de l'utilisateur sont valides et non périmées, 'false' sinon.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return 'true' si le compte est activé, 'false' sinon.
     */
    @Override
    public boolean isEnabled() {
        return verified;
    }

    public String getFullName() {
        return firstname.substring(0, 1).toUpperCase() + firstname.substring(1).toLowerCase() + " " + lastname.toUpperCase();
    }

    @JsonProperty("birthDate") // Force son inclusion
    public LocalDate getBirthDate() {
        return birthDate;
    }

}
