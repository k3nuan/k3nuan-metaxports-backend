package com.meta.sports.general.controllers;


import com.meta.sports.general.domain.PronosticoExactoUser;
import com.meta.sports.general.domain.PronosticoUser;
import com.meta.sports.general.ports.in.PronosticosExactoUserPorIn;
import com.meta.sports.general.ports.in.PronosticosPorIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PronosticoExactoUserController {

    private final PronosticosExactoUserPorIn pronosticosPorIn;

    @PostMapping("/api/v1/pronosticosExactos/createPronostico")
    public PronosticoExactoUser createpronosticosExacto(@RequestBody PronosticoExactoUser pronosticoUser) {

        return pronosticosPorIn.createPronosticoExacto(pronosticoUser);

    }

    @GetMapping("/api/v1/pronosticosExactos/pronosticosExactos")
    public List<PronosticoExactoUser> pronosticoExactoList() {

        return pronosticosPorIn.findAllPronosticosExactos();


    }

    @GetMapping("/api/v1/pronosticosExactos/{id}/pronostico")
    public PronosticoExactoUser onePronosticoExacto(@PathVariable Long id) {

        return pronosticosPorIn.findPronosticoExactoById(id);

    }

    @GetMapping("/api/v1/pronosticosExactos/juego/{idApuesta}/pronostico")
    public List<PronosticoExactoUser> pronosticoExactoByJuego(@PathVariable Long idJuego) {

        return pronosticosPorIn.findPronosticoExactoByApuesta(idJuego);

    }

    @GetMapping("/api/v1/pronosticosExactos/user/{idUser}/pronostico")
    public List<PronosticoExactoUser> pronosticoExactoByUser(@PathVariable Long idUser) {

        return pronosticosPorIn.findPronosticoExactoByUser(idUser);

    }
}
