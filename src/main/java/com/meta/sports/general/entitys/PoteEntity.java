package com.meta.sports.general.entitys;


import com.meta.sports.user.adapter.out.persistence.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pot")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_apuesta")
    private ApuestasEntity id_apuesta;

    @ManyToOne
    @JoinColumn(name="id_apuesta_exacta")
    private ApuestaExactaEntity id_apuesta_exacta;

    @Column(nullable = false)
    private Integer saldo;

    @Column
    private Integer nro_apuestas;

    @Column
    private Integer nro_ganadores;

    @ManyToOne
    @JoinColumn(name="created_by")
    private UserEntity created_by;

    @Column(nullable = false)
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name="distributed_by")
    private UserEntity distributed_by;

    @Column(nullable = false)
    private LocalDateTime distribute;


}
