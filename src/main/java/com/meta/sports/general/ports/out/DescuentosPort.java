package com.meta.sports.general.ports.out;

import com.meta.sports.general.domain.Descuentos;
import com.meta.sports.general.domain.Recargas;

import java.util.List;

public interface DescuentosPort {


    Descuentos createDescuento(Descuentos descuentos);

    Descuentos createDescuentoAliado(Descuentos recargas);


    List<Descuentos> findAllDescuentos();

    List<Descuentos> findDescuentosByUser(Long idUser);



    List<Descuentos> findDescuentosByAliado(Long idUser);

    List<Descuentos> findDescuentoByUserAndAliado(Long idUSer, Long idAliado);
}
