package com.meta.sports.general.entitys;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tournament")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime start_date;

    @Column(nullable = false)
    private LocalDateTime end_date;

    @Column
    private String image;

    @OneToMany(mappedBy = "campeonato_id", cascade = CascadeType.ALL)
    private List<SoccerGameEntity> games;



}
