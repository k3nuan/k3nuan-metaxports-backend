package com.meta.sports.general.ports.in;

import com.meta.sports.general.domain.Pote;

public interface PotePortIn {

    Pote findpotebyIdApuesta(Long idApuesta);

    Pote findpotebyIdApuestaExacta(Long id);


}
