package com.meta.sports.general.entitys;


import com.meta.sports.user.adapter.out.persistence.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "apuestas_exactas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApuestaExactaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_juego")
    private SoccerGameEntity id_juego;


    @Column(nullable = false)
    private String pregunta;


    @Column(nullable = false)
    private Integer marcador_local;

    @Column(nullable = false)
    private Integer marcador_visitante;

    @Column(nullable = false)
    private Integer status;

    @ManyToOne
    @JoinColumn(name="created_by")
    private UserEntity created_by;

    @Column(nullable = false)
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name="modified_by")
    private UserEntity modified_by;

    @Column(nullable = false)
    private LocalDateTime modified;


    @OneToMany(mappedBy = "id_apuesta_exacta", cascade = CascadeType.ALL)
    private List<PronosticoExactoUserEntity> pronosticosExactos;

}
