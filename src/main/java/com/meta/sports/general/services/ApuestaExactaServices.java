package com.meta.sports.general.services;

import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.ApuestaExacta;
import com.meta.sports.general.domain.SoccerGame;
import com.meta.sports.general.ports.in.ApuestaExactaPortInd;
import com.meta.sports.general.ports.in.ApuestasPortIn;
import com.meta.sports.general.ports.out.ApuestaExactaPort;
import com.meta.sports.general.ports.out.ApuestasPort;
import com.meta.sports.general.ports.out.SoccerGamePort;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.user.application.port.in.FindUser;
import com.meta.sports.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApuestaExactaServices implements ApuestaExactaPortInd {


    @Autowired
    private ApuestaExactaPort apuestaExactaPort;

    @Autowired
    private SoccerGamePort soccerGamePort;

    @Autowired
    private FindUser findUser;

    @Override
    public List<ApuestaExacta> findApuestaExactaByPartido(Long idJuego) {
        if (idJuego == null) {
            throw new BadRequestException(324);
        }

        SoccerGame verifyIdSoccerGame = soccerGamePort.findSoccerGameById(idJuego) ;


        if (verifyIdSoccerGame == null) {
            throw new BadRequestException(324);
        }


        return apuestaExactaPort.findApuestaExactaByPartido(idJuego);
    }

    @Override
    public void apuestaGanadora(ApuestaExacta apuestaExacta) {

        if (apuestaExacta.getId() == null) {
            throw new BadRequestException(323);
        }

        User verifyId = findUser.byId(apuestaExacta.getIdModifiedBy()) ;


        if (verifyId == null) {
            throw new BadRequestException(323);
        }


        apuestaExactaPort.apuestaGanadora(apuestaExacta);
    }

    @Override
    public ApuestaExacta findApuestaExactaById(Long id) {

        if (id == null) {
            throw new BadRequestException(323);
        }

        ApuestaExacta verifyId = apuestaExactaPort.findApuestaExactaById(id) ;


        if (verifyId == null) {
            throw new BadRequestException(323);
        }


        return apuestaExactaPort.findApuestaExactaById(id);
    }

    @Override
    public void changeStatus(Long id) {
        if (id == null) {
            throw new BadRequestException(324);
        }

        SoccerGame verifyIdSoccerGame = soccerGamePort.findSoccerGameById(id) ;


        if (verifyIdSoccerGame == null) {
            throw new BadRequestException(324);
        }


         apuestaExactaPort.changeStatus(id);
    }
}
