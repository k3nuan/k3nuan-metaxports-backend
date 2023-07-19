package com.meta.sports.general.adapters;

import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.PronosticoUser;
import com.meta.sports.general.entitys.*;
import com.meta.sports.general.ports.out.PronosticoUserPort;
import com.meta.sports.general.repositorys.*;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.global.exceptions.NotFoundException;
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
public class PronosticoUserAdapter implements PronosticoUserPort {

    private  final PronosticoUserRepository pronosticoUserRepository;

    private  final ApuestasRepository apuestasRepository;

    private  final UserRepository userRepository;

    private  final PoteRepository poteRepository;


    private  final ApuestasAdapter apuestasAdapter;

    @Transactional
    @Override
    public PronosticoUser createPronostico(PronosticoUser pronosticoUser) {



        ApuestasEntity apuestaIdEntity = apuestasRepository.findApuestaById(pronosticoUser.getIdApuesta());

        UserEntity userIdEntity = userRepository.findUserByid(pronosticoUser.getIdUser());


        if (userIdEntity.getSaldo() < pronosticoUser.getMontoApostado()){
            throw new BadRequestException(332);
        }

        PronosticoUserEntity pronosticoEntity = new PronosticoUserEntity();

        pronosticoEntity.setId_apuesta(apuestaIdEntity);
        pronosticoEntity.setId_usuario(userIdEntity);
        pronosticoEntity.setMonto_apostado(pronosticoUser.getMontoApostado());
        pronosticoEntity.setOpcion_apuesta(pronosticoUser.getOpcionApuesta());
        pronosticoEntity.setCreated(LocalDateTime.now());

        int saldoUser=userIdEntity.getSaldo() - pronosticoEntity.getMonto_apostado();

        userIdEntity.setSaldo(saldoUser);

        userRepository.saveAndFlush(userIdEntity);

        //Buscamos el pote segun el id de la apuesta, le asignamos el monto apostado y le sumamos uno a nroApuestas

        PoteEntity poteEntity = poteRepository.findPoteByApuestaId(apuestaIdEntity);

        int saldoPote= poteEntity.getSaldo() + pronosticoUser.getMontoApostado();
        poteEntity.setSaldo(saldoPote);

        int nroApuestas= poteEntity.getNro_apuestas() + 1;
        poteEntity.setNro_apuestas(nroApuestas);

        // guardamos los cambios
        poteRepository.saveAndFlush(poteEntity);

        //guardamos el pronostico
        pronosticoUserRepository.saveAndFlush(pronosticoEntity);

        pronosticoUser.setId(pronosticoEntity.getId());

        return pronosticoUser;
    }

    @Override
    public List<PronosticoUser> findAllPronosticos() {
        List<PronosticoUserEntity> apronosticosEntities = pronosticoUserRepository.findAll();
        List<PronosticoUser>  pronosticos = new ArrayList<>();
        apronosticosEntities.forEach(pronosticosEntity-> {

            PronosticoUser pronostico = new PronosticoUser();
            pronostico.setId(pronosticosEntity.getId());
            pronostico.setIdApuesta(pronosticosEntity.getId_apuesta().getId());
            pronostico.setIdUser(pronosticosEntity.getId_usuario().getId());
            pronostico.setMontoApostado(pronosticosEntity.getMonto_apostado());
            pronostico.setOpcionApuesta(pronosticosEntity.getOpcion_apuesta());
            pronostico.setCreate(pronosticosEntity.getCreated());

            if (pronosticosEntity.getState_apuesta() != null) {
                pronostico.setStateApuesta(pronosticosEntity.getState_apuesta());
                pronostico.setStateDate(pronosticosEntity.getState_date());

            }

            pronosticos.add(pronostico);
        });
        return pronosticos;
    }

