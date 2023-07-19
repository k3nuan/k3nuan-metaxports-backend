package com.meta.sports.general.adapters;


import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.ApuestaExacta;
import com.meta.sports.general.domain.PronosticoUser;
import com.meta.sports.general.entitys.*;
import com.meta.sports.general.ports.out.ApuestaExactaPort;
import com.meta.sports.general.repositorys.*;
import com.meta.sports.global.Constants;
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
public class ApuestaExactaAdapter implements ApuestaExactaPort {

    private final ApuestaExactaRepository apuestaExactaRepository;

    private final SoccerGameRepository soccerGameRepository;

    private final SoccerGameAdapter soccerGame;

    private final UserRepository userRepository;
    private final PoteRepository poteRepository;

    private final PronosticoExactoUserRepository pronosticoExactoUserRepository;






    @Override
    @Transactional
    public List<ApuestaExacta> findApuestaExactaByPartido(Long idJuego) {

        SoccerGameEntity soccerGameEntity = soccerGameRepository.findSoccerGameById(idJuego);

        if (soccerGameEntity == null) {
            // Manejar el caso en que no se encuentre el juego
            throw new BadRequestException(326);

        }


        List<ApuestaExactaEntity> apuestaExactaEntities = soccerGameEntity.getApuestasExactas();

        if (apuestaExactaEntities == null || apuestaExactaEntities.isEmpty()) {
            // Manejar el caso en que no se encuentren apuestas asociados al juego
            throw new BadRequestException(331);
        }



        List<ApuestaExacta> apuestaExactas = new ArrayList<>(apuestaExactaEntities.size());
        ApuestaExacta apuestaExacta;

        for (ApuestaExactaEntity apuestaExactaEntity : apuestaExactaEntities) {

            apuestaExacta = new ApuestaExacta();
            apuestaExacta.setId(apuestaExactaEntity.getId());
            apuestaExacta.setIdJuego(apuestaExactaEntity.getId_juego().getId());
            apuestaExacta.setPregunta(apuestaExactaEntity.getPregunta());
            apuestaExacta.setSoccerGame(soccerGame.findSoccerGameById(apuestaExacta.getId()));
            apuestaExacta.setMarcadorLocal(apuestaExactaEntity.getMarcador_local());
            apuestaExacta.setMarcadorVisitante(apuestaExactaEntity.getMarcador_visitante());
            apuestaExacta.setCreate(apuestaExactaEntity.getCreated());
            apuestaExacta.setStatus(apuestaExactaEntity.getStatus());

                if(apuestaExactaEntity.getModified()!= null){
                    apuestaExacta.setModified(apuestaExactaEntity.getModified());
                }
            apuestaExactas.add(apuestaExacta);
        }

        return apuestaExactas;
    }



    @Override
    public ApuestaExacta findApuestaExactaById(Long id) {

        ApuestaExactaEntity apuestaEntity= apuestaExactaRepository.findApuestaExactaById(id);

        if (apuestaEntity == null) {
            throw new BadRequestException(323);
        }

        ApuestaExacta apuesta = new ApuestaExacta();
        apuesta.setId(apuestaEntity.getId());
        apuesta.setIdJuego(apuestaEntity.getId_juego().getId());
        apuesta.setSoccerGame(soccerGame.findSoccerGameById(apuesta.getIdJuego()));
        apuesta.setPregunta(apuestaEntity.getPregunta());
        apuesta.setMarcadorLocal(apuestaEntity.getMarcador_local());
        apuesta.setMarcadorVisitante(apuestaEntity.getMarcador_visitante());
        apuesta.setCreate(apuestaEntity.getCreated());

        if (apuestaEntity.getModified_by() != null){
            apuesta.setIdModifiedBy(apuestaEntity.getModified_by().getId());
            apuesta.setModified(apuestaEntity.getModified());
        }

        return apuesta;

    }

    @Override
    public void changeStatus(Long id) {
        SoccerGameEntity soccerGameEntity=soccerGameRepository.findSoccerGameById(id);
       List<ApuestaExactaEntity> apuestaEntity = apuestaExactaRepository.findApuestaExactaByPartido(soccerGameEntity);

       apuestaEntity.forEach(apuesta->{
           apuesta.setStatus(1);
           apuestaExactaRepository.saveAndFlush(apuesta);
       });

    }

