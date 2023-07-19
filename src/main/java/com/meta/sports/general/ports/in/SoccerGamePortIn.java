package com.meta.sports.general.ports.in;

import com.meta.sports.general.domain.SoccerGame;

import java.util.List;

public  interface SoccerGamePortIn {

    SoccerGame createSoccerGame(SoccerGame soccerGame);

    void updateSoccerGame(SoccerGame soccerGame);

    List<SoccerGame> findAllSoccerGames();

    List<SoccerGame> findSoccerGamesByTournament(Long idCampeonato);

    SoccerGame findSoccerGameById(Long id);

    void deleteSoccerGame(Long id);
}
