package com.meta.sports.general.ports.in;

import com.meta.sports.general.domain.ApuestaExacta;

import java.util.List;

public interface ApuestaExactaPortInd {

    List<ApuestaExacta> findApuestaExactaByPartido (Long idJuego);

    void apuestaGanadora(ApuestaExacta apuestaExacta);

    ApuestaExacta findApuestaExactaById(Long id);

    void changeStatus(Long id);




}
