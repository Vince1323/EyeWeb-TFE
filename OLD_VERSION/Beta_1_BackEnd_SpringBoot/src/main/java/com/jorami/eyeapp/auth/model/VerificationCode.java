package com.jorami.eyeapp.auth.model;

import com.jorami.eyeapp.model.IdentifiedModel;
import com.jorami.eyeapp.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "verification_code")
public class VerificationCode extends IdentifiedModel {

    private String code;
    private Date expirationTime;
    private static final int EXPIRATION_TIME = 10;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    public VerificationCode(String code, User user) {
        super();
        this.code = code;
        this.user = user;
        this.expirationTime = this.getCodeExpirationTime();
    }

    public Date getCodeExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }

}