package com.meta.sports.general.entitys;


import com.meta.sports.user.adapter.out.persistence.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "descuentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DescuentosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    private UserEntity id_usuario;

    @Column(nullable = false)
    private Integer monto_descontado;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String motivo;

    @ManyToOne
    @JoinColumn(name="created_by")
    private UserEntity created_by;
}
