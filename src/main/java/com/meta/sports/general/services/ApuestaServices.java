package com.meta.sports.general.services;


import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.Events;
import com.meta.sports.general.domain.SoccerGame;
import com.meta.sports.general.ports.in.ApuestasPortIn;
import com.meta.sports.general.ports.in.EventsPortIn;
import com.meta.sports.general.ports.out.ApuestasPort;
import com.meta.sports.general.ports.out.EventsPort;
import com.meta.sports.general.ports.out.SoccerGamePort;
import com.meta.sports.general.ports.out.TournamentPort;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.global.exceptions.NotFoundException;
import com.meta.sports.user.application.port.in.FindUser;
import com.meta.sports.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApuestaServices implements ApuestasPortIn {

    @Autowired
    private ApuestasPort apuestasPort;

    @Autowired
    private SoccerGamePort soccerGamePort;

    @Autowired
    private FindUser findUser;

    @Autowired
    private EventsPort eventsPort;


    @Override
    public Apuesta createApuesta(Apuesta apuesta) {


        if (apuesta == null) {
            throw new BadRequestException(326);
        }

        SoccerGame verifyIdSoccerGame = soccerGamePort.findSoccerGameById(apuesta.getIdJuego()) ;

        if (verifyIdSoccerGame == null) {
            throw new BadRequestException(324);
        }

        Events verifyIdEvent = eventsPort.findEventById(apuesta.getIdEvento()) ;

        if (verifyIdEvent == null) {
            throw new BadRequestException(325);
        }

        User verifyIdUser = findUser.byId(apuesta.getIdCreateBy()) ;

        if (verifyIdUser == null) {
            throw new BadRequestException(327);
        }

        return apuestasPort.createApuesta(apuesta);
    }

    @Override
    public List<Apuesta> createApuestaSegundoTiempo(Long idJuego) {

        if (idJuego == null) {
            throw new BadRequestException(324);
        }

        SoccerGame verifyIdSoccerGame = soccerGamePort.findSoccerGameById(idJuego) ;


        if (verifyIdSoccerGame == null) {
            throw new BadRequestException(324);
        }


        return apuestasPort.createApuestaSegundoTiempo(idJuego);
    }

    @Override
    public void updateApuesta(Apuesta apuesta) {

        if (apuesta.getId() == null) {
            throw new BadRequestException(323);
        }

        User verifyId = findUser.byId(apuesta.getIdModifiedBy()) ;


        if (verifyId == null) {
            throw new BadRequestException(323);
        }


         apuestasPort.apuestaGanadora(apuesta);

    }

    @Override
    public List<Apuesta> findAllApuestas() {
        return apuestasPort.findAllApuestas();

    }

    @Override
    public Apuesta findApuestaById(Long id) {

        if (id == null) {
            throw new BadRequestException(323);
        }

        Apuesta verifyId = apuestasPort.findApuestaById(id) ;


        if (verifyId == null) {
            throw new BadRequestException(323);
        }


        return apuestasPort.findApuestaById(id);
    }

    @Override
    public List<Apuesta> findApuestaBySoccerGame(Long idJuego) {

        if (idJuego == null) {
            throw new BadRequestException(324);
        }

        SoccerGame verifyIdSoccerGame = soccerGamePort.findSoccerGameById(idJuego) ;


        if (verifyIdSoccerGame == null) {
            throw new BadRequestException(324);
        }


        return apuestasPort.findApuestaBySoccerGame(idJuego);
    }

    @Override
    public List<Apuesta> findApuestaByEvent(Long idEvento) {

        if (idEvento == null) {
            throw new BadRequestException(325);
        }

        Events verifyIdEvent = eventsPort.findEventById(idEvento) ;


        if (verifyIdEvent == null) {
            throw new BadRequestException(325);
        }

        return apuestasPort.findApuestaByEvent(idEvento);
    }

    @Override
    public List<Apuesta> findApuestaByidsocergameEtapaOne(Long idJuego) {

        if (idJuego == null) {
            throw new BadRequestException(324);
        }

        SoccerGame verifyIdSoccerGame = soccerGamePort.findSoccerGameById(idJuego) ;


        if (verifyIdSoccerGame == null) {
            throw new BadRequestException(324);
        }
        return apuestasPort.findApuestaByidsocergameEtapaOne(idJuego);

    }


    @Override
    public List<Apuesta> findApuestaByidsocergameEtapatwo(Long idJuego) {

        if (idJuego == null) {
            throw new BadRequestException(324);
        }

        SoccerGame verifyIdSoccerGame = soccerGamePort.findSoccerGameById(idJuego) ;


        if (verifyIdSoccerGame == null) {
            throw new BadRequestException(324);
        }
        return apuestasPort.findApuestaByidsocergameEtapatwo(idJuego);

    }

    @Override
    public void deleteApuesta(Long id) {
        apuestasPort.deleteApuesta(id);
    }


}
