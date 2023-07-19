package com.meta.sports.general.controllers;

import com.meta.sports.general.domain.Descuentos;
import com.meta.sports.general.domain.Recargas;
import com.meta.sports.general.ports.in.DescuentosPortIn;
import com.meta.sports.general.ports.in.RecargasPortIn;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DescuentosController {


    private final DescuentosPortIn descuentosPortIn;

    @PostMapping("/api/v1/descuentos/createDescuento")
    public Descuentos createDescuento(@RequestBody Descuentos descuentos) {

        return descuentosPortIn.createDescuento(descuentos);

    }


    @PostMapping("/api/v1/descuentos/aliados/createDescuento")
    public Descuentos createDescuentoAliado(@RequestBody Descuentos descuentos) {

        return descuentosPortIn.createDescuentoAliado(descuentos);

    }


    @GetMapping("/api/v1/descuentos/descuentos")
    public List<Descuentos> descuentosList() {

        return descuentosPortIn.findAllDescuentos();


    }

    @GetMapping("/api/v1/descuentos/aliados/{idUser}")
    public List<Descuentos> descuentoAliadosList(@PathVariable Long idUser) {

        return descuentosPortIn.findDescuentosByAliado(idUser);

    }

    @GetMapping("/api/v1/descuentos/{idUser}/descuentos")
    public List<Descuentos> descuentosbyUSer(@PathVariable Long idUser) {

        return descuentosPortIn.findDescuentosByUser(idUser);

    }

    @GetMapping("/api/v1/descuentos/aliados/{idAliado}/{idUSer}")
    public List<Descuentos>  descuentosAliadosByUser(@PathVariable Long idUSer ,@PathVariable Long idAliado) {

        return descuentosPortIn.findDescuentoByUserAndAliado(idUSer,idAliado);

    }
}
