package com.meta.sports.general.adapters;

import com.amazonaws.util.IOUtils;
import com.meta.sports.general.adapters.image.ImageServices;
import com.meta.sports.general.domain.SoccerGame;
import com.meta.sports.general.domain.Tournament;
import com.meta.sports.general.entitys.SoccerGameEntity;
import com.meta.sports.general.entitys.TournamentEntity;
import com.meta.sports.general.mappers.TournamentMapper;

import com.meta.sports.general.ports.out.TournamentPort;
import com.meta.sports.general.repositorys.TournamentRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
@AllArgsConstructor
public class TournamentAdapter implements TournamentPort {

    private final TournamentRepository tournamentRepository;

    private  final ImageServices imageServices;


    private final TournamentMapper tournamentMapper;


    @Override
    @Transactional
    public Tournament createTournament(Tournament tournament) {

        TournamentEntity entity = new TournamentEntity();

        entity.setName(tournament.getName());
        entity.setStart_date(tournament.getStartDate());
        entity.setEnd_date(tournament.getEndDate());

        String nombreImagen =  entity.getName() + ".png ";
        System.out.println(nombreImagen);

        String rutaImagen = imageServices.subirImagen(nombreImagen, tournament.getImagenBase64());
        System.out.println(rutaImagen);

        entity.setImage(rutaImagen);
        tournament.setImagen(entity.getImage());

        tournamentRepository.saveAndFlush(entity);

        tournament.setId(entity.getId());

        return tournament;
    }



    @Override
    @Transactional
    public void updateTournament(Tournament Tournament) {


        TournamentEntity tournamentEntity = tournamentRepository.findTournamentById(Tournament.getId());


        tournamentEntity.setName(Tournament.getName());
        tournamentEntity.setStart_date(Tournament.getStartDate());
        tournamentEntity.setEnd_date(Tournament.getEndDate());
        String nombreImagen =  tournamentEntity.getName() + ".png ";

        String rutaImagen = imageServices.subirImagen(nombreImagen, Tournament.getImagenBase64());

        tournamentEntity.setImage(rutaImagen);


        tournamentRepository.saveAndFlush(tournamentEntity);
    }

    @Override
    public List<Tournament> findAllTournaments() {

        List<TournamentEntity> tournamentsEntities = tournamentRepository.findAll();
        List<Tournament>  tournaments = new ArrayList<>();
        tournamentsEntities.forEach(tournamentEntities-> {
            Tournament tournament = new Tournament();
            tournament.setId(tournamentEntities.getId());
            tournament.setName(tournamentEntities.getName());
            tournament.setStartDate(tournamentEntities.getStart_date());
            tournament.setEndDate(tournamentEntities.getEnd_date());
            tournament.setImagen(imageServices.obtenerImagenBase64(tournament.getName()));

            tournaments.add(tournament);
        });
        return tournaments;

    }

    @Override
    public Tournament findTournamentByName(String name) {

        TournamentEntity tournamentEntity= tournamentRepository.findByName(name);
        if (tournamentEntity != null){
            Tournament tournament = new Tournament();
            tournament.setId(tournamentEntity.getId());
            tournament.setName(tournamentEntity.getName());
            tournament.setStartDate(tournamentEntity.getStart_date());
            tournament.setEndDate(tournamentEntity.getEnd_date());
            tournament.setImagen(imageServices.obtenerImagenBase64(tournament.getName()));
            return tournament;
        }else {
            return null;
        }
    }


    @Override
    public Tournament findTournamentById(Long id) {
        TournamentEntity tournamentEntity= tournamentRepository.findTournamentById(id);
        if (tournamentEntity != null){
            Tournament tournament = new Tournament();
            tournament.setId(tournamentEntity.getId());
            tournament.setName(tournamentEntity.getName());
            tournament.setStartDate(tournamentEntity.getStart_date());
            tournament.setEndDate(tournamentEntity.getEnd_date());
            tournament.setImagen(imageServices.obtenerImagenBase64(tournament.getName()));
            return tournament;
        }else {
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteTournament(Long id) {

    tournamentRepository.deleteById(id);

    }
}
