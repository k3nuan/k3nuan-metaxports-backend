package com.meta.sports.general.ports.in;

import com.meta.sports.general.domain.Events;
import com.meta.sports.general.domain.Tournament;

import java.util.List;

public interface EventsPortIn {

    Events createEvent(Events events);

    void updateEvent(Events events);

    List<Events> findAllEvents();

    List<Events> findEventNotCreatebyGame(Long idApuesta);


    Events findEventById(Long id);

    void deleteEvent(Long id);
}
