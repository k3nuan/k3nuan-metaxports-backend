package com.meta.sports.general.domain;


import com.meta.sports.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Pote {

    private Long id;

    private Long idApuesta;

    private Apuesta apuesta;

    private Long idApuestaExacta;

    private Apuesta apuestaExacta;

    private Integer saldo;

    private Integer nroApuestas;

    private Integer nroGanadores;

    private Long idCreateBy;

    private User createBy;

    private LocalDateTime create;

    private Long idDistributedBy;

    private User distributedBy;

    private LocalDateTime distribute;


    public Pote() {}

    public Pote(Long id) {
        this.id = id;
    }

    public Pote(Long id, Long idApuesta,Long idApuestaExacta, Integer saldo, Integer nroApuestas,Integer nroGanadores, Long idCreateBy, LocalDateTime create,Long idDistributedBy,LocalDateTime distribute) {
        this.id = id;
        this.idApuesta = idApuesta;
        this.idApuestaExacta = idApuestaExacta;
        this.saldo = saldo;
        this.nroApuestas = nroApuestas;
        this.idCreateBy = idCreateBy;
        this.create = create;
        this.idDistributedBy = idDistributedBy;
        this.distribute = distribute;
        this.nroGanadores = nroGanadores;

    }






}
