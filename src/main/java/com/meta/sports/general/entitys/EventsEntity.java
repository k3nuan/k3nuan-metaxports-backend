package com.meta.sports.general.entitys;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "eventos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String opcion_uno;


    @Column(nullable = false)
    private String opcion_dos;

    @Column(nullable = false)
    private String opcion_tres;

    @OneToMany(mappedBy = "id_evento", cascade = CascadeType.ALL)
    private List<ApuestasEntity> apuestas;



}
