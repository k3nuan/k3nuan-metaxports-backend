package com.meta.sports.general.entitys;


import com.meta.sports.user.adapter.out.persistence.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pronostico_exacto_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PronosticoExactoUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_apuesta_exacta")
    private ApuestaExactaEntity id_apuesta_exacta;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    private UserEntity id_usuario;

    @Column(nullable = false)
    private Integer monto_apostado;

    @Column(nullable = false)
    private Integer marcador_local;

    @Column(nullable = false)
    private Integer marcador_visitante;

    @Column(nullable = false)
    private Integer puntuacion;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column(nullable = false)
    private String state_apuesta;

    @Column(nullable = false)
    private LocalDateTime state_date;
}
