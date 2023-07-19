package com.meta.sports.general.repositorys;

import com.meta.sports.general.entitys.ApuestasEntity;
import com.meta.sports.general.entitys.SoccerGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ApuestasRepository extends JpaRepository<ApuestasEntity, Long> {

    List<ApuestasEntity> findAll();


    ApuestasEntity findApuestaById(Long id);


    ApuestasEntity save(ApuestasEntity apuestasEntity);

    @Query("SELECT ap FROM ApuestasEntity ap WHERE ap.id_juego.id = :juegoId AND ap.id_evento.id = :eventoId")
    List<ApuestasEntity> findByJuegoAndEventoList(Long juegoId, Long eventoId);


    @Query("SELECT ap FROM ApuestasEntity ap WHERE ap.id_juego.id = :juegoId AND ap.etapa = 'PRIMERA'")
    List<ApuestasEntity> findByidAndPrimera(Long juegoId);


    @Query("SELECT ap FROM ApuestasEntity ap WHERE ap.id_juego.id = :juegoId AND ap.etapa = 'SEGUNDA'")
    List<ApuestasEntity> findByidAndSegunda(Long juegoId);

    void deleteById(Long id);
}
