package com.meta.sports.general.entitys;


import com.meta.sports.user.adapter.out.persistence.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recargas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecargasEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    private UserEntity id_usuario;

    @Column(nullable = false)
    private Integer monto_recargado;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name="created_by")
    private UserEntity created_by;



}
