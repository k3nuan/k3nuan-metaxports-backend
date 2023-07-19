package com.meta.sports.general.services;

import com.meta.sports.general.domain.Pote;
import com.meta.sports.general.ports.in.PotePortIn;
import com.meta.sports.general.ports.out.PotePort;
import com.meta.sports.global.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PoteService implements PotePortIn {

    @Autowired
    private  PotePort potePort;


    @Override
    public Pote findpotebyIdApuesta(Long idApuesta) {


        if (idApuesta == null) {
            throw new BadRequestException(325);
        }

        return potePort.findpotebyIdApuesta(idApuesta);
    }

    @Override
    public Pote findpotebyIdApuestaExacta(Long id) {

        if (id == null) {
            throw new BadRequestException(325);
        }

        return potePort.findpotebyIdApuestaExacta(id);
    }
}
