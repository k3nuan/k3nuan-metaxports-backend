package com.meta.sports.general.services;

import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.ApuestaExacta;
import com.meta.sports.general.domain.PronosticoExactoUser;
import com.meta.sports.general.domain.PronosticoUser;
import com.meta.sports.general.ports.in.PronosticosExactoUserPorIn;
import com.meta.sports.general.ports.out.ApuestaExactaPort;
import com.meta.sports.general.ports.out.ApuestasPort;
import com.meta.sports.general.ports.out.PronosticoExactoUserPort;
import com.meta.sports.general.ports.out.PronosticoUserPort;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.global.exceptions.NotFoundException;
import com.meta.sports.user.adapter.out.persistence.FindUserAdapter;
import com.meta.sports.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PronosticoExactoUserServices implements PronosticosExactoUserPorIn {
    @Autowired
    private ApuestaExactaPort apuestasPort;

    @Autowired
    private PronosticoExactoUserPort pronosticoUserPort;

    @Autowired
    private FindUserAdapter findUser;

    @Override
    public PronosticoExactoUser createPronosticoExacto(PronosticoExactoUser pronosticoExactoUser) {

        if (pronosticoExactoUser == null) {
            throw new NotFoundException();
        }

        ApuestaExacta verifyIdApuesta= apuestasPort.findApuestaExactaById(pronosticoExactoUser.getIdApuestaExacta()) ;

        if (verifyIdApuesta == null) {
            throw new BadRequestException(330);
        }

        User verifyIdUser = findUser.byId(pronosticoExactoUser.getIdUser()) ;

        if (verifyIdUser == null) {
            throw new BadRequestException(327);
        }

        return pronosticoUserPort.createPronosticoExacto(pronosticoExactoUser);
    }

    @Override
    public List<PronosticoExactoUser> findAllPronosticosExactos() {
        return pronosticoUserPort.findAllPronosticosExactos();
    }

    @Override
    public PronosticoExactoUser findPronosticoExactoById(Long id) {

        if (id == null) {
            throw new BadRequestException(150);
        }

        PronosticoExactoUser verifyId = pronosticoUserPort.findPronosticoExactoById(id) ;


        if (verifyId == null) {
            throw new BadRequestException(330);
        }


        return pronosticoUserPort.findPronosticoExactoById(id);
    }

    @Override
    public List<PronosticoExactoUser> findPronosticoExactoByApuesta(Long idApuesta) {

        if (idApuesta == null) {
            throw new BadRequestException(330);
        }

        ApuestaExacta verifyIdApuesta = apuestasPort.findApuestaExactaById(idApuesta) ;


        if (verifyIdApuesta == null) {
            throw new BadRequestException(330);
        }


        return pronosticoUserPort.findPronosticoExactoByApuesta(idApuesta);
    }

    @Override
    public List<PronosticoExactoUser> findPronosticoExactoByUser(Long idUser) {

        if (idUser == null) {
            throw new BadRequestException(327);
        }

        User verifyIdUser = findUser.byId(idUser) ;


        if (verifyIdUser == null) {
            throw new BadRequestException(327);
        }


        return pronosticoUserPort.findPronosticoExactoByUser(idUser);
    }
}
