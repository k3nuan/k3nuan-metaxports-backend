package com.meta.sports.general.ports.out;

import com.meta.sports.general.domain.Events;
import com.meta.sports.general.domain.Recargas;

import java.util.List;

public interface RecargasPort {

    Recargas createRecarga(Recargas recargas);


    List<Recargas> findAllRecargas();


    List<Recargas> findRecargaByUser(Long idUser);


    Recargas crearRecargaAliado(Recargas recargas);



    List<Recargas> findRecargaAllrecargaAliado(Long idUser);


    List<Recargas> findRecargaByUserAndAliado(Long idUSer, Long idAliado);








}