    @Override
    public PronosticoUser findPronosticoById(Long id) {
        PronosticoUserEntity PronosticoEntity= pronosticoUserRepository.findPronosticoById(id);

        if (PronosticoEntity == null) {
            throw new BadRequestException(331);
        }

        PronosticoUser pronostico = new PronosticoUser();
        pronostico.setId(PronosticoEntity.getId());
        pronostico.setIdApuesta(PronosticoEntity.getId_apuesta().getId());
        pronostico.setApuesta(apuestasAdapter.findApuestaById(pronostico.getIdApuesta()));
        pronostico.setIdUser(PronosticoEntity.getId_usuario().getId());
        pronostico.setMontoApostado(PronosticoEntity.getMonto_apostado());
        pronostico.setOpcionApuesta(PronosticoEntity.getOpcion_apuesta());
        pronostico.setCreate(PronosticoEntity.getCreated());

        if (PronosticoEntity.getState_apuesta() != null) {
            pronostico.setStateApuesta(PronosticoEntity.getState_apuesta());
            pronostico.setStateDate(PronosticoEntity.getState_date());

        }

        return pronostico;
    }

    @Transactional
    @Override
    public List<PronosticoUser> findPronosticoByApuesta(Long idApuesta) {

        ApuestasEntity apuestaEntity = apuestasRepository.findApuestaById(idApuesta);

        if (apuestaEntity == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(326);

        }

        List<PronosticoUserEntity> pronosticosEntities = apuestaEntity.getPronosticos();

        if (pronosticosEntities == null || pronosticosEntities.isEmpty()) {
            // Manejar el caso en que no se encuentren apuestas asociados al juego
            throw new BadRequestException(331);
        }

        List<PronosticoUser> pronosticos = new ArrayList<>(pronosticosEntities.size());
        PronosticoUser pronostico;

        for (PronosticoUserEntity pronosticoUserEntity : pronosticosEntities) {

            pronostico = new PronosticoUser();
            pronostico.setId(pronosticoUserEntity.getId());
            pronostico.setIdApuesta(pronosticoUserEntity.getId_apuesta().getId());
            pronostico.setApuesta(apuestasAdapter.findApuestaById(pronostico.getIdApuesta()));
            pronostico.setIdUser(pronosticoUserEntity.getId_usuario().getId());
            pronostico.setOpcionApuesta(pronosticoUserEntity.getOpcion_apuesta());
            pronostico.setMontoApostado(pronosticoUserEntity.getMonto_apostado());
            pronostico.setCreate(pronosticoUserEntity.getCreated());

            if (pronosticoUserEntity.getState_apuesta() != null) {
                pronostico.setStateApuesta(pronosticoUserEntity.getState_apuesta());
                pronostico.setStateDate(pronosticoUserEntity.getState_date());

            }

            pronosticos.add(pronostico);
        }

        return pronosticos;
    }
    @Transactional
    @Override
    public List<PronosticoUser> findPronosticoByUser(Long idUser) {

        UserEntity userEntity = userRepository.findUserByid(idUser);

        if (userEntity == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(327);
        }

        List<PronosticoUserEntity> pronosticosEntities = userEntity.getPronosticos();


        List<PronosticoUser> pronosticos = new ArrayList<>(pronosticosEntities.size());
        PronosticoUser pronostico;

        for (PronosticoUserEntity pronosticoUserEntity : pronosticosEntities) {

            pronostico = new PronosticoUser();
            pronostico.setId(pronosticoUserEntity.getId());
            pronostico.setIdApuesta(pronosticoUserEntity.getId_apuesta().getId());
            pronostico.setApuesta(apuestasAdapter.findApuestaById(pronostico.getIdApuesta()));
            pronostico.setIdUser(pronosticoUserEntity.getId_usuario().getId());
            pronostico.setOpcionApuesta(pronosticoUserEntity.getOpcion_apuesta());
            pronostico.setMontoApostado(pronosticoUserEntity.getMonto_apostado());
            pronostico.setCreate(pronosticoUserEntity.getCreated());

            if (pronosticoUserEntity.getState_apuesta() != null) {
                pronostico.setStateApuesta(pronosticoUserEntity.getState_apuesta());
                pronostico.setStateDate(pronosticoUserEntity.getState_date());

            }

            pronosticos.add(pronostico);
        }

        return pronosticos;
    }

}
