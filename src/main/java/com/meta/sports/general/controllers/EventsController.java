package com.meta.sports.general.controllers;


import com.meta.sports.general.domain.Events;
import com.meta.sports.general.domain.SoccerGame;
import com.meta.sports.general.ports.in.EventsPortIn;
import com.meta.sports.general.ports.in.SoccerGamePortIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventsController {

    private final EventsPortIn eventsPortIn;

    @PostMapping("/api/v1/events/createEvent")
    public Events createEvent(@RequestBody Events events) {

        return eventsPortIn.createEvent(events);

    }


    @GetMapping("/api/v1/events/events")
    public List<Events> eventsList() {

        return eventsPortIn.findAllEvents();


    }

    @GetMapping("/api/v1/events/{idJuego}/eventsNotCreate")
    public List<Events> eventsNotCreate(@PathVariable Long idJuego) {

        return eventsPortIn.findEventNotCreatebyGame(idJuego);

    }

    @GetMapping("/api/v1/events/{id}/event")
    public Events oneEvent(@PathVariable Long id) {

        return eventsPortIn.findEventById(id);

    }



    @PutMapping("/api/v1/events/updateEvent")
    public void  updateEvents(@RequestBody Events events) {

        eventsPortIn.updateEvent(events);

    }

    @DeleteMapping("/api/v1/events/{id}/deleteEvents")
    public void  deleteEvent(@PathVariable Long id) {

        eventsPortIn.deleteEvent(id);

    }
}
