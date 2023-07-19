package com.meta.sports.general.services;


import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.global.exceptions.NotFoundException;
import com.meta.sports.general.domain.Tournament;
import com.meta.sports.general.ports.in.TournamentPortIn;
import com.meta.sports.general.ports.out.TournamentPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class TournamentService implements TournamentPortIn {

    @Autowired
    private TournamentPort tournamentPort;

    @Override
    public Tournament createTournament(Tournament tournament) {
        if (tournament == null) {
            throw new NotFoundException();
        }

        Tournament verifiTournament = tournamentPort.findTournamentByName(tournament.getName());


        if (verifiTournament != null) {

            String verifiTournamentLowerCase=verifiTournament.getName().toLowerCase();
            String tournamentLowerCase=tournament.getName().toLowerCase();

            if (verifiTournamentLowerCase.equals(tournamentLowerCase)) {
                throw new BadRequestException(107);
            };
        }

        return tournamentPort.createTournament(tournament);
    }

    @Override
    public void updateTournament(Tournament tournament) {

        if (tournament == null) {
            throw new NotFoundException();
        }

        Tournament opTournament = tournamentPort.findTournamentById(tournament.getId());

        if (opTournament == null) {
            throw new BadRequestException(160);
        }

        Tournament verifiTournament = tournamentPort.findTournamentByName(tournament.getName());

        if (verifiTournament != null && !verifiTournament.getId().equals(tournament.getId())) {
            throw new BadRequestException(107);
        }

        tournamentPort.updateTournament(tournament);
    }

    @Override
    public List<Tournament> findAllTournaments() {
        return tournamentPort.findAllTournaments();
    }

    @Override
    public Tournament findTournamentById(Long id) {

        if (id == null) {
            throw new BadRequestException(150);
        }

        boolean opTournament = idExist(id);


        if (opTournament == true) {
            throw new BadRequestException(159);
        }


        return tournamentPort.findTournamentById(id);
    }

    @Override
    public void deleteTournament(Long id) {

        if (id == null) {
            throw new BadRequestException(150);

        }

        boolean opTournament = idExist(id);

        if (opTournament == true) {
            throw new BadRequestException(159);
        }

        tournamentPort.deleteTournament(id);

    }

    private boolean idExist(Long id) {
        try {
            return tournamentPort.findTournamentById(id) == null && tournamentPort.findTournamentById(id) == null;
        } catch (NotFoundException e) {
            return true;
        }
    }

    private boolean nameAvailability(String name) {
        try {
            return tournamentPort.findTournamentByName(name) == null && tournamentPort.findTournamentByName(name)  == null;
        } catch (NotFoundException e) {
            return true;
        }
    }
}
