package com.meta.sports.general.ports.in;

import com.meta.sports.general.domain.Recargas;

import java.util.List;

public interface RecargasPortIn {

    Recargas createRecarga(Recargas recargas);


    List<Recargas> findAllRecargas();


    List<Recargas> findRecargaByUser(Long idUser);

    Recargas crearRecargaAliado(Recargas recargas);

    List<Recargas> findRecargaAllrecargaAliado(Long idUser);


    List<Recargas> findRecargaByUserAndAliado(Long idUser, Long idAliado);




}
