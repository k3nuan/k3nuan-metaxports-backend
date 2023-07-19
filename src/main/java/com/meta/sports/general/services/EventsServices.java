package com.meta.sports.general.services;

import com.meta.sports.general.domain.Events;
import com.meta.sports.general.domain.Tournament;
import com.meta.sports.general.ports.in.EventsPortIn;
import com.meta.sports.general.ports.out.EventsPort;
import com.meta.sports.general.ports.out.TournamentPort;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.global.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EventsServices implements EventsPortIn {

    @Autowired
    private EventsPort eventsPort;

    @Override
    public Events createEvent(Events events) {

        if (events == null) {
            throw new NotFoundException();
        }

        return eventsPort.createEvent(events);

    }

    @Override
    public void updateEvent(Events events) {

        if (events == null) {
            throw new NotFoundException();
        }

        Events eventsOp = eventsPort.findEventById(events.getId());

        if (eventsOp == null) {
            throw new BadRequestException(325);
        }

        eventsPort.updateEvent(events);

    }

    @Override
    public List<Events> findAllEvents() {
       return eventsPort.findAllEvents();
    }

    @Override
    public List<Events> findEventNotCreatebyGame(Long idJuego) {
        if (idJuego == null) {
            throw new BadRequestException(325);
        }
        return eventsPort.findEventNotCreatebyGame(idJuego);
    }

    @Override
    public Events findEventById(Long id) {

        if (id == null) {
            throw new BadRequestException(325);
        }

        boolean opEvents = idExist(id);


        if (opEvents == true) {
            throw new BadRequestException(325);
        }


        return eventsPort.findEventById(id);
    }


    @Override
    public void deleteEvent(Long id) {
        if (id == null) {
            throw new BadRequestException(325);

        }

        boolean opEvents = idExist(id);

        if (opEvents == true) {
            throw new BadRequestException(325);
        }

        eventsPort.deleteEvent(id);
    }

    private boolean idExist(Long id) {
        try {
            return eventsPort.findEventById(id) == null && eventsPort.findEventById(id) == null;
        } catch (NotFoundException e) {
            return true;
        }
    }
}