    @Override
    public void apuestaGanadora(ApuestaExacta apuestaExacta) {

        //Buscamos la apuesta y el usuario que esta haciendo la modificacion

        ApuestaExactaEntity apuestaEntity = apuestaExactaRepository.findApuestaExactaById(apuestaExacta.getId());

        UserEntity userIdEntity = userRepository.findUserByid(apuestaExacta.getIdModifiedBy());

        if (apuestaEntity.getCreated_by()!= null){
            throw new BadRequestException(322);
        }

        //Guardamos los datos de la apuesta en la tabla Apuesta

        apuestaEntity.setMarcador_local(apuestaExacta.getMarcadorLocal());
        apuestaEntity.setMarcador_visitante(apuestaExacta.getMarcadorVisitante());
        apuestaEntity.setModified_by(userIdEntity);
        apuestaEntity.setModified(LocalDateTime.now());

        int marcadorLocal = apuestaExacta.getMarcadorLocal();
        int marcadorVisitante = apuestaExacta.getMarcadorVisitante();

        apuestaExactaRepository.saveAndFlush(apuestaEntity);



        String ganador;

        if (marcadorLocal==marcadorVisitante){
            ganador="empate";
        }else if(marcadorLocal >=marcadorVisitante){
             ganador="EquipoLocal";
        }else{
            ganador="EquipoVisitante";

        }

        //Ahora buscamos todos los pronosticos asociados a la apuesta actualizada

        List<PronosticoExactoUserEntity> pronosticosEntity= pronosticoExactoUserRepository.findPronosticoExactoByApuestaId(apuestaEntity);

         pronosticosEntity.forEach(pronostico ->{

             int puntuacion=0;
             boolean ganadorAcert=false;

             String pronosticoGanador;

             //Se obtiene el equipo ganador del pronostico
             if (pronostico.getMarcador_local() == pronostico.getMarcador_visitante()){
                 pronosticoGanador="empate";
             }else if (pronostico.getMarcador_local() >= pronostico.getMarcador_visitante()){
                 pronosticoGanador="EquipoLocal";
             } else{
                 pronosticoGanador="EquipoVisitante";
             }

             //Se obtiene el punto por acertar ganador
             if (ganador == pronosticoGanador){
                 puntuacion = puntuacion+1;
                 ganadorAcert = true;
             }

             //Se obtiene los puntos por acertar resultados
             if (ganadorAcert && pronostico.getMarcador_local() == apuestaEntity.getMarcador_local()){
                 puntuacion=puntuacion+2;
             }

             if (ganadorAcert && pronostico.getMarcador_visitante() == apuestaEntity.getMarcador_visitante()){
                 puntuacion=puntuacion+2;
             }

             pronostico.setPuntuacion(puntuacion);
             pronosticoExactoUserRepository.saveAndFlush(pronostico);

         });

         //Creamos los contadores para saber cuantos pronosticos hay por puntos

        int ganadoresPorPunto = 0;
        int ganadoresPorTresPuntos = 0;
        int ganadoresPorCincoPuntos = 0;

        //Marccamos como ganadores a todos los pronosticos que sean > a 1
        for (PronosticoExactoUserEntity pronosticoEntity : pronosticosEntity) {

            if (pronosticoEntity.getPuntuacion() >= 1) {
                pronosticoEntity.setState_apuesta(Constants.APUESTA_GANADA);
                pronosticoEntity.setState_date(LocalDateTime.now());

                if (pronosticoEntity.getPuntuacion() == 1){
                    ganadoresPorPunto++;
                }

                if (pronosticoEntity.getPuntuacion() == 3){
                    ganadoresPorTresPuntos++;
                }

                if (pronosticoEntity.getPuntuacion() == 5){
                    ganadoresPorCincoPuntos++;
                }

            } else {
                pronosticoEntity.setState_apuesta(Constants.APUESTA_PERDIDA);
                pronosticoEntity.setState_date(LocalDateTime.now());
            }

            pronosticoExactoUserRepository.saveAndFlush(pronosticoEntity);
        }

        //buscamos el pote asociado a la apuesta y le asignamos el numero de ganadores
        PoteEntity poteEntity = poteRepository.findPoteByApuestaExactaId(apuestaEntity);

        poteEntity.setNro_ganadores(ganadoresPorPunto+ganadoresPorTresPuntos+ganadoresPorCincoPuntos);

        //Buscamos los ganadores
        List<PronosticoExactoUserEntity> pronosticosGanadoresEntity= pronosticoExactoUserRepository.findPronosticoExactoGanadores(apuestaEntity);

        final int ganadoresPorCincoPuntosFinal = ganadoresPorCincoPuntos;
        final int ganadoresPortresPuntosFinal = ganadoresPorTresPuntos;
        final int ganadoresPorUnPuntosFinal = ganadoresPorPunto;

        // asignamos el dinero a los ganadores de acuerdo a su puntuacion
        pronosticosGanadoresEntity.forEach(pronosticoGanador->{

            if (pronosticoGanador.getPuntuacion()== 5){

                UserEntity usuarioGanador= userRepository.findUserByid(pronosticoGanador.getId_usuario().getId());

                int montoAsignado = (int) ((poteEntity.getSaldo() * 0.6)/ ganadoresPorCincoPuntosFinal);

                int saldoGanador = usuarioGanador.getSaldo() + montoAsignado;

                usuarioGanador.setSaldo(saldoGanador);

                userRepository.saveAndFlush(usuarioGanador);

            }

            if (pronosticoGanador.getPuntuacion()== 3){

                UserEntity usuarioGanador= userRepository.findUserByid(pronosticoGanador.getId_usuario().getId());

                int montoAsignado = (int) ((poteEntity.getSaldo() * 0.3)/ ganadoresPortresPuntosFinal);

                int saldoGanador = usuarioGanador.getSaldo() + montoAsignado;

                usuarioGanador.setSaldo(saldoGanador);

                userRepository.saveAndFlush(usuarioGanador);

            }

            if (pronosticoGanador.getPuntuacion()== 1){

                UserEntity usuarioGanador= userRepository.findUserByid(pronosticoGanador.getId_usuario().getId());

                int montoAsignado = (int) ((poteEntity.getSaldo() * 0.1)/ ganadoresPorUnPuntosFinal);

                int saldoGanador = usuarioGanador.getSaldo() + montoAsignado;

                usuarioGanador.setSaldo(saldoGanador);

                userRepository.saveAndFlush(usuarioGanador);

            }
        });

        // Y guardamos el usuario que realizo la solicitud
        poteEntity.setDistributed_by(userIdEntity);
        poteEntity.setDistribute(LocalDateTime.now());

        poteRepository.saveAndFlush(poteEntity);

    }
}
