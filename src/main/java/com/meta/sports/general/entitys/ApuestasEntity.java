package com.meta.sports.general.entitys;


import com.meta.sports.user.adapter.out.persistence.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "apuestas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApuestasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_juego")
    private SoccerGameEntity id_juego;

    @ManyToOne
    @JoinColumn(name="id_evento")
    private EventsEntity id_evento;

    @Column(nullable = false)
    private String etapa;


    @Column(nullable = false)
    private String opcion_ganadora;

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

    @OneToMany(mappedBy = "id_apuesta", cascade = CascadeType.ALL)
    private List<PronosticoUserEntity> pronosticos;




}
