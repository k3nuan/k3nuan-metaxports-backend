package com.meta.sports.general.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PronosticoUser {

    private Long id;

    private Long idApuesta;

    private Apuesta apuesta;

    private Long idUser;


    private Integer montoApostado;


    private String opcionApuesta;

    private LocalDateTime create;

    private String stateApuesta;

    private LocalDateTime stateDate;

    public PronosticoUser() {}

    public PronosticoUser(Long id) {
        this.id = id;
    }

    public PronosticoUser(Long id, Long idApuesta, Integer montoApostado,Long idUser,String opcionApuesta,String stateApuesta, LocalDateTime create,LocalDateTime stateDate) {
        this.id = id;
        this.idApuesta = idApuesta;
        this.montoApostado = montoApostado;
        this.opcionApuesta = opcionApuesta;
        this.stateApuesta = stateApuesta;
        this.create = create;
        this.stateDate = stateDate;
        this.idUser = idUser;

    }

}
