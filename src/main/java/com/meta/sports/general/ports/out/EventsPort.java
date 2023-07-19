package com.meta.sports.general.ports.out;

import com.meta.sports.events.domain.Event;
import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.Events;

import java.util.List;

public interface EventsPort {

    Events createEvent(Events event);

    void updateEvent(Events event);

    List<Events> findAllEvents();

    List<Events> findEventNotCreatebyGame(Long idApuesta);

    Events findEventById(Long id);



    void deleteEvent(Long id);
}
