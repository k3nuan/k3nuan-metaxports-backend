package com.meta.sports.general.controllers;


import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.Pote;
import com.meta.sports.general.ports.in.ApuestasPortIn;
import com.meta.sports.general.ports.in.PotePortIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PoteController {

    private final PotePortIn potePortIn;

    @GetMapping("/api/v1/potes/{idApuesta}/pote")
    public Pote onePoteByApuesta(@PathVariable Long idApuesta) {

        return potePortIn.findpotebyIdApuesta(idApuesta);

    }

    @GetMapping("/api/v1/potes/{idApuestaExacta}/poteApuestaExacta")
    public Pote onePoteByApuestaExacta(@PathVariable Long idApuestaExacta) {

        return potePortIn.findpotebyIdApuestaExacta(idApuestaExacta);

    }

}
