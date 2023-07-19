package com.meta.sports.general.services;

import com.meta.sports.general.domain.Events;
import com.meta.sports.general.domain.Recargas;
import com.meta.sports.general.domain.SoccerGame;
import com.meta.sports.general.ports.in.RecargasPortIn;
import com.meta.sports.general.ports.out.ApuestasPort;
import com.meta.sports.general.ports.out.RecargasPort;
import com.meta.sports.general.ports.out.SoccerGamePort;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.user.application.port.in.FindUser;
import com.meta.sports.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecargasServices implements RecargasPortIn {

    @Autowired
    private RecargasPort recargasPort;

    @Autowired
    private FindUser findUser;



    @Override
    public Recargas createRecarga(Recargas recargas) {

        if (recargas == null) {
            throw new BadRequestException(340);
        }

        User verifyIdUser = findUser.byId(recargas.getIdUsuario()) ;

        if (verifyIdUser == null) {
            throw new BadRequestException(324);
        }

        User verifyIdUserModifies = findUser.byId(recargas.getIdCreatedBy()) ;

        if (verifyIdUserModifies == null) {
            throw new BadRequestException(324);
        }

        return recargasPort.createRecarga(recargas);

    }

    @Override
    public List<Recargas> findAllRecargas() {
        return recargasPort.findAllRecargas();
    }

    @Override
    public List<Recargas> findRecargaByUser(Long idUser) {

        if (idUser == null) {
            throw new BadRequestException(327);
        }

        User verifyIdUser = findUser.byId(idUser) ;

        if (verifyIdUser == null) {
            throw new BadRequestException(324);
        }

        return recargasPort.findRecargaByUser(idUser);

    }

    @Override
    public Recargas crearRecargaAliado(Recargas recargas) {


            if (recargas == null) {
                throw new BadRequestException(340);
            }

            User verifyIdUser = findUser.byId(recargas.getIdUsuario()) ;

            if (verifyIdUser == null) {
                throw new BadRequestException(324);
            }

            User verifyIdUserModifies = findUser.byId(recargas.getIdCreatedBy()) ;

            if (verifyIdUserModifies == null) {
                throw new BadRequestException(324);
            }

            return recargasPort.crearRecargaAliado(recargas);


    }

    @Override
    public List<Recargas> findRecargaAllrecargaAliado(Long idUser) {

        if (idUser == null) {
            throw new BadRequestException(327);
        }

        User verifyIdUser = findUser.byId(idUser) ;

        if (verifyIdUser == null) {
            throw new BadRequestException(324);
        }

        return recargasPort.findRecargaAllrecargaAliado(idUser);
    }

    @Override
    public List<Recargas> findRecargaByUserAndAliado(Long idUSer, Long idAliado) {

        if (idUSer == null) {
            throw new BadRequestException(327);
        }

        User verifyIdUser = findUser.byId(idUSer) ;

        if (verifyIdUser == null) {
            throw new BadRequestException(324);
        }

        if (idAliado == null) {
            throw new BadRequestException(327);
        }

        User verifyIdAliador = findUser.byId(idAliado) ;

        if (verifyIdAliador == null) {
            throw new BadRequestException(324);
        }

        return recargasPort.findRecargaByUserAndAliado(idUSer,idAliado);
    }
}
