package com.meta.sports.general.mappers;

import com.meta.sports.general.domain.Tournament;
import com.meta.sports.general.entitys.TournamentEntity;

import org.springframework.stereotype.Component;

@Component
public class TournamentMapper {

    public TournamentEntity mapToJpaEntity(Long id,Tournament tournament ) {

//        if (id !=null){
//            return new TournamentEntity(
//                    id,
//                    tournament.getName(),
//                    tournament.getStartDate(),
//                    tournament.getEndDate(),
//                    tournament.
//            );
//
//        }
//        return new TournamentEntity(
//                tournament.getId(),
//                tournament.getName(),
//                tournament.getStartDate(),
//                tournament.getEndDate()
//        );
        return null ;
    }

    public Tournament mapToDomainEntity(TournamentEntity entity ) {
       // return new Tournament(
        //        entity.getId(),
         //       entity.getName(),
         //       entity.getStart_date(),
         //       entity.getEnd_date(),
//
         //       entity.getImage()
      //  );

        return null;

    }





}
