package com.meta.sports.general.services;

import com.meta.sports.general.domain.Descuentos;
import com.meta.sports.general.ports.in.DescuentosPortIn;
import com.meta.sports.general.ports.out.DescuentosPort;
import com.meta.sports.general.ports.out.RecargasPort;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.user.application.port.in.FindUser;
import com.meta.sports.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DescuentosServices  implements DescuentosPortIn {

    @Autowired
    private DescuentosPort descuentosPort;

    @Autowired
    private FindUser findUser;

    @Override
    public Descuentos createDescuento(Descuentos descuentos) {

        if (descuentos == null) {
            throw new BadRequestException(340);
        }

        User verifyIdUser = findUser.byId(descuentos.getIdUsuario()) ;

        if (verifyIdUser == null) {
            throw new BadRequestException(324);
        }

        User verifyIdUserModifies = findUser.byId(descuentos.getIdCreatedBy()) ;

        if (verifyIdUserModifies == null) {
            throw new BadRequestException(324);
        }

        return descuentosPort.createDescuento(descuentos);
    }

    @Override
    public Descuentos createDescuentoAliado(Descuentos descuentos) {

        if (descuentos == null) {
            throw new BadRequestException(340);
        }

        User verifyIdUser = findUser.byId(descuentos.getIdUsuario()) ;

        if (verifyIdUser == null) {
            throw new BadRequestException(324);
        }

        User verifyIdUserModifies = findUser.byId(descuentos.getIdCreatedBy()) ;

        if (verifyIdUserModifies == null) {
            throw new BadRequestException(324);
        }

        return descuentosPort.createDescuentoAliado(descuentos);
    }

    @Override
    public List<Descuentos> findAllDescuentos() {
        return  descuentosPort.findAllDescuentos();
    }

    @Override
    public List<Descuentos> findDescuentosByUser(Long idUser) {

        if (idUser == null) {
            throw new BadRequestException(327);
        }

        User verifyIdUser = findUser.byId(idUser) ;

        if (verifyIdUser == null) {
            throw new BadRequestException(324);
        }

        return descuentosPort.findDescuentosByUser(idUser);
    }

    @Override
    public List<Descuentos> findDescuentosByAliado(Long idUser) {

        if (idUser == null) {
            throw new BadRequestException(327);
        }

        User verifyIdUser = findUser.byId(idUser) ;

        if (verifyIdUser == null) {
            throw new BadRequestException(324);
        }

        return descuentosPort.findDescuentosByAliado(idUser);

    }

    @Override
    public List<Descuentos> findDescuentoByUserAndAliado(Long idUSer, Long idAliado) {

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

        return descuentosPort.findDescuentoByUserAndAliado(idUSer,idAliado);
    }
}
