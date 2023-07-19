package com.meta.sports.general.domain;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Events {

    private Long id;

    private String descripcion;

    private String opcionUno;

    private String opcionDos;

    private String opcionTres ;

    public Events() {}

    public Events(Long id) {
        this.id = id;
    }

    public Events(Long id, String descripcion, String opcionUno, String opcionDos, String opcionTres) {
        this.id = id;
        this.descripcion = descripcion;
        this.opcionUno = opcionUno;
        this.opcionDos = opcionDos;
        this.opcionTres = opcionTres;
    }
}
