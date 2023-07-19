package com.meta.sports.general.domain;

import com.meta.sports.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApuestaExacta {

    private Long id;

    private Long idJuego;

    private SoccerGame soccerGame;


    private String pregunta;

    private Integer marcadorLocal;

    private Integer marcadorVisitante;

    private Long idCreateBy;
    private Integer status;



    private User createBy;

    private LocalDateTime create;

    private Long idModifiedBy;

    private User modifiedBy;

    private LocalDateTime modified;

    public ApuestaExacta() {}

    public ApuestaExacta(Long id) {
        this.id = id;
    }

    public ApuestaExacta(Long id, Long idJuego, Integer marcadorLocal,Integer marcadorVisitante, Long idCreateBy, LocalDateTime create,Long idModifiedBy,LocalDateTime modified) {
        this.id = id;
        this.idJuego = idJuego;
        this.marcadorLocal = marcadorLocal;
        this.marcadorVisitante = marcadorVisitante;
        this.idCreateBy = idCreateBy;
        this.create = create;
        this.idModifiedBy = idModifiedBy;
        this.modified = modified;

    }
}
