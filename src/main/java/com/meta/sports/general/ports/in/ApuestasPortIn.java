package com.meta.sports.general.ports.in;

import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.Events;

import java.util.List;

public interface ApuestasPortIn {

    Apuesta createApuesta(Apuesta apuesta);

    List<Apuesta> createApuestaSegundoTiempo(Long idJuego);

    void updateApuesta(Apuesta apuesta);

    List<Apuesta> findAllApuestas();

    Apuesta findApuestaById(Long id);

    List<Apuesta> findApuestaBySoccerGame(Long idJuego);

    List<Apuesta> findApuestaByEvent(Long idEvento);

    List<Apuesta> findApuestaByidsocergameEtapaOne(Long idJuego);


    List<Apuesta> findApuestaByidsocergameEtapatwo(Long idjuego);

    void deleteApuesta(Long id);





}
