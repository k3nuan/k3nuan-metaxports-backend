package com.meta.sports.general.mappers;

import com.meta.sports.general.domain.SoccerGame;
import com.meta.sports.general.domain.Tournament;
import com.meta.sports.general.entitys.SoccerGameEntity;
import com.meta.sports.general.entitys.TournamentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SoccerGameMapper {
    @Autowired
    private TournamentMapper tournamentMapper;

    public SoccerGameEntity mapToJpaEntity(SoccerGame soccerGame ) {
       return null ;
//        new SoccerGameEntity(
//                soccerGame.getId(),
//                idCampeonato,
//                soccerGame.getEquipoLocal(),
//                soccerGame.getEquipoVisitante(),
//                soccerGame.getFechaHora(),
//                soccerGame.getMarcadorLocal(),
//                soccerGame.getMarcadorVisitante()
//
//
//        );
    }

    public Tournament mapToDomainEntity(TournamentEntity entity ) {

        return null;
//        return new Tournament(
//                entity.getId(),
//                entity.getName(),
//                entity.getStart_date(),
//                entity.getEnd_date()
//        );

    }





}
