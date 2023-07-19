package com.meta.sports.general.ports.in;

import com.meta.sports.general.domain.Descuentos;

import java.util.List;

public interface DescuentosPortIn {

    Descuentos createDescuento(Descuentos descuentos);


    Descuentos createDescuentoAliado(Descuentos recargas);


    List<Descuentos> findAllDescuentos();

    List<Descuentos> findDescuentosByUser(Long idUser);


    List<Descuentos> findDescuentosByAliado(Long idUser);

    List<Descuentos> findDescuentoByUserAndAliado(Long idUSer, Long idAliado);

}
