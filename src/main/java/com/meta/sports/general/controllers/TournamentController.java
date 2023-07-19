package com.meta.sports.general.controllers;


import com.meta.sports.general.domain.Tournament;
import com.meta.sports.general.ports.in.TournamentPortIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentPortIn tournamentPortIn;

    @PostMapping("/api/v1/tournaments/createTournament")
    public Tournament createdTournament(@RequestBody Tournament tournament) {

        return tournamentPortIn.createTournament(tournament);

    }

    @PutMapping("/api/v1/tournaments/updateTournament")
    public void  updateTournament(@RequestBody Tournament tournament) {

        tournamentPortIn.updateTournament(tournament);

    }

    @GetMapping("/api/v1/tournaments/Tournaments")
    public List<Tournament> ClientList() {

        return  tournamentPortIn.findAllTournaments();

    }

    @CrossOrigin(origins = "http://192.168.0.101:8100")
    @GetMapping("/api/v1/tournaments/{id}/tournament")
    public Tournament  oneProduct(@PathVariable Long id) {

        return tournamentPortIn.findTournamentById(id);

    }

    @DeleteMapping("/api/v1/tournaments/{id}/deleteTournament")
    public void  deleteProduct(@PathVariable Long id) {

        tournamentPortIn.deleteTournament(id);

    }


}
