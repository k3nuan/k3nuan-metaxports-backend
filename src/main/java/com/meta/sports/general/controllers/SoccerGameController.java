package com.meta.sports.general.controllers;


import com.meta.sports.general.domain.SoccerGame;
import com.meta.sports.general.domain.Tournament;
import com.meta.sports.general.ports.in.SoccerGamePortIn;
import com.meta.sports.general.ports.in.TournamentPortIn;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SoccerGameController {

    private final SoccerGamePortIn soccerGamePortIn;

    @PostMapping("/api/v1/soccerGames/createSoccerGames")
    public SoccerGame createdSoccerGames(@RequestBody SoccerGame soccerGame) {

        return soccerGamePortIn.createSoccerGame(soccerGame);

    }


    @GetMapping("/api/v1/soccerGames/tournaments/{idCampeonato}/games")
    public List<SoccerGame> soccerGamesByTournament(@PathVariable Long idCampeonato) {

        return soccerGamePortIn.findSoccerGamesByTournament(idCampeonato);

    }

    @GetMapping("/api/v1/soccerGames/games")
    public List<SoccerGame> soccerGamesList() {

        return soccerGamePortIn.findAllSoccerGames();


    }

    @GetMapping("/api/v1/soccerGames/{id}/game")
    public SoccerGame oneSoccerGame(@PathVariable Long id) {

        return soccerGamePortIn.findSoccerGameById(id);

    }



    @PutMapping("/api/v1/soccerGames/updateSoccerGame")
    public void  updateSoccerGame(@RequestBody SoccerGame soccerGame) {

        soccerGamePortIn.updateSoccerGame(soccerGame);

    }

    @DeleteMapping("/api/v1/soccerGames/{id}/deleteSoccerGames")
    public void  deleteSoccerGame(@PathVariable Long id) {

        soccerGamePortIn.deleteSoccerGame(id);

    }





}
