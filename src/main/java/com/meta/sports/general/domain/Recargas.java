package com.meta.sports.general.domain;

import com.meta.sports.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Recargas {

    private Long id;

    private Long idUsuario;

    private User usuario;

    private Integer montoRecargado;


    private LocalDateTime date;

    private Long idCreatedBy;

    private User createdBy;



    public Recargas() {}

    public Recargas(Long id) {
        this.id = id;
    }

    public Recargas(Long id, Long idUsuario, Integer montoRecargado, Long idCreatedBy, LocalDateTime date) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.montoRecargado= montoRecargado;
        this.idCreatedBy = idCreatedBy;
        this.date = date;

    }


}
