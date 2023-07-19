package com.meta.sports.general.ports.out;

import com.meta.sports.general.domain.Apuesta;


import java.util.List;

public interface ApuestasPort {

    Apuesta createApuesta(Apuesta apuesta);

    List<Apuesta> createApuestaSegundoTiempo(Long idJuego);


    void apuestaGanadora(Apuesta apuesta);

    List<Apuesta> findAllApuestas();


    Apuesta findApuestaById(Long id);

    List<Apuesta> findApuestaBySoccerGame(Long idJuego);

    List<Apuesta> findApuestaByEvent(Long idEvento);





    List<Apuesta> findApuestaByidsocergameEtapaOne(Long idEvento);

    List<Apuesta> findApuestaByidsocergameEtapatwo(Long idjuego);

    void deleteApuesta(Long id);





}
