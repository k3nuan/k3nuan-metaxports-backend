package com.meta.sports.general.repositorys;

import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.entitys.ApuestaExactaEntity;
import com.meta.sports.general.entitys.ApuestasEntity;
import com.meta.sports.general.entitys.DescuentosEntity;
import com.meta.sports.general.entitys.PoteEntity;
import com.meta.sports.user.adapter.out.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PoteRepository extends JpaRepository<PoteEntity, Long> {

    List<PoteEntity> findAll();


    PoteEntity findPoteById(Long id);

    @Query("SELECT p FROM PoteEntity p WHERE p.id_apuesta = :apuesta")
    PoteEntity findPoteByApuesta(ApuestasEntity apuesta);

    @Query("SELECT p FROM PoteEntity p WHERE p.id_apuesta_exacta = :apuesta")
    PoteEntity findPoteByApuestaExacta(ApuestaExactaEntity apuesta);


    PoteEntity save(PoteEntity poteEntity);

    @Query("SELECT p FROM PoteEntity p WHERE p.id_apuesta = :apuesta")
    PoteEntity findPoteByApuestaId(ApuestasEntity apuesta);

    @Query("SELECT p FROM PoteEntity p WHERE p.id_apuesta_exacta = :apuesta")
    PoteEntity findPoteByApuestaExactaId(ApuestaExactaEntity apuesta);

    void deleteById(Long id);
}
