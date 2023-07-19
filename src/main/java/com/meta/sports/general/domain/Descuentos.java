package com.meta.sports.general.domain;

import com.meta.sports.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Descuentos {

    private Long id;

    private Long idUsuario;

    private User usuario;

    private Integer montoDescontado;

    private String motivo ;
    private LocalDateTime date;

    private Long idCreatedBy;

    private User createdBy;



    public Descuentos() {}

    public Descuentos(Long id) {
        this.id = id;
    }

    public Descuentos(Long id, Long idUsuario, Integer montoDescontado, Long idCreatedBy, LocalDateTime date) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.montoDescontado= montoDescontado;
        this.idCreatedBy = idCreatedBy;
        this.date = date;

    }


}
