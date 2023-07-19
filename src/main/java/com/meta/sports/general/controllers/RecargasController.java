package com.meta.sports.general.controllers;


import com.meta.sports.general.domain.Events;
import com.meta.sports.general.domain.Recargas;
import com.meta.sports.general.ports.in.EventsPortIn;
import com.meta.sports.general.ports.in.RecargasPortIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecargasController {

    private final RecargasPortIn recargasPortIn;


    @PostMapping("/api/v1/recargas/createRecarga")
    public Recargas createRecarga(@RequestBody Recargas recargas) {

        return recargasPortIn.createRecarga(recargas);

    }


    @GetMapping("/api/v1/recargas/recargas")
    public List<Recargas> RecargaList() {

        return recargasPortIn.findAllRecargas();


    }

    @GetMapping("/api/v1/recargas/{idUser}/recargas")
    public List<Recargas> recargasbyUSer(@PathVariable Long idUser) {

        return recargasPortIn.findRecargaByUser(idUser);

    }

    @PostMapping("/api/v1/recargas/aliados/createRecarga")
    public Recargas createRecargaAliados(@RequestBody Recargas recargas) {

        return recargasPortIn.crearRecargaAliado(recargas);

    }

    @GetMapping("/api/v1/recargas/aliados/{idAliado}")
    public List<Recargas>  recargaAliados(@PathVariable Long idAliado) {

        System.out.println("en ejecucion");
        return recargasPortIn.findRecargaAllrecargaAliado(idAliado);

    }

    @GetMapping("/api/v1/recargas/aliados/{idAliado}/{idUSer}")
    public List<Recargas>  recargaAliadosByUser(@PathVariable Long idUSer ,@PathVariable Long idAliado) {

        return recargasPortIn.findRecargaByUserAndAliado(idUSer,idAliado);

    }

}
