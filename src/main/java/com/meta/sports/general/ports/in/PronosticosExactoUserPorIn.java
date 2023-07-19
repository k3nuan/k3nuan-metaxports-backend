package com.meta.sports.general.ports.in;

import com.meta.sports.general.domain.PronosticoExactoUser;

import java.util.List;

public interface PronosticosExactoUserPorIn {

    PronosticoExactoUser createPronosticoExacto(PronosticoExactoUser pronosticoExactoUser);


    List<PronosticoExactoUser> findAllPronosticosExactos();


    PronosticoExactoUser findPronosticoExactoById(Long id);

    List<PronosticoExactoUser> findPronosticoExactoByApuesta(Long idApuesta);

    List<PronosticoExactoUser> findPronosticoExactoByUser(Long idUser);
}
