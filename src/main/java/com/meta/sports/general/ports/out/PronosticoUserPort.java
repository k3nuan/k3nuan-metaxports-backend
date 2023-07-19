package com.meta.sports.general.ports.out;

import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.PronosticoUser;

import java.util.List;

public interface PronosticoUserPort {

    PronosticoUser createPronostico(PronosticoUser pronosticoUser);


    List<PronosticoUser> findAllPronosticos();


    PronosticoUser findPronosticoById(Long id);

    List<PronosticoUser> findPronosticoByApuesta(Long idApuesta);

    List<PronosticoUser> findPronosticoByUser(Long idUser);


}
