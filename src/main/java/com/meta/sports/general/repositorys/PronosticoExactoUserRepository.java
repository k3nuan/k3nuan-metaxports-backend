package com.meta.sports.general.repositorys;

import com.meta.sports.general.entitys.ApuestaExactaEntity;
import com.meta.sports.general.entitys.PronosticoExactoUserEntity;
import com.meta.sports.general.entitys.PronosticoUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PronosticoExactoUserRepository extends JpaRepository<PronosticoExactoUserEntity, Long> {

    List<PronosticoExactoUserEntity> findAll();


    PronosticoExactoUserEntity findPronosticoExactoById(Long id);

    PronosticoExactoUserEntity save(PronosticoUserEntity pronosticoUserEntity);

    @Query("SELECT p FROM PronosticoExactoUserEntity p WHERE p.id_apuesta_exacta = :apuesta")
    List<PronosticoExactoUserEntity> findPronosticoExactoByApuestaId(ApuestaExactaEntity apuesta);


    @Query("SELECT p FROM PronosticoExactoUserEntity p WHERE p.id_apuesta_exacta=:apuesta AND p.state_apuesta='GANADOR'")
    List<PronosticoExactoUserEntity> findPronosticoExactoGanadores(ApuestaExactaEntity apuesta);
}
