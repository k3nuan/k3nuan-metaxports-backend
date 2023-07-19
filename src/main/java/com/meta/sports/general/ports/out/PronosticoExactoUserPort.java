package com.meta.sports.general.ports.out;

import com.meta.sports.general.domain.PronosticoExactoUser;
import com.meta.sports.general.domain.PronosticoUser;

import java.util.List;

public interface PronosticoExactoUserPort {

    PronosticoExactoUser createPronosticoExacto(PronosticoExactoUser pronosticoExactoUser);


    List<PronosticoExactoUser> findAllPronosticosExactos();


    PronosticoExactoUser findPronosticoExactoById(Long id);

    List<PronosticoExactoUser> findPronosticoExactoByApuesta(Long idApuesta);

    List<PronosticoExactoUser> findPronosticoExactoByUser(Long idUser);

}
