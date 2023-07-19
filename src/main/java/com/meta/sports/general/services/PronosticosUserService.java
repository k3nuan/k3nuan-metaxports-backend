package com.meta.sports.general.services;

import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.Events;
import com.meta.sports.general.domain.PronosticoUser;
import com.meta.sports.general.domain.SoccerGame;
import com.meta.sports.general.ports.in.PronosticosPorIn;
import com.meta.sports.general.ports.out.ApuestasPort;
import com.meta.sports.general.ports.out.PronosticoUserPort;
import com.meta.sports.general.ports.out.SoccerGamePort;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.global.exceptions.NotFoundException;
import com.meta.sports.user.adapter.out.persistence.FindUserAdapter;
import com.meta.sports.user.application.port.in.FindUser;
import com.meta.sports.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PronosticosUserService implements PronosticosPorIn {

    @Autowired
    private ApuestasPort apuestasPort;

    @Autowired
    private PronosticoUserPort pronosticoUserPort;

    @Autowired
    private FindUserAdapter findUser;

    @Override
    public PronosticoUser createPronostico(PronosticoUser pronosticoUser) {


        if (pronosticoUser == null) {
            throw new NotFoundException();
        }

        Apuesta verifyIdApuesta= apuestasPort.findApuestaById(pronosticoUser.getIdApuesta()) ;

        if (verifyIdApuesta == null) {
            throw new BadRequestException(330);
        }

        User verifyIdUser = findUser.byId(pronosticoUser.getIdUser()) ;

        if (verifyIdUser == null) {
            throw new BadRequestException(327);
        }

        return pronosticoUserPort.createPronostico(pronosticoUser);
    }

    @Override
    public List<PronosticoUser> findAllPronosticos() {
        return pronosticoUserPort.findAllPronosticos();
    }

    @Override
    public PronosticoUser findPronosticoById(Long id) {

        if (id == null) {
            throw new BadRequestException(150);
        }

        PronosticoUser verifyId = pronosticoUserPort.findPronosticoById(id) ;


        if (verifyId == null) {
            throw new BadRequestException(330);
        }


        return pronosticoUserPort.findPronosticoById(id);
    }

    @Override
    public List<PronosticoUser> findPronosticoByApuesta(Long idApuesta) {

        if (idApuesta == null) {
            throw new BadRequestException(330);
        }

        Apuesta verifyIdApuesta = apuestasPort.findApuestaById(idApuesta) ;


        if (verifyIdApuesta == null) {
            throw new BadRequestException(330);
        }


        return pronosticoUserPort.findPronosticoByApuesta(idApuesta);
    }

    @Override
    public List<PronosticoUser> findPronosticoByUser(Long idUser) {

        if (idUser == null) {
            throw new BadRequestException(327);
        }

        User verifyIdUser = findUser.byId(idUser) ;


        if (verifyIdUser == null) {
            throw new BadRequestException(327);
        }


        return pronosticoUserPort.findPronosticoByUser(idUser);
    }
}
