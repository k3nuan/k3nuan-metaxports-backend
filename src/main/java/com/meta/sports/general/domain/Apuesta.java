package com.meta.sports.general.domain;

import com.meta.sports.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Apuesta {

    private Long id;

    private Long idJuego;


    private SoccerGame soccerGame;

    private Long idEvento;

    private Events evento;

    private String etapa;

    private String opcionGanadora;

    private Long idCreateBy;

    private User createBy;

    private LocalDateTime create;

    private Long idModifiedBy;

    private User modifiedBy;

    private LocalDateTime modified;

    public Apuesta() {}

    public Apuesta(Long id) {
        this.id = id;
    }

    public Apuesta(Long id, Long idJuego, Long idEvento, String opcionGanadora, Long idCreateBy, LocalDateTime create,Long idModifiedBy,LocalDateTime modified) {
        this.id = id;
        this.idJuego = idJuego;

        this.idEvento = idEvento;
        this.opcionGanadora = opcionGanadora;
        this.idCreateBy = idCreateBy;
        this.create = create;
        this.idModifiedBy = idModifiedBy;
        this.modified = modified;

    }


}
