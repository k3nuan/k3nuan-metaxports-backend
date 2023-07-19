package com.meta.sports.general.adapters;


import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.Events;
import com.meta.sports.general.domain.Pote;
import com.meta.sports.general.domain.SoccerGame;
import com.meta.sports.general.entitys.*;
import com.meta.sports.general.ports.out.ApuestasPort;
import com.meta.sports.general.repositorys.*;
import com.meta.sports.global.Constants;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.global.exceptions.NotFoundException;
import com.meta.sports.user.adapter.out.persistence.UserEntity;
import com.meta.sports.user.adapter.out.persistence.UserRepository;
import com.meta.sports.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ApuestasAdapter implements ApuestasPort {


    private final EntityManagerFactory entityManagerFactory;

    private final ApuestasRepository apuestasRepository;
    private final SoccerGameRepository soccerGameRepository;
    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;
    private final PoteRepository poteRepository;
    private final SoccerGameAdapter soccerGame;
    private final eventsAdapter eventsAdapter;

    private final PronosticoUserRepository pronosticoUserRepository;


    @Override
    public Apuesta createApuesta(Apuesta apuesta) {
        List<ApuestasEntity> existingApuestas = apuestasRepository.findByJuegoAndEventoList(apuesta.getIdJuego(), apuesta.getIdEvento());
        if (existingApuestas.size() == 1) {
            ApuestasEntity existingApuesta = existingApuestas.get(0);
            if (existingApuesta.getEtapa().equals(Constants.PRIMERA_ETAPA)) {
                ApuestasEntity nuevaApuesta = new ApuestasEntity();
                nuevaApuesta.setId_juego(existingApuesta.getId_juego());
                nuevaApuesta.setId_evento(existingApuesta.getId_evento());
                nuevaApuesta.setCreated_by(existingApuesta.getCreated_by());
                nuevaApuesta.setEtapa(Constants.SEGUNDA_ETAPA);
                nuevaApuesta.setCreated(LocalDateTime.now());
                apuestasRepository.saveAndFlush(nuevaApuesta);
                PoteEntity poteEntity = new PoteEntity();
                poteEntity.setId_apuesta(nuevaApuesta);
                poteEntity.setSaldo(0);
                poteEntity.setNro_apuestas(0);
                poteEntity.setNro_ganadores(0);
                poteEntity.setCreated_by(nuevaApuesta.getCreated_by());
                poteEntity.setCreated(LocalDateTime.now());
                poteRepository.saveAndFlush(poteEntity);
                apuesta.setId(nuevaApuesta.getId());
                return apuesta;
            } else if (existingApuesta.getEtapa().equals(Constants.SEGUNDA_ETAPA)) {
                throw new BadRequestException(320);
            }
        } else if (existingApuestas.size() == 0) {
            SoccerGameEntity soccerGameIdEntity = soccerGameRepository.findSoccerGameById(apuesta.getIdJuego());
            EventsEntity eventIdEntity = eventsRepository.findEventById(apuesta.getIdEvento());
            UserEntity userIdEntity = userRepository.findUserByid(apuesta.getIdCreateBy());
            ApuestasEntity apuestaEntity = new ApuestasEntity();
            apuestaEntity.setId_juego(soccerGameIdEntity);
            apuestaEntity.setId_evento(eventIdEntity);
            apuestaEntity.setCreated_by(userIdEntity);
            apuestaEntity.setOpcion_ganadora(apuesta.getOpcionGanadora());
            apuestaEntity.setEtapa(Constants.PRIMERA_ETAPA);
            apuestaEntity.setCreated(LocalDateTime.now());
            apuestasRepository.saveAndFlush(apuestaEntity);
            PoteEntity poteEntity = new PoteEntity();
            poteEntity.setId_apuesta(apuestaEntity);
            poteEntity.setSaldo(0);
            poteEntity.setNro_apuestas(0);
            poteEntity.setNro_ganadores(0);
            poteEntity.setCreated_by(apuestaEntity.getCreated_by());
            poteEntity.setCreated(LocalDateTime.now());
            poteRepository.saveAndFlush(poteEntity);
            apuesta.setId(apuestaEntity.getId());
            return apuesta;
        }
        throw new BadRequestException(321);

    }
    @Transactional
    @Override
    public List<Apuesta> createApuestaSegundoTiempo(Long idJuego) {

        List<Apuesta> apuestasPrimerTiempo =findApuestaBySoccerGame(idJuego);

        List<Apuesta>  apuestasSegundoTiempo = new ArrayList<>();

        apuestasPrimerTiempo.forEach(apuesta-> {

            Apuesta apuestaSegundoTiempo =createApuesta(apuesta);

            apuestasSegundoTiempo.add(apuestaSegundoTiempo);

        });

        return apuestasSegundoTiempo;


        }


    @Override
    public void apuestaGanadora(Apuesta apuesta) {

        ApuestasEntity apuestaEntity = apuestasRepository.findApuestaById(apuesta.getId());

        UserEntity userIdEntity = userRepository.findUserByid(apuesta.getIdModifiedBy());

        if (apuestaEntity.getOpcion_ganadora()!= null){
            throw new BadRequestException(322);
        }

        apuestaEntity.setOpcion_ganadora(apuesta.getOpcionGanadora());
        apuestaEntity.setModified_by(userIdEntity);
        apuestaEntity.setModified(LocalDateTime.now());

        apuestasRepository.saveAndFlush(apuestaEntity);

        PoteEntity poteEntity = poteRepository.findPoteByApuestaId(apuestaEntity);

        List<PronosticoUserEntity> pronosticosEntity= pronosticoUserRepository.findPronosticoByApuestaId(apuestaEntity);

        int ganadores = 0;
        for (PronosticoUserEntity pronosticoEntity : pronosticosEntity) {
            if (pronosticoEntity.getOpcion_apuesta().equalsIgnoreCase(apuestaEntity.getOpcion_ganadora())) {
                pronosticoEntity.setState_apuesta(Constants.APUESTA_GANADA);
                pronosticoEntity.setState_date(LocalDateTime.now());
                ganadores++;
            } else {
                pronosticoEntity.setState_apuesta(Constants.APUESTA_PERDIDA);
                pronosticoEntity.setState_date(LocalDateTime.now());
            }

            pronosticoUserRepository.saveAndFlush(pronosticoEntity);
        }

        poteEntity.setNro_ganadores(ganadores);

        List<PronosticoUserEntity> pronosticosGanadoresEntity= pronosticoUserRepository.findPronosticoGanadores(apuestaEntity);

        pronosticosGanadoresEntity.forEach(pronosticoGanador-> {

            UserEntity usuarioGanador= userRepository.findUserByid(pronosticoGanador.getId_usuario().getId());

            int montoAsignado = poteEntity.getSaldo() / poteEntity.getNro_ganadores();

            int saldoGanador = usuarioGanador.getSaldo() + montoAsignado;


            if (usuarioGanador.getReferente() != null) {
                UserEntity referente = userRepository.findUserByid(usuarioGanador.getReferente().getId());

                int montoReferente = (int) (montoAsignado * 0.05);
                int saldoReferente = referente.getSaldo() + montoReferente;
                referente.setSaldo(saldoReferente);

                int saldoGanadorConReferente = saldoGanador - montoReferente;
                usuarioGanador.setSaldo(saldoGanadorConReferente);

                userRepository.saveAndFlush(usuarioGanador);
                userRepository.saveAndFlush(referente);


            }else{
                usuarioGanador.setSaldo(saldoGanador);

                userRepository.saveAndFlush(usuarioGanador);
            }


        });

        poteEntity.setDistributed_by(userIdEntity);
        poteEntity.setDistribute(LocalDateTime.now());

        poteRepository.saveAndFlush(poteEntity);

    }

    @Override
    public List<Apuesta> findAllApuestas() {
        List<ApuestasEntity> apuestasEntitiesEntities = apuestasRepository.findAll();
        List<Apuesta>  apuestas = new ArrayList<>();
        apuestasEntitiesEntities.forEach(apuestaEntity-> {

            Apuesta apuesta = new Apuesta();
            apuesta.setId(apuestaEntity.getId());
            apuesta.setIdJuego(apuestaEntity.getId_juego().getId());
            apuesta.setIdEvento(apuestaEntity.getId_evento().getId());
            apuesta.setOpcionGanadora(apuestaEntity.getOpcion_ganadora());
            apuesta.setIdCreateBy(apuestaEntity.getCreated_by().getId());
            apuesta.setCreate(apuestaEntity.getCreated());

            apuesta.setEtapa(apuestaEntity.getEtapa());


            if (apuestaEntity.getModified_by() != null) {
                apuesta.setIdModifiedBy(apuestaEntity.getModified_by().getId());
                apuesta.setModified(apuestaEntity.getModified());
            }

            apuestas.add(apuesta);
        });
        return apuestas;
    }

    @Override
    public Apuesta findApuestaById(Long id) {

        ApuestasEntity apuestaEntity= apuestasRepository.findApuestaById(id);

        if (apuestaEntity == null) {
            throw new BadRequestException(323);
        }

        Apuesta apuesta = new Apuesta();
        apuesta.setId(apuestaEntity.getId());
        apuesta.setIdJuego(apuestaEntity.getId_juego().getId());
        apuesta.setSoccerGame(soccerGame.findSoccerGameById(apuesta.getIdJuego()));
        apuesta.setIdEvento(apuestaEntity.getId_evento().getId());
        apuesta.setEvento(eventsAdapter.findEventById(apuesta.getIdEvento()));

        apuesta.setEtapa(apuesta.getEtapa());

        apuesta.setOpcionGanadora(apuestaEntity.getOpcion_ganadora());
        apuesta.setIdCreateBy(apuestaEntity.getCreated_by().getId());
        apuesta.setCreate(apuestaEntity.getCreated());
        if (apuestaEntity.getModified_by() != null){
            apuesta.setIdModifiedBy(apuestaEntity.getModified_by().getId());
            apuesta.setModified(apuestaEntity.getModified());
        }

        return apuesta;

    }
    @Transactional
    @Override
    public List<Apuesta> findApuestaBySoccerGame(Long idJuego) {

        SoccerGameEntity soccerGameEntity = soccerGameRepository.findSoccerGameById(idJuego);

        if (soccerGameEntity == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(324);

        }

        List<ApuestasEntity> apuestasEntities = soccerGameEntity.getApuestas();

        if (apuestasEntities == null || apuestasEntities.isEmpty()) {
            // Manejar el caso en que no se encuentren apuestas asociados al juego
            throw new RuntimeException("No se han encontrado apuestas");
        }

        List<Apuesta> apuestas = new ArrayList<>(apuestasEntities.size());
        Apuesta apuesta;

        for (ApuestasEntity apuestaEntity : apuestasEntities) {

            apuesta = new Apuesta();
            apuesta.setId(apuestaEntity.getId());
            apuesta.setIdJuego(apuestaEntity.getId_juego().getId());
            apuesta.setSoccerGame(soccerGame.findSoccerGameById(apuesta.getIdJuego()));
            apuesta.setIdEvento(apuestaEntity.getId_evento().getId());
            apuesta.setEvento(eventsAdapter.findEventById(apuesta.getIdEvento()));
            apuesta.setEtapa(apuestaEntity.getEtapa());

            apuesta.setOpcionGanadora(apuestaEntity.getOpcion_ganadora());
            apuesta.setIdCreateBy(apuestaEntity.getCreated_by().getId());
            apuesta.setCreate(apuestaEntity.getCreated());
            if (apuestaEntity.getModified_by() != null){
                apuesta.setIdModifiedBy(apuestaEntity.getModified_by().getId());
                apuesta.setModified(apuestaEntity.getModified());
            }


            apuestas.add(apuesta);
        }

        return apuestas;
    }

    @Transactional
    @Override
    public List<Apuesta> findApuestaByidsocergameEtapaOne(Long idJuego) {

        List<ApuestasEntity> apuestasEntity = apuestasRepository.findByidAndPrimera(idJuego);

        if (apuestasEntity == null) {
            throw new BadRequestException(324);

        }

        List<Apuesta> apuestas = new ArrayList<>(apuestasEntity.size());

        Apuesta apuesta;

        for (ApuestasEntity apuestaEntity : apuestasEntity) {



            apuesta = new Apuesta();
            apuesta.setId(apuestaEntity.getId());
            apuesta.setIdJuego(apuestaEntity.getId_juego().getId());
            apuesta.setSoccerGame(soccerGame.findSoccerGameById(apuesta.getIdJuego()));
            apuesta.setIdEvento(apuestaEntity.getId_evento().getId());
            apuesta.setEvento(eventsAdapter.findEventById(apuesta.getIdEvento()));
            apuesta.setEtapa(apuestaEntity.getEtapa());

            apuesta.setOpcionGanadora(apuestaEntity.getOpcion_ganadora());
            apuesta.setIdCreateBy(apuestaEntity.getCreated_by().getId());
            apuesta.setCreate(apuestaEntity.getCreated());
            if (apuestaEntity.getModified_by() != null){
                apuesta.setIdModifiedBy(apuestaEntity.getModified_by().getId());
                apuesta.setModified(apuestaEntity.getModified());
            }


            apuestas.add(apuesta);
        }

        return apuestas;
    }


    @Transactional
    @Override
    public List<Apuesta> findApuestaByidsocergameEtapatwo(Long idJuego) {

        List<ApuestasEntity> apuestasEntity = apuestasRepository.findByidAndSegunda(idJuego);

        if (apuestasEntity == null) {
            throw new BadRequestException(324);

        }

        List<Apuesta> apuestas = new ArrayList<>(apuestasEntity.size());

        Apuesta apuesta;

        for (ApuestasEntity apuestaEntity : apuestasEntity) {

            apuesta = new Apuesta();
            apuesta.setId(apuestaEntity.getId());
            apuesta.setIdJuego(apuestaEntity.getId_juego().getId());
            apuesta.setSoccerGame(soccerGame.findSoccerGameById(apuesta.getIdJuego()));
            apuesta.setIdEvento(apuestaEntity.getId_evento().getId());
            apuesta.setEvento(eventsAdapter.findEventById(apuesta.getIdEvento()));
            apuesta.setEtapa(apuestaEntity.getEtapa());

            apuesta.setOpcionGanadora(apuestaEntity.getOpcion_ganadora());
            apuesta.setIdCreateBy(apuestaEntity.getCreated_by().getId());
            apuesta.setCreate(apuestaEntity.getCreated());
            if (apuestaEntity.getModified_by() != null){
                apuesta.setIdModifiedBy(apuestaEntity.getModified_by().getId());
                apuesta.setModified(apuestaEntity.getModified());
            }


            apuestas.add(apuesta);
        }

        return apuestas;
    }
    @Transactional
    @Override
    public List<Apuesta> findApuestaByEvent(Long idEvento) {

        EventsEntity eventEntity = eventsRepository.findEventById(idEvento);

        if (eventEntity == null) {
            // Manejar el caso en que no se encuentre el evento
            throw new BadRequestException(324);
        }

        List<ApuestasEntity> apuestasEntities = eventEntity.getApuestas();

        if (apuestasEntities == null || apuestasEntities.isEmpty()) {
            // Manejar el caso en que no se encuentren apuestas asociados al juego
            throw new BadRequestException(326);
        }

        List<Apuesta> apuestas = new ArrayList<>(apuestasEntities.size());
        Apuesta apuesta;

        for (ApuestasEntity apuestaEntity : apuestasEntities) {

            apuesta = new Apuesta();
            apuesta.setId(apuestaEntity.getId());
            apuesta.setIdJuego(apuestaEntity.getId_juego().getId());
            apuesta.setSoccerGame(soccerGame.findSoccerGameById(apuesta.getIdJuego()));
            apuesta.setIdEvento(apuestaEntity.getId_evento().getId());
            apuesta.setEtapa(apuestaEntity.getEtapa());

            apuesta.setEvento(eventsAdapter.findEventById(apuesta.getIdEvento()));

            apuesta.setOpcionGanadora(apuestaEntity.getOpcion_ganadora());
            apuesta.setIdCreateBy(apuestaEntity.getCreated_by().getId());
            apuesta.setCreate(apuestaEntity.getCreated());
            if (apuestaEntity.getModified_by() != null){
                apuesta.setIdModifiedBy(apuestaEntity.getModified_by().getId());
                apuesta.setModified(apuestaEntity.getModified());
            }


            apuestas.add(apuesta);
        }

        return apuestas;
    }


    @Override
    @Transactional

    public void deleteApuesta(Long id) {


        List<PronosticoUserEntity> apronosticosEntities = pronosticoUserRepository.findAll();
        if(!apronosticosEntities.isEmpty() ){
            throw new BadRequestException(328);

        }



        poteRepository.deleteById(id);

        apuestasRepository.deleteById(id);

    }






}
