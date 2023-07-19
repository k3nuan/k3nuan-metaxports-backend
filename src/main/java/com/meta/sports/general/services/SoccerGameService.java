package com.meta.sports.general.services;

import com.meta.sports.general.domain.SoccerGame;
import com.meta.sports.general.domain.Tournament;
import com.meta.sports.general.ports.in.SoccerGamePortIn;
import com.meta.sports.general.ports.out.SoccerGamePort;
import com.meta.sports.general.ports.out.TournamentPort;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.global.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SoccerGameService implements SoccerGamePortIn {

    @Autowired
    private SoccerGamePort soccerGamePort;

    @Autowired
    private TournamentPort tournamentPort;

    @Override
    public SoccerGame createSoccerGame(SoccerGame soccerGame) {

        if (soccerGame == null) {
            throw new NotFoundException();
        }

        boolean opTournament = idExist(soccerGame.getIdCampeonato());

        if (opTournament == true) {
            throw new BadRequestException(162);
        }

        return soccerGamePort.createSoccerGame(soccerGame);
    }

    @Override
    public void updateSoccerGame(SoccerGame soccerGame) {

        if (soccerGame == null) {
            throw new NotFoundException();
        }

        SoccerGame opSoccerGame = soccerGamePort.findSoccerGameById(soccerGame.getId());

        if (opSoccerGame == null) {
            throw new BadRequestException(162);
        }

        soccerGamePort.updateSoccerGame(soccerGame);

    }

    @Override
    public List<SoccerGame> findAllSoccerGames() {
        return soccerGamePort.findAllSoccerGames();
    }

    @Override
    public List<SoccerGame> findSoccerGamesByTournament(Long idCampeonato) {

        if (idCampeonato == null ) {
            throw new NotFoundException();
        }

        boolean opTournament = idExist(idCampeonato);

        if (opTournament == true) {
            throw new BadRequestException(162);
        }

        return soccerGamePort.findSoccerGamesByTournament(idCampeonato);
    }

    @Override
    public SoccerGame findSoccerGameById(Long id) {

        if (id == null) {
            throw new BadRequestException(150);
        }

        boolean opExist = idExist(id);





        return soccerGamePort.findSoccerGameById(id);
    }

    @Override
    public void deleteSoccerGame(Long id) {

        if (id == null) {
            throw new BadRequestException(150);

        }



        boolean opTournament = idExist(id);

        if (opTournament == false) {
            throw new BadRequestException(162);
        }

        soccerGamePort.deleteSoccerGame(id);

    }

    private boolean idExist(Long id) {
        try {
            return tournamentPort.findTournamentById(id) == null && tournamentPort.findTournamentById(id) == null;
        } catch (NotFoundException e) {
            return true;
        }
    }
}
