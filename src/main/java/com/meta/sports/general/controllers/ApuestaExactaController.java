package com.meta.sports.general.controllers;

import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.ApuestaExacta;
import com.meta.sports.general.ports.in.ApuestaExactaPortInd;
import com.meta.sports.general.ports.in.ApuestasPortIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ApuestaExactaController {

    private final ApuestaExactaPortInd apuestaExactaPortInd;


    @GetMapping("/api/v1/apuestasExactas/{idJuego}/apuestaExactas")
    public List<ApuestaExacta> apuestasByJuego(@PathVariable Long idJuego) {

        return apuestaExactaPortInd.findApuestaExactaByPartido(idJuego);

    }

    @GetMapping("/api/v1/apuestasExactas/{idApuesta}/apuestaExacta")
    public ApuestaExacta apuestasById(@PathVariable Long idApuesta) {

        return apuestaExactaPortInd.findApuestaExactaById(idApuesta);

    }

    @PutMapping("/api/v1/apuestasExactas/apuestaGanadora")
    public void apuestaGanadora(@RequestBody ApuestaExacta apuestaExacta) {

        apuestaExactaPortInd.apuestaGanadora(apuestaExacta);

    }

    @PutMapping("/api/v1/apuestasExactas/{id}/changeStatus")
    public void changeStatus(@PathVariable Long id) {

        apuestaExactaPortInd.changeStatus(id);

    }
}
