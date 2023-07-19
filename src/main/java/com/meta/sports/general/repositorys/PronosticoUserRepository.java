package com.meta.sports.general.repositorys;

import com.meta.sports.general.entitys.ApuestasEntity;
import com.meta.sports.general.entitys.PoteEntity;
import com.meta.sports.general.entitys.PronosticoUserEntity;
import com.meta.sports.general.entitys.SoccerGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PronosticoUserRepository extends JpaRepository<PronosticoUserEntity, Long>  {

    List<PronosticoUserEntity> findAll();


    PronosticoUserEntity findPronosticoById(Long id);

    PronosticoUserEntity save(PronosticoUserEntity pronosticoUserEntity);

    @Query("SELECT p FROM PronosticoUserEntity p WHERE p.id_apuesta = :apuesta")
    List<PronosticoUserEntity> findPronosticoByApuestaId(ApuestasEntity apuesta);


    @Query("SELECT p FROM PronosticoUserEntity p WHERE p.id_apuesta=:apuesta AND p.state_apuesta='GANADOR'")
    List<PronosticoUserEntity> findPronosticoGanadores(ApuestasEntity apuesta);


    void deleteById(Long id);

}
