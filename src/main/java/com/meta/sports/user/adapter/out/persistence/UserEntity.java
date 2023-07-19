package com.meta.sports.user.adapter.out.persistence;

import com.meta.sports.general.entitys.DescuentosEntity;
import com.meta.sports.general.entitys.PronosticoExactoUserEntity;
import com.meta.sports.general.entitys.PronosticoUserEntity;
import com.meta.sports.general.entitys.RecargasEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column
    private String otp;

    @OneToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Integer saldo;


    @Column(nullable = false)
    private String email;

    @Column
    private String phone;

    @Column
    private LocalDate birthDate;

    @Column
    private Date otpTime;

    @Column
    private String gender;

    @Column
    private String imgProfile;

    @Column(name = "ip_address")
    private String origin;

    @Column
    private LocalDateTime lastLogin;

    @OneToOne
    @JoinColumn(name = "id")
    private AddressEntity address;

    @Column
    private LocalDate created;

    @Column
    private String codigo_referido;

    @Column
    private String codigo_referente;

    @ManyToOne
    @JoinColumn(name = "id_referente")
    private UserEntity referente;


    @OneToMany(mappedBy = "id_usuario", cascade = CascadeType.ALL)
    private List<PronosticoUserEntity> pronosticos;

    @OneToMany(mappedBy = "id_usuario", cascade = CascadeType.ALL)
    private List<PronosticoExactoUserEntity> pronosticosExactos;


    @OneToMany(mappedBy = "id_usuario")
    private List<RecargasEntity> recargas;

    @OneToMany(mappedBy = "id_usuario")
    private List<DescuentosEntity> descuentos;


}