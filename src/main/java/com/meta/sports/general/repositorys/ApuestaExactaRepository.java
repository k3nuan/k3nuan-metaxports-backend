package com.meta.sports.general.repositorys;

import com.meta.sports.general.entitys.ApuestaExactaEntity;
import com.meta.sports.general.entitys.ApuestasEntity;
import com.meta.sports.general.entitys.PoteEntity;
import com.meta.sports.general.entitys.SoccerGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApuestaExactaRepository extends JpaRepository<ApuestaExactaEntity, Long> {

    List<ApuestaExactaEntity> findAll();


    ApuestaExactaEntity findApuestaExactaById(Long id);

    @Query("SELECT p FROM ApuestaExactaEntity p WHERE p.id_juego = :soccerGameEntity")
    List<ApuestaExactaEntity> findApuestaExactaByPartido(SoccerGameEntity soccerGameEntity);


    ApuestaExactaEntity save(ApuestaExactaEntity apuestaExactaEntity);



    void deleteById(Long id);
}
