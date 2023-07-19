package com.meta.sports.general.ports.out;


import com.meta.sports.general.domain.Tournament;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface TournamentPort {

    Tournament createTournament(Tournament Tournament);

    void updateTournament(Tournament Tournament);

    List<Tournament> findAllTournaments();

    Tournament findTournamentByName(String name);



    Tournament findTournamentById(Long id);


    void deleteTournament(Long id);
}
