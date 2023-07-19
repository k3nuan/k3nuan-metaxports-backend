package com.meta.sports.general.adapters;


import com.meta.sports.general.domain.Descuentos;
import com.meta.sports.general.domain.Recargas;
import com.meta.sports.general.entitys.DescuentosEntity;
import com.meta.sports.general.entitys.RecargasEntity;
import com.meta.sports.general.ports.out.DescuentosPort;
import com.meta.sports.general.repositorys.DescuentosRepository;
import com.meta.sports.general.repositorys.RecargasRepository;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.user.adapter.out.persistence.FindUserAdapter;
import com.meta.sports.user.adapter.out.persistence.UserEntity;
import com.meta.sports.user.adapter.out.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
//import sun.security.krb5.internal.crypto.Des;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class DescuentosAdapter implements DescuentosPort {


    private final DescuentosRepository descuentosRepository;

    private final UserRepository userRepository;

    private final FindUserAdapter findUserAdapter;

    @Transactional
    @Override
    public Descuentos createDescuento(Descuentos descuentos) {
        UserEntity userEntity = userRepository.findUserByid(descuentos.getIdUsuario());

        if (userEntity == null) {
            throw new BadRequestException(327);
        }

        if (userEntity.getRole().getName().equalsIgnoreCase("ADMIN")) {
            // Manejar el caso en que el usuario sea un admin
            throw new BadRequestException(343);
        }

        UserEntity modifiedBy = userRepository.findUserByid(descuentos.getIdCreatedBy());

        if (modifiedBy == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(327);
        }



        DescuentosEntity descuentosEntity = new DescuentosEntity();

        descuentosEntity.setId_usuario(userEntity);

        descuentosEntity.setMonto_descontado(descuentos.getMontoDescontado());

        descuentosEntity.setMotivo(descuentos.getMotivo());

        descuentosEntity.setDate(LocalDateTime.now());

        descuentosEntity.setCreated_by(modifiedBy);

        int saldoActualizado = userEntity.getSaldo()-descuentos.getMontoDescontado();

        userEntity.setSaldo(saldoActualizado);

        userRepository.saveAndFlush(userEntity);

        descuentosRepository.saveAndFlush(descuentosEntity);

        descuentos.setId(descuentosEntity.getId());

        return descuentos;
    }

    @Transactional
    @Override
    public Descuentos createDescuentoAliado(Descuentos descuento) {

        UserEntity userEntity = userRepository.findUserByid(descuento.getIdUsuario());

        if (userEntity == null) {
            // Manejar el caso en que no se encuentre el usuario
            throw new BadRequestException(327);
        }

        UserEntity usuarioAliado = userRepository.findUserByid(descuento.getIdCreatedBy());

        if (usuarioAliado == null) {
            // Manejar el caso en que no se encuentre el usuario
            throw new BadRequestException(327);
        }

        if (!usuarioAliado.getRole().getName().equalsIgnoreCase("ALIADO")) {
            // Manejar el caso en que el usuario no sea aliado
            throw new BadRequestException(329);
        }

        // Manejar el caso en que el el monto sea mayor al monto del aliado
        if (descuento.getMontoDescontado() > userEntity.getSaldo()) {
            throw new BadRequestException(341);
        }

        DescuentosEntity descuentoEntity = new DescuentosEntity();

        descuentoEntity.setId_usuario(userEntity);

        descuentoEntity.setMonto_descontado(descuento.getMontoDescontado());

        descuentoEntity.setDate(LocalDateTime.now());

        descuentoEntity.setMotivo(descuento.getMotivo());


        descuentoEntity.setCreated_by(usuarioAliado);

        int saldoActualizadoUser = userEntity.getSaldo() - descuento.getMontoDescontado();

        userEntity.setSaldo(saldoActualizadoUser);

        userRepository.saveAndFlush(userEntity);

        int saldoaliado = usuarioAliado.getSaldo() + descuento.getMontoDescontado();
        usuarioAliado.setSaldo(saldoaliado);

        userRepository.saveAndFlush(usuarioAliado);

        descuentosRepository.saveAndFlush(descuentoEntity);

        descuento.setId(descuentoEntity.getId());

        return descuento;
    }

    @Override
    public List<Descuentos> findAllDescuentos() {

        List<DescuentosEntity> recargasEntities = descuentosRepository.findAll();
        List<Descuentos> descuentos = new ArrayList<>();
        recargasEntities.forEach(recargaEntity -> {
            Descuentos descuento = new Descuentos();
            descuento.setId(recargaEntity.getId());
            descuento.setIdUsuario(recargaEntity.getId_usuario().getId());
            descuento.setUsuario(findUserAdapter.byId(descuento.getIdUsuario()));
            descuento.setMontoDescontado(recargaEntity.getMonto_descontado());
            descuento.setMotivo(recargaEntity.getMotivo());
            descuento.setDate(recargaEntity.getDate());
            descuento.setIdCreatedBy(recargaEntity.getCreated_by().getId());
            descuento.setCreatedBy(findUserAdapter.byId(descuento.getIdCreatedBy()));

            descuentos.add(descuento);

        });
        return descuentos;
    }

    @Transactional
    @Override
    public List<Descuentos> findDescuentosByUser(Long idUser) {
        UserEntity userEntity = userRepository.findUserByid(idUser);

        if (userEntity == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(327);
        }

        List<DescuentosEntity> descuentosEntities = userEntity.getDescuentos();


        List<Descuentos> descuentos = new ArrayList<>(descuentosEntities.size());
        Descuentos descuento;

        for (DescuentosEntity descuentosEntity : descuentosEntities) {

            descuento = new Descuentos();
            descuento.setId(descuentosEntity.getId());
            descuento.setIdUsuario(descuentosEntity.getId_usuario().getId());
            descuento.setUsuario( findUserAdapter.byId(descuento.getIdUsuario()));
            descuento.setMontoDescontado(descuentosEntity.getMonto_descontado());
            descuento.setDate(descuentosEntity.getDate());
            descuento.setMotivo(descuentosEntity.getMotivo());

            descuento.setIdCreatedBy(descuentosEntity.getCreated_by().getId());
            descuento.setCreatedBy(findUserAdapter.byId(descuento.getIdCreatedBy()));

            descuentos.add(descuento);
        }

        return descuentos;
    }

    @Transactional
    @Override
    public List<Descuentos> findDescuentosByAliado(Long idUser) {


        UserEntity userEntity = userRepository.findUserByid(idUser);

        if (userEntity == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(327);
        }

        if (!userEntity.getRole().getName().equalsIgnoreCase("ALIADO")) {
            // Manejar el caso en que el usuario no sea aliado
            throw new BadRequestException(329);
        }

        List<DescuentosEntity> descuentosEntities = descuentosRepository.findDescuentosByUser(userEntity);


        List<Descuentos> descuentos = new ArrayList<>(descuentosEntities.size());
        Descuentos descuento;

        for (DescuentosEntity descuentosEntity : descuentosEntities) {

            descuento = new Descuentos();
            descuento.setId(descuentosEntity.getId());
            descuento.setIdUsuario(descuentosEntity.getId_usuario().getId());
            descuento.setUsuario(findUserAdapter.byId(descuento.getIdUsuario()));
            descuento.setMontoDescontado(descuentosEntity.getMonto_descontado());
            descuento.setDate(descuentosEntity.getDate());
            descuento.setMotivo(descuentosEntity.getMotivo());

            descuento.setIdCreatedBy(descuentosEntity.getCreated_by().getId());
            descuento.setCreatedBy(findUserAdapter.byId(descuento.getIdCreatedBy()));

            descuentos.add(descuento);
        }

        return descuentos;
    }

    @Override
    public List<Descuentos> findDescuentoByUserAndAliado(Long idUSer, Long idAliado) {

        UserEntity userEntity = userRepository.findUserByid(idUSer);

        if (userEntity == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(327);
        }

        UserEntity aliadoEntity = userRepository.findUserByid(idAliado);

        if (aliadoEntity == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(327);
        }

        if (!aliadoEntity.getRole().getName().equalsIgnoreCase("ALIADO")) {
            // Manejar el caso en que el usuario no sea aliado
            throw new BadRequestException(329);
        }

        List<DescuentosEntity> descuentosEntities = descuentosRepository.findDescuentosByAliadoAndUSer(userEntity, aliadoEntity);

        List<Descuentos> DescuentosUser = new ArrayList<>(descuentosEntities.size());
        Descuentos descuentos;

        for (DescuentosEntity descuentosEntity : descuentosEntities) {

            descuentos = new Descuentos();
            descuentos.setId(descuentosEntity.getId());
            descuentos.setIdUsuario(descuentosEntity.getId_usuario().getId());
            descuentos.setUsuario(findUserAdapter.byId(descuentos.getIdUsuario()));
            descuentos.setMontoDescontado(descuentosEntity.getMonto_descontado());
            descuentos.setDate(descuentosEntity.getDate());
            descuentos.setMotivo(descuentosEntity.getMotivo());

            descuentos.setIdCreatedBy(descuentosEntity.getCreated_by().getId());
            descuentos.setCreatedBy(findUserAdapter.byId(descuentos.getIdCreatedBy()));

            DescuentosUser.add(descuentos);
        }

        return DescuentosUser;

    }
}
