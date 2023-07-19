package com.meta.sports.general.adapters;

import com.meta.sports.general.domain.PronosticoExactoUser;
import com.meta.sports.general.domain.PronosticoUser;
import com.meta.sports.general.entitys.*;
import com.meta.sports.general.ports.out.PronosticoExactoUserPort;
import com.meta.sports.general.repositorys.*;
import com.meta.sports.global.exceptions.BadRequestException;
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
public class PronosticoExactoUserAdapter implements PronosticoExactoUserPort {

    private  final PronosticoExactoUserRepository pronosticoExactoUserRepository;

    private  final ApuestaExactaRepository apuestaExactaRepository;

    private  final UserRepository userRepository;

    private  final PoteRepository poteRepository;


    private  final ApuestaExactaAdapter apuestaExactaAdapter;

    @Transactional
    @Override
    public PronosticoExactoUser createPronosticoExacto(PronosticoExactoUser pronosticoExactoUser) {


        ApuestaExactaEntity apuestaIdEntity = apuestaExactaRepository.findApuestaExactaById(pronosticoExactoUser.getIdApuestaExacta());

        UserEntity userIdEntity = userRepository.findUserByid(pronosticoExactoUser.getIdUser());

        if (userIdEntity.getSaldo() < pronosticoExactoUser.getMontoApostado()){
            throw new BadRequestException(332);
        }

        PronosticoExactoUserEntity pronosticoEntity = new PronosticoExactoUserEntity();

        pronosticoEntity.setId_apuesta_exacta(apuestaIdEntity);
        pronosticoEntity.setId_usuario(userIdEntity);
        pronosticoEntity.setMonto_apostado(pronosticoExactoUser.getMontoApostado());
        pronosticoEntity.setMarcador_local(pronosticoExactoUser.getMarcadorLocal());
        pronosticoEntity.setMarcador_visitante(pronosticoExactoUser.getMarcadorVisitante());
        pronosticoEntity.setCreated(LocalDateTime.now());
        pronosticoEntity.setPuntuacion(0);

        int saldoUser=userIdEntity.getSaldo() - pronosticoEntity.getMonto_apostado();

        userIdEntity.setSaldo(saldoUser);

        userRepository.saveAndFlush(userIdEntity);

        //Buscamos el pote segun el id de la apuesta, le asignamos el monto apostado y le sumamos uno a nroApuestas

        PoteEntity poteEntity = poteRepository.findPoteByApuestaExactaId(apuestaIdEntity);

        int saldoPote= poteEntity.getSaldo() + pronosticoExactoUser.getMontoApostado();
        poteEntity.setSaldo(saldoPote);

        int nroApuestas= poteEntity.getNro_apuestas() + 1;
        poteEntity.setNro_apuestas(nroApuestas);

        // guardamos los cambios
        poteRepository.saveAndFlush(poteEntity);

        //guardamos el pronostico
        pronosticoExactoUserRepository.saveAndFlush(pronosticoEntity);

        pronosticoExactoUser.setId(pronosticoEntity.getId());

        return pronosticoExactoUser;
    }

