package com.meta.sports.general.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SoccerGame {

    private Long id;

    private Long idCampeonato;

    private Tournament tournament;

    private String equipoLocal;

    private String equipoVisitante ;

    private LocalDateTime fechaHora;

    private Integer marcadorLocal;

    private Integer marcadorVisitante;

    private String base64Visitante;

    private String base64Local;

    private String imagenVisitante;

    private String imagenLocal;


    public SoccerGame() {}

    public SoccerGame(Long id) {
        this.id = id;
    }


    public SoccerGame(Long id, Long idCampeonato,String equipoLocal,String equipoVisitante,LocalDateTime fechaHora,Integer marcadorLocal,Integer marcadorVisitante,String base64Visitante,String base64Local,String imagenVisitante,String imagenLocal,Tournament tournament ) {
        this.id = id;
        this.idCampeonato = idCampeonato;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.fechaHora = fechaHora;
        this.marcadorLocal = marcadorLocal;
        this.marcadorVisitante = marcadorVisitante;
        this.base64Visitante = base64Visitante;
        this.base64Local = base64Local;
        this.imagenVisitante = imagenVisitante;
        this.imagenLocal = imagenLocal;
        this.tournament = tournament;

    }

}
