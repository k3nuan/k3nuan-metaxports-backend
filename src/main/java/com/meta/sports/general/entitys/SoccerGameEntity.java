package com.meta.sports.general.entitys;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "soccer_games")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoccerGameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="campeonato_id")
    private TournamentEntity campeonato_id;


    @Column(nullable = false)
    private String equipo_local;

    @Column(nullable = false)
    private String equipo_visitante;


    @Column(nullable = false)
    private LocalDateTime fecha_hora;

    @Column(nullable = false)
    private Integer marcador_local;

    @Column(nullable = false)
    private Integer marcador_visitante;

   @Column
    private String imagen_local;

   @Column
    private String imagen_visitante;

    @OneToMany(mappedBy = "id_juego", cascade = CascadeType.ALL)
    private List<ApuestasEntity> apuestas;

    @OneToMany(mappedBy = "id_juego", cascade = CascadeType.ALL)
    private List<ApuestaExactaEntity> apuestasExactas;


}



