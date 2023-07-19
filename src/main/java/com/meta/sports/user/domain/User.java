package com.meta.sports.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.meta.sports.global.Constants;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class User {

    public static final String STATUS_ACTIVE     = "A";
    public static final String STATUS_INACTIVE   = "I";
    public static final String STATUS_LOCKED     = "B";

    private Long id;

    private Address address;

    private LocalDate birthDate;

    private String email;

    private String firstName;

    private Integer saldo;

    private String gender;

    private LocalDateTime lastLogin;

    private String lastName;

    private String name;

    private LocalDate created;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String oldPassword;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordConfirm;

    private String tokenPassword;

    private String phone;

    private String profilePic;

    private String role;

    private String status;

    private String username;

    private String codigoReferido;

    private Long referente;


    private String codigoReferente;

    /** Constructors **/

    public User() {}

    public User(Long id) {
        this.id = id;
    }

    public User(String username) {
        this.username = username;
    }

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    /** Utils **/

    public boolean hasRole(String role, boolean withPrefix) {
        return this.role != null && role != null && role.equalsIgnoreCase((withPrefix ? Constants.ROLE_PREFIX : "") + this.role) ? true : false;
    }

    public User withRole(String role) {
        this.role = role;
        return this;
    }

    public User withStatus(String status) {
        this.status = status;
        return this;
    }

    public String getTokenPassword() {
        return tokenPassword;
    }

    public void setTokenPassword(String tokenPassword) {
        this.tokenPassword = tokenPassword;
    }
}
