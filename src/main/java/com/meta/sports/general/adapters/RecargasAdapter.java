package com.meta.sports.general.adapters;


import com.meta.sports.general.domain.Events;
import com.meta.sports.general.domain.PronosticoUser;
import com.meta.sports.general.domain.Recargas;
import com.meta.sports.general.entitys.EventsEntity;
import com.meta.sports.general.entitys.PronosticoUserEntity;
import com.meta.sports.general.entitys.RecargasEntity;
import com.meta.sports.general.entitys.TournamentEntity;
import com.meta.sports.general.ports.out.RecargasPort;
import com.meta.sports.general.repositorys.RecargasRepository;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.user.adapter.out.persistence.FindUserAdapter;
import com.meta.sports.user.adapter.out.persistence.UserEntity;
import com.meta.sports.user.adapter.out.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class RecargasAdapter implements RecargasPort {


    private final RecargasRepository recargasRepository;

    private final UserRepository userRepository;

    private final FindUserAdapter findUserAdapter;

    @Transactional
    @Override
    public Recargas createRecarga(Recargas recargas) {


        UserEntity userEntity = userRepository.findUserByid(recargas.getIdUsuario());

        if (userEntity == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(327);
        }

        if (userEntity.getRole().getName().equalsIgnoreCase("ADMIN")) {
            // Manejar el caso en que el usuario no sea aliado
            throw new BadRequestException(343);
        }

        UserEntity modifiedBy = userRepository.findUserByid(recargas.getIdCreatedBy());

        if (modifiedBy == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(327);
        }



        RecargasEntity recargasEntity = new RecargasEntity();

        recargasEntity.setId_usuario(userEntity);

        recargasEntity.setMonto_recargado(recargas.getMontoRecargado());

        recargasEntity.setDate(LocalDateTime.now());

        recargasEntity.setCreated_by(modifiedBy);

        int saldoActualizado = userEntity.getSaldo()+recargas.getMontoRecargado();

        userEntity.setSaldo(saldoActualizado);

        userRepository.saveAndFlush(userEntity);

        recargasRepository.saveAndFlush(recargasEntity);

        recargas.setId(recargasEntity.getId());

        return recargas;
    }

    @Override
    public List<Recargas> findAllRecargas() {

        List<RecargasEntity> recargasEntities = recargasRepository.findAll();
        List<Recargas>  recargas = new ArrayList<>();
        recargasEntities.forEach(recargaEntity-> {
            Recargas recarga = new Recargas();
            recarga.setId(recargaEntity.getId());
            recarga.setIdUsuario(recargaEntity.getId_usuario().getId());
            recarga.setUsuario( findUserAdapter.byId(recarga.getIdUsuario()));
            recarga.setMontoRecargado(recargaEntity.getMonto_recargado());
            recarga.setDate(recargaEntity.getDate());
            recarga.setIdCreatedBy(recargaEntity.getCreated_by().getId());
            recarga.setCreatedBy(findUserAdapter.byId(recarga.getIdCreatedBy()));

            recargas.add(recarga);

        });
        return recargas;
    }

    @Transactional
    @Override
    public List<Recargas> findRecargaByUser(Long idUser) {

        UserEntity userEntity = userRepository.findUserByid(idUser);

        if (userEntity == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(327);
        }

        List<RecargasEntity> recargasEntities = userEntity.getRecargas();


        List<Recargas> recargas = new ArrayList<>(recargasEntities.size());
        Recargas recarga;

        for (RecargasEntity recargaEntity : recargasEntities) {

            recarga = new Recargas();
            recarga.setId(recargaEntity.getId());
            recarga.setIdUsuario(recargaEntity.getId_usuario().getId());
            recarga.setUsuario( findUserAdapter.byId(recarga.getIdUsuario()));
            recarga.setMontoRecargado(recargaEntity.getMonto_recargado());
            recarga.setDate(recargaEntity.getDate());
            recarga.setIdCreatedBy(recargaEntity.getCreated_by().getId());
            recarga.setCreatedBy(findUserAdapter.byId(recarga.getIdCreatedBy()));

            recargas.add(recarga);
        }

        return recargas;


    }

    @Override
    public Recargas crearRecargaAliado(Recargas recargas) {

        UserEntity userEntity = userRepository.findUserByid(recargas.getIdUsuario());

        if (userEntity == null) {
            // Manejar el caso en que no se encuentre el usuario
            throw new BadRequestException(327);
        }

        if (userEntity.getRole().getName().equalsIgnoreCase("ADMIN")) {
            // Manejar el caso en que el usuario no sea aliado
            throw new BadRequestException(343);
        }

        UserEntity usuarioAliado = userRepository.findUserByid(recargas.getIdCreatedBy());

        if (usuarioAliado == null) {
            // Manejar el caso en que no se encuentre el usuario
            throw new BadRequestException(327);
        }

        if (!usuarioAliado.getRole().getName().equalsIgnoreCase("ALIADO")) {
            // Manejar el caso en que el usuario no sea aliado
            throw new BadRequestException(329);
        }

        // Manejar el caso en que el el monto sea mayor al monto del aliado
        if (recargas.getMontoRecargado() > usuarioAliado.getSaldo()){
            throw new BadRequestException(341);
        }

        RecargasEntity recargasEntity = new RecargasEntity();

        recargasEntity.setId_usuario(userEntity);

        recargasEntity.setMonto_recargado(recargas.getMontoRecargado());

        recargasEntity.setDate(LocalDateTime.now());

        recargasEntity.setCreated_by(usuarioAliado);

        int saldoAliado=usuarioAliado.getSaldo() - recargas.getMontoRecargado();
        usuarioAliado.setSaldo(saldoAliado);
        userRepository.saveAndFlush(usuarioAliado);

        int saldoActualizadoUser = userEntity.getSaldo()+recargas.getMontoRecargado();

        userEntity.setSaldo(saldoActualizadoUser);

        userRepository.saveAndFlush(userEntity);

        recargasRepository.saveAndFlush(recargasEntity);

        recargas.setId(recargasEntity.getId());

        return recargas;

    }


    @Override
    @Transactional
    public List<Recargas> findRecargaAllrecargaAliado(Long idUser) {

        UserEntity userEntity = userRepository.findUserByid(idUser);

        if (userEntity == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(327);
        }

        if (!userEntity.getRole().getName().equalsIgnoreCase("ALIADO")) {
            // Manejar el caso en que el usuario no sea aliado
            throw new BadRequestException(329);
        }

        List<RecargasEntity> recargasEntities = recargasRepository.findRecargasByUser(userEntity);


        List<Recargas> recargas = new ArrayList<>(recargasEntities.size());
        Recargas recarga;

        for (RecargasEntity recargaEntity : recargasEntities) {

            recarga = new Recargas();
            recarga.setId(recargaEntity.getId());
            recarga.setIdUsuario(recargaEntity.getId_usuario().getId());
            recarga.setUsuario( findUserAdapter.byId(recarga.getIdUsuario()));
            recarga.setMontoRecargado(recargaEntity.getMonto_recargado());
            recarga.setDate(recargaEntity.getDate());
            recarga.setIdCreatedBy(recargaEntity.getCreated_by().getId());
            recarga.setCreatedBy(findUserAdapter.byId(recarga.getIdCreatedBy()));

            recargas.add(recarga);
        }

        return recargas;
    }

    @Override
    public List<Recargas> findRecargaByUserAndAliado(Long idUSer, Long idAliado) {

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

        List<RecargasEntity> recargasEntities = recargasRepository.findRecargasByAliadoAndUSer(userEntity,aliadoEntity);

        List<Recargas> recargasUser = new ArrayList<>(recargasEntities.size());
        Recargas recarga;

        for (RecargasEntity recargaEntity : recargasEntities) {

            recarga = new Recargas();
            recarga.setId(recargaEntity.getId());
            recarga.setIdUsuario(recargaEntity.getId_usuario().getId());
            recarga.setUsuario( findUserAdapter.byId(recarga.getIdUsuario()));
            recarga.setMontoRecargado(recargaEntity.getMonto_recargado());
            recarga.setDate(recargaEntity.getDate());
            recarga.setIdCreatedBy(recargaEntity.getCreated_by().getId());
            recarga.setCreatedBy(findUserAdapter.byId(recarga.getIdCreatedBy()));

            recargasUser.add(recarga);
        }

        return recargasUser;


    }


}
