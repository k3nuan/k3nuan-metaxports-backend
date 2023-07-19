package com.meta.sports.general.ports.in;

import com.meta.sports.general.domain.PronosticoUser;

import java.util.List;

public interface PronosticosPorIn {

    PronosticoUser createPronostico(PronosticoUser pronosticoUser);


    List<PronosticoUser> findAllPronosticos();


    PronosticoUser findPronosticoById(Long id);

    List<PronosticoUser> findPronosticoByApuesta(Long idApuesta);

    List<PronosticoUser> findPronosticoByUser(Long idUser);
}