    @Override
    public List<PronosticoExactoUser> findAllPronosticosExactos() {

        List<PronosticoExactoUserEntity> apronosticosEntities = pronosticoExactoUserRepository.findAll();
        List<PronosticoExactoUser>  pronosticos = new ArrayList<>();
        apronosticosEntities.forEach(pronosticosEntity-> {

            PronosticoExactoUser pronostico = new PronosticoExactoUser();
            pronostico.setId(pronosticosEntity.getId());
            pronostico.setIdApuestaExacta(pronosticosEntity.getId_apuesta_exacta().getId());
            pronostico.setIdUser(pronosticosEntity.getId_usuario().getId());
            pronostico.setMontoApostado(pronosticosEntity.getMonto_apostado());
            pronostico.setMarcadorLocal(pronosticosEntity.getMarcador_local());
            pronostico.setMarcadorVisitante(pronosticosEntity.getMarcador_visitante());
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
    public PronosticoExactoUser findPronosticoExactoById(Long id) {

        PronosticoExactoUserEntity PronosticoEntity= pronosticoExactoUserRepository.findPronosticoExactoById(id);

        if (PronosticoEntity == null) {
            throw new BadRequestException(331);
        }

        PronosticoExactoUser pronostico = new PronosticoExactoUser();
        pronostico.setId(PronosticoEntity.getId());
        pronostico.setIdApuestaExacta(PronosticoEntity.getId_apuesta_exacta().getId());
        pronostico.setApuestaExacta(apuestaExactaAdapter.findApuestaExactaById(pronostico.getIdApuestaExacta()));
        pronostico.setIdUser(PronosticoEntity.getId_usuario().getId());
        pronostico.setMontoApostado(PronosticoEntity.getMonto_apostado());
        pronostico.setMarcadorLocal(PronosticoEntity.getMarcador_local());
        pronostico.setMarcadorVisitante(PronosticoEntity.getMarcador_visitante());
        pronostico.setCreate(PronosticoEntity.getCreated());

        if (PronosticoEntity.getState_apuesta() != null) {
            pronostico.setStateApuesta(PronosticoEntity.getState_apuesta());
            pronostico.setStateDate(PronosticoEntity.getState_date());

        }

        return pronostico;
    }
    @Transactional
    @Override
    public List<PronosticoExactoUser> findPronosticoExactoByApuesta(Long idApuesta) {

        ApuestaExactaEntity apuestaEntity = apuestaExactaRepository.findApuestaExactaById(idApuesta);

        if (apuestaEntity == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(326);

        }

        List<PronosticoExactoUserEntity> pronosticosEntities = apuestaEntity.getPronosticosExactos();

        if (pronosticosEntities == null || pronosticosEntities.isEmpty()) {
            // Manejar el caso en que no se encuentren apuestas asociados al juego
            throw new BadRequestException(331);
        }

        List<PronosticoExactoUser> pronosticos = new ArrayList<>(pronosticosEntities.size());
        PronosticoExactoUser pronostico;

        for (PronosticoExactoUserEntity pronosticoUserEntity : pronosticosEntities) {

            pronostico = new PronosticoExactoUser();
            pronostico.setId(pronosticoUserEntity.getId());
            pronostico.setIdApuestaExacta(pronosticoUserEntity.getId_apuesta_exacta().getId());
            pronostico.setApuestaExacta(apuestaExactaAdapter.findApuestaExactaById(pronostico.getIdApuestaExacta()));
            pronostico.setIdUser(pronosticoUserEntity.getId_usuario().getId());
            pronostico.setMarcadorLocal(pronosticoUserEntity.getMarcador_local());
            pronostico.setMarcadorVisitante(pronosticoUserEntity.getMarcador_visitante());
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
    public List<PronosticoExactoUser> findPronosticoExactoByUser(Long idUser) {

        UserEntity userEntity = userRepository.findUserByid(idUser);

        if (userEntity == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(327);
        }

        List<PronosticoExactoUserEntity> pronosticosEntities = userEntity.getPronosticosExactos();


        List<PronosticoExactoUser> pronosticos = new ArrayList<>(pronosticosEntities.size());
        PronosticoExactoUser pronostico;

        for (PronosticoExactoUserEntity pronosticoUserEntity : pronosticosEntities) {

            pronostico = new PronosticoExactoUser();
            pronostico.setId(pronosticoUserEntity.getId());
            pronostico.setIdApuestaExacta(pronosticoUserEntity.getId_apuesta_exacta().getId());
            pronostico.setApuestaExacta(apuestaExactaAdapter.findApuestaExactaById(pronostico.getIdApuestaExacta()));
            pronostico.setIdUser(pronosticoUserEntity.getId_usuario().getId());
            pronostico.setMarcadorLocal(pronosticoUserEntity.getMarcador_local());
            pronostico.setMarcadorVisitante(pronosticoUserEntity.getMarcador_visitante());
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
