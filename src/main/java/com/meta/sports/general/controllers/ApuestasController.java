package com.meta.sports.general.controllers;


import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.Events;
import com.meta.sports.general.ports.in.ApuestasPortIn;
import com.meta.sports.general.ports.in.EventsPortIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApuestasController {

    private final ApuestasPortIn apuestasPortIn;

    @PostMapping("/api/v1/apuestas/createApuesta")
    public Apuesta createApuesta(@RequestBody Apuesta apuesta) {

        return apuestasPortIn.createApuesta(apuesta);

    }

    @PostMapping("/api/v1/apuestas/{idJuego}/createApuestaSegundoTiempo")
    public List<Apuesta> createApuestaSegundoTiempo(@PathVariable Long idJuego) {

        return apuestasPortIn.createApuestaSegundoTiempo(idJuego);

    }

    @PutMapping("/api/v1/apuestas/apuestaGanadora")
    public void apuestaGanadora(@RequestBody Apuesta apuesta) {

         apuestasPortIn.updateApuesta(apuesta);

    }

    @GetMapping("/api/v1/apuestas/apuestas")
    public List<Apuesta> apuestasList() {

        return apuestasPortIn.findAllApuestas();


    }

    @GetMapping("/api/v1/apuestas/{id}/apuesta")
    public Apuesta oneApuesta(@PathVariable Long id) {

        return apuestasPortIn.findApuestaById(id);

    }

    @GetMapping("/api/v1/apuestas/eventos/{idEvento}/apuesta")
    public List<Apuesta> apuestaByEvent(@PathVariable Long idEvento) {

        return apuestasPortIn.findApuestaByEvent(idEvento);

    }

    @GetMapping("/api/v1/apuestas/soccerGame/{idJuego}/apuesta")
    public List<Apuesta> apuestaBySoccerGame(@PathVariable Long idJuego) {

        return apuestasPortIn.findApuestaBySoccerGame(idJuego);

    }

    @GetMapping("/api/v1/apuestas/primerTiempo/{idJuego}/apuestas")
    public List<Apuesta> apuestaBySoccerGameEtapaOne(@PathVariable Long idJuego) {

        return apuestasPortIn.findApuestaByidsocergameEtapaOne(idJuego);

    }


    @GetMapping("/api/v1/apuestas/segundoTiempo/{idJuego}/apuestas")
    public List<Apuesta> apuestaBySoccerGameEtapaTwo(@PathVariable Long idJuego) {

        return apuestasPortIn.findApuestaByidsocergameEtapatwo(idJuego);

    }

    @DeleteMapping("/api/v1/apuestas/{id}/deleteApuesta")
    public void  deleteApuesta(@PathVariable Long id) {

        apuestasPortIn.deleteApuesta(id);

    }
}
