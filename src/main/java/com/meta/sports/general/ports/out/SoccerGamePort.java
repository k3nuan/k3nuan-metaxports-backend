package com.meta.sports.general.ports.out;

import com.meta.sports.general.domain.SoccerGame;

import java.util.List;

public interface SoccerGamePort {

    SoccerGame createSoccerGame(SoccerGame soccerGame);

    void updateSoccerGame(SoccerGame soccerGame);

    List<SoccerGame> findAllSoccerGames();


    SoccerGame findSoccerGameById(Long id);

    List<SoccerGame> findSoccerGamesByTournament(Long campeonatoId);


    void deleteSoccerGame(Long id);
}
