package com.meta.sports.general.adapters;


import com.meta.sports.general.adapters.image.ImageServices;
import com.meta.sports.general.domain.SoccerGame;
import com.meta.sports.general.domain.Tournament;
import com.meta.sports.general.entitys.ApuestaExactaEntity;
import com.meta.sports.general.entitys.PoteEntity;
import com.meta.sports.general.entitys.SoccerGameEntity;
import com.meta.sports.general.entitys.TournamentEntity;
import com.meta.sports.general.ports.out.SoccerGamePort;
import com.meta.sports.general.repositorys.ApuestaExactaRepository;
import com.meta.sports.general.repositorys.PoteRepository;
import com.meta.sports.general.repositorys.SoccerGameRepository;
import com.meta.sports.general.repositorys.TournamentRepository;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.global.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class SoccerGameAdapter implements SoccerGamePort {

    private  final SoccerGameRepository soccerGameRepository;

    private  final TournamentRepository tournamentRepository;

    private  final PoteRepository poteRepository;


    private  final ApuestaExactaRepository apuestaExactaRepository;


    private  final ImageServices imageServices;

    private  final TournamentAdapter tournamentAdapter;



    @Override
    public SoccerGame createSoccerGame(SoccerGame soccerGame) {


        // Obtener el objeto TournamentEntity correspondiente al campeonato con ID 1
        TournamentEntity tournamentIdEntity = tournamentRepository.findTournamentById(soccerGame.getIdCampeonato());


        // Crear un objeto SoccerGame para el nuevo juego
        SoccerGameEntity gameEntity = new SoccerGameEntity();

        // Establecer la relación ManyToOne con el objeto TournamentEntity correspondiente
        gameEntity.setCampeonato_id(tournamentIdEntity);

        // Establecer los campos de juego según sea necesario
        gameEntity.setEquipo_local(soccerGame.getEquipoLocal());
        gameEntity.setEquipo_visitante(soccerGame.getEquipoVisitante());
        gameEntity.setFecha_hora(soccerGame.getFechaHora());
        gameEntity.setMarcador_local(0);
        gameEntity.setMarcador_visitante(0);

        String nombreImagenLocal = gameEntity.getEquipo_local() + ".png ";
        String rutaImagenLocal = imageServices.subirImagen(nombreImagenLocal, soccerGame.getBase64Local());
        gameEntity.setImagen_local(rutaImagenLocal);

        String nombreImagenVisitante = gameEntity.getEquipo_visitante() + ".png ";
        String rutaImagenVisitante = imageServices.subirImagen(nombreImagenVisitante, soccerGame.getBase64Visitante());
        gameEntity.setImagen_visitante(rutaImagenVisitante);

        // Guardar el objeto SoccerGame en la base de datos utilizando el SoccerGameRepository
        soccerGameRepository.saveAndFlush(gameEntity);

        //Creamos la apuesta exacta asociada a este juego
        ApuestaExactaEntity apuestaExactaEntity=new ApuestaExactaEntity();

        apuestaExactaEntity.setId_juego(gameEntity);
        apuestaExactaEntity.setPregunta("¿Cual es el marcador final del partido?");
        apuestaExactaEntity.setMarcador_local(0);
        apuestaExactaEntity.setMarcador_visitante(0);
        apuestaExactaEntity.setCreated(LocalDateTime.now());
        apuestaExactaEntity.setStatus(0);

        apuestaExactaRepository.saveAndFlush(apuestaExactaEntity);

        PoteEntity poteEntity = new PoteEntity();
        poteEntity.setId_apuesta_exacta(apuestaExactaEntity);
        poteEntity.setSaldo(0);
        poteEntity.setNro_apuestas(0);
        poteEntity.setNro_ganadores(0);
        poteEntity.setCreated(LocalDateTime.now());
        poteRepository.saveAndFlush(poteEntity);

        //Asignamos el id a la consulta
        soccerGame.setId(gameEntity.getId());

        //retornamos el objeto
        return soccerGame;


    }

    @Override
    public void updateSoccerGame(SoccerGame soccerGame) {

        SoccerGameEntity soccerGameEntity = soccerGameRepository.findSoccerGameById(soccerGame.getId());

        TournamentEntity tournamentIdEntity = tournamentRepository.findTournamentById(soccerGame.getIdCampeonato());


        soccerGameEntity.setCampeonato_id(tournamentIdEntity);
        soccerGameEntity.setFecha_hora(soccerGame.getFechaHora());
        soccerGameEntity.setEquipo_local(soccerGame.getEquipoLocal());
        soccerGameEntity.setEquipo_visitante(soccerGame.getEquipoVisitante());
        soccerGameEntity.setMarcador_local(soccerGame.getMarcadorLocal());
        soccerGameEntity.setMarcador_visitante(soccerGame.getMarcadorVisitante());


        String nombreImagenLocal =  soccerGameEntity.getEquipo_local() + ".png ";
        String rutaImagenLocal = imageServices.subirImagen(nombreImagenLocal, soccerGame.getBase64Local());
        soccerGameEntity.setImagen_local(rutaImagenLocal);

        String nombreImagenVisitante =  soccerGameEntity.getEquipo_visitante() + ".png ";
        String rutaImagenVisitante = imageServices.subirImagen(nombreImagenVisitante, soccerGame.getBase64Visitante());
        soccerGameEntity.setImagen_visitante(rutaImagenVisitante);


        soccerGameRepository.saveAndFlush(soccerGameEntity);


    }

    @Override
    public List<SoccerGame> findAllSoccerGames() {

        List<SoccerGameEntity> soccerGameEntities = soccerGameRepository.findAll();
        List<SoccerGame>  games = new ArrayList<>();
        soccerGameEntities.forEach(soccerGameEntity-> {
            SoccerGame soccerGame = new SoccerGame();
            soccerGame.setId(soccerGameEntity.getId());
            soccerGame.setIdCampeonato(soccerGameEntity.getCampeonato_id().getId());
            soccerGame.setTournament(tournamentAdapter.findTournamentById(soccerGame.getIdCampeonato()));
            soccerGame.setFechaHora(soccerGameEntity.getFecha_hora());
            soccerGame.setEquipoLocal(soccerGameEntity.getEquipo_local());
            soccerGame.setEquipoVisitante(soccerGameEntity.getEquipo_visitante());
            soccerGame.setMarcadorLocal(soccerGameEntity.getMarcador_local());
            soccerGame.setMarcadorVisitante(soccerGameEntity.getMarcador_visitante());
            soccerGame.setImagenLocal(imageServices.obtenerImagenBase64(soccerGame.getEquipoLocal()));
            soccerGame.setImagenVisitante(imageServices.obtenerImagenBase64(soccerGame.getEquipoVisitante()));
            games.add(soccerGame);
        });
        return games;
    }

    @Override
    public SoccerGame findSoccerGameById(Long id) {

        SoccerGameEntity soccerGameEntity= soccerGameRepository.findSoccerGameById(id);

        if (soccerGameEntity == null) {
            throw new BadRequestException(162);
        }

            SoccerGame soccerGame = new SoccerGame();
            soccerGame.setId(soccerGameEntity.getId());
            soccerGame.setIdCampeonato(soccerGameEntity.getCampeonato_id().getId());
            soccerGame.setEquipoLocal(soccerGameEntity.getEquipo_local());
            soccerGame.setFechaHora(soccerGameEntity.getFecha_hora());
            soccerGame.setEquipoVisitante(soccerGameEntity.getEquipo_visitante());
            soccerGame.setMarcadorLocal(soccerGameEntity.getMarcador_local());
            soccerGame.setMarcadorVisitante(soccerGameEntity.getMarcador_visitante());
            soccerGame.setImagenLocal(imageServices.obtenerImagenBase64(soccerGame.getEquipoLocal()));
            soccerGame.setImagenVisitante(imageServices.obtenerImagenBase64(soccerGame.getEquipoVisitante()));
            return soccerGame;

    }

    @Transactional
    @Override
    public List<SoccerGame> findSoccerGamesByTournament(Long campeonatoId) {

        TournamentEntity campeonato = tournamentRepository.findTournamentById(campeonatoId);

        if (campeonato == null) {
            // Manejar el caso en que no se encuentre el campeonato
            throw new NotFoundException();
        }

        List<SoccerGameEntity> gamesEntities = campeonato.getGames();

        if (gamesEntities == null || gamesEntities.isEmpty()) {
            // Manejar el caso en que no se encuentren juegos asociados al campeonato
            throw new BadRequestException(161);
        }

        List<SoccerGame> soccerGames = new ArrayList<>(gamesEntities.size());
        SoccerGame soccerGame;

        for (SoccerGameEntity gameEntity : gamesEntities) {
            soccerGame = new SoccerGame();
            soccerGame.setEquipoLocal(gameEntity.getEquipo_local());
            soccerGame.setEquipoVisitante(gameEntity.getEquipo_visitante());
            soccerGame.setFechaHora(gameEntity.getFecha_hora());
            soccerGame.setIdCampeonato(campeonatoId);
            soccerGame.setId(gameEntity.getId());
            soccerGame.setTournament(tournamentAdapter.findTournamentById(soccerGame.getIdCampeonato()));
            soccerGame.setMarcadorLocal(gameEntity.getMarcador_local());
            soccerGame.setMarcadorVisitante(gameEntity.getMarcador_visitante());
            soccerGame.setImagenLocal(imageServices.obtenerImagenBase64(soccerGame.getEquipoLocal()));
            soccerGame.setImagenVisitante(imageServices.obtenerImagenBase64(soccerGame.getEquipoVisitante()));
            soccerGames.add(soccerGame);
        }

        return soccerGames;


    }

    @Override
    public void deleteSoccerGame(Long id) {

        soccerGameRepository.deleteById(id);

    }
}
