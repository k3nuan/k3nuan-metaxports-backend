package com.meta.sports.general.ports.out;

import com.meta.sports.general.domain.Pote;

public interface PotePort {

    Pote findpotebyIdApuesta(Long id);
    Pote findpotebyIdApuestaExacta(Long id);
}
