package com.meta.sports.general.ports.in;

import com.meta.sports.general.domain.Tournament;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface TournamentPortIn {


    Tournament createTournament(Tournament tournament);

    void updateTournament(Tournament tournament);

    List<Tournament> findAllTournaments();

    Tournament findTournamentById(Long id);

    void deleteTournament(Long id);
}
