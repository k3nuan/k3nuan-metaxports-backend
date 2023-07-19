package com.meta.sports.general.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class PronosticoExactoUser {


    private Long id;

    private Long idApuestaExacta;

    private ApuestaExacta apuestaExacta;

    private Long idUser;


    private Integer montoApostado;


    private Integer marcadorLocal;

    private Integer marcadorVisitante;

    private LocalDateTime create;

    private String stateApuesta;

    private LocalDateTime stateDate;

    public PronosticoExactoUser() {}

    public PronosticoExactoUser(Long id) {
        this.id = id;
    }

    public PronosticoExactoUser(Long id, Long idApuestaExacta, Integer montoApostado,Integer marcadorLocal,Integer marcadorVisitante,Long idUser,String stateApuesta, LocalDateTime create,LocalDateTime stateDate) {
        this.id = id;
        this.idApuestaExacta = idApuestaExacta;
        this.montoApostado = montoApostado;
        this.marcadorLocal = marcadorLocal;
        this.marcadorVisitante = marcadorVisitante;
        this.stateApuesta = stateApuesta;
        this.create = create;
        this.stateDate = stateDate;
        this.idUser = idUser;

    }
}
