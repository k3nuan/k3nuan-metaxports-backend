package com.meta.sports.general.controllers;


import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.PronosticoUser;
import com.meta.sports.general.ports.in.ApuestasPortIn;
import com.meta.sports.general.ports.in.PronosticosPorIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PronosticoUserController {

    private final PronosticosPorIn pronosticosPorIn;

    @PostMapping("/api/v1/pronosticos/createPronostico")
    public PronosticoUser createPronostico(@RequestBody PronosticoUser pronosticoUser) {

        return pronosticosPorIn.createPronostico(pronosticoUser);

    }

    @GetMapping("/api/v1/pronosticos/pronosticos")
    public List<PronosticoUser> pronosticoList() {

        return pronosticosPorIn.findAllPronosticos();


    }

    @GetMapping("/api/v1/pronosticos/{id}/pronostico")
    public PronosticoUser onePronosticos(@PathVariable Long id) {

        return pronosticosPorIn.findPronosticoById(id);

    }

    @GetMapping("/api/v1/pronosticos/apuesta/{idApuesta}/pronostico")
    public List<PronosticoUser> pronosticoByApuesta(@PathVariable Long idApuesta) {

        return pronosticosPorIn.findPronosticoByApuesta(idApuesta);

    }

    @GetMapping("/api/v1/pronosticos/user/{idUser}/pronostico")
    public List<PronosticoUser> pronosticoByUser(@PathVariable Long idUser) {

        return pronosticosPorIn.findPronosticoByUser(idUser);

    }
}
