package com.meta.sports.general.ports.out;

import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.ApuestaExacta;
import com.meta.sports.general.domain.PronosticoUser;

import java.util.List;

public interface ApuestaExactaPort {

    List<ApuestaExacta> findApuestaExactaByPartido (Long idJuego);

    ApuestaExacta findApuestaExactaById(Long id);

    void changeStatus(Long id);

    void apuestaGanadora(ApuestaExacta apuestaExacta);




}
