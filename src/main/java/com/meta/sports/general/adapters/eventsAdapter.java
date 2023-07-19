package com.meta.sports.general.adapters;


import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.Events;
import com.meta.sports.general.domain.SoccerGame;
import com.meta.sports.general.domain.Tournament;
import com.meta.sports.general.entitys.ApuestasEntity;
import com.meta.sports.general.entitys.EventsEntity;
import com.meta.sports.general.entitys.SoccerGameEntity;
import com.meta.sports.general.entitys.TournamentEntity;
import com.meta.sports.general.ports.out.EventsPort;
import com.meta.sports.general.repositorys.EventsRepository;
import com.meta.sports.general.repositorys.SoccerGameRepository;
import com.meta.sports.general.repositorys.TournamentRepository;
import com.meta.sports.general.services.ApuestaServices;
import com.meta.sports.global.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class eventsAdapter implements EventsPort {

    private final EventsRepository eventsRepository;

    private final SoccerGameRepository soccerGameRepository;






    @Override
    @Transactional

    public Events createEvent(Events event) {
        EventsEntity entity = new EventsEntity();

        entity.setDescripcion(event.getDescripcion());
        entity.setOpcion_uno(event.getOpcionUno());
        entity.setOpcion_dos(event.getOpcionDos());
        if (event.getOpcionTres()== null){
            entity.setOpcion_tres(null);
        }else {
            entity.setOpcion_tres(event.getOpcionTres());
        }

        eventsRepository.saveAndFlush(entity);

        event.setId(entity.getId());

        return event;
    }

    @Override
    @Transactional

    public void updateEvent(Events event) {

        EventsEntity entity = eventsRepository.findEventById(event.getId());


        entity.setDescripcion(event.getDescripcion());
        entity.setOpcion_uno(event.getOpcionUno());
        entity.setOpcion_dos(event.getOpcionDos());

        if (event.getOpcionTres()== null){
            entity.setOpcion_tres(null);
        }else {
            entity.setOpcion_tres(event.getOpcionTres());
        }

        eventsRepository.saveAndFlush(entity);

    }

    @Override
    public List<Events> findAllEvents() {

        List<EventsEntity> eventsEntities = eventsRepository.findAll();
        List<Events>  events = new ArrayList<>();
        eventsEntities.forEach(eventEntities-> {
            Events event = new Events();
            event.setId(eventEntities.getId());
            event.setDescripcion(eventEntities.getDescripcion());
            event.setOpcionUno(eventEntities.getOpcion_uno());
            event.setOpcionDos(eventEntities.getOpcion_dos());
            event.setOpcionTres(eventEntities.getOpcion_tres());
            events.add(event);
        });
        return events;
    }
    @Transactional
    @Override
    public List<Events> findEventNotCreatebyGame(Long idJuego) {

        List<Events> eventsNotCreated = new ArrayList<>();

        // Obtener todas las apuestas creadas para el juego
        SoccerGameEntity soccerGameEntity = soccerGameRepository.findSoccerGameById(idJuego);

        if (soccerGameEntity == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(324);

        }

        List<ApuestasEntity> apuestasEntities = soccerGameEntity.getApuestas();



        List<Apuesta> apuestasCreadas = new ArrayList<>(apuestasEntities.size());
        Apuesta apuesta;

        for (ApuestasEntity apuestaEntity : apuestasEntities) {

            apuesta = new Apuesta();
            apuesta.setId(apuestaEntity.getId());
            apuesta.setIdJuego(apuestaEntity.getId_juego().getId());
            apuesta.setIdEvento(apuestaEntity.getId_evento().getId());
            apuesta.setEtapa(apuestaEntity.getEtapa());
            apuesta.setOpcionGanadora(apuestaEntity.getOpcion_ganadora());
            apuesta.setIdCreateBy(apuestaEntity.getCreated_by().getId());
            apuesta.setCreate(apuestaEntity.getCreated());
            if (apuestaEntity.getModified_by() != null){
                apuesta.setIdModifiedBy(apuestaEntity.getModified_by().getId());
                apuesta.setModified(apuestaEntity.getModified());
            }


            apuestasCreadas.add(apuesta);
        }

        // Obtener todos los eventos posibles
        List<Events> allEvents = findAllEvents();

        // Verificar cuáles eventos no han sido creados aún
        for (Events event : allEvents) {
            boolean eventCreated = false;
            for (Apuesta apuestaOP : apuestasCreadas) {
                if (apuestaOP.getIdEvento().equals(event.getId())) {
                    eventCreated = true;
                    break;
                }
            }
            if (!eventCreated) {
                eventsNotCreated.add(event);
            }
        }

        return eventsNotCreated;
    }

    @Override
    public Events findEventById(Long id) {
        EventsEntity entity= eventsRepository.findEventById(id);
        if (entity != null){
            Events events = new Events();
            events.setId(entity.getId());
            events.setDescripcion(entity.getDescripcion());
            events.setOpcionUno(entity.getOpcion_uno());
            events.setOpcionDos(entity.getOpcion_dos());
            events.setOpcionTres(entity.getOpcion_tres());
            return events;
        }else {
            return null;
        }
    }

    @Override
    @Transactional

    public void deleteEvent(Long id) {

        eventsRepository.deleteById(id);

    }
}
