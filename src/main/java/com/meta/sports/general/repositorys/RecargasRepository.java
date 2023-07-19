package com.meta.sports.general.repositorys;

import com.meta.sports.general.entitys.ApuestasEntity;
import com.meta.sports.general.entitys.PoteEntity;
import com.meta.sports.general.entitys.PronosticoUserEntity;
import com.meta.sports.general.entitys.RecargasEntity;
import com.meta.sports.user.adapter.out.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecargasRepository extends JpaRepository<RecargasEntity, Long> {


    List<RecargasEntity> findAll();


    RecargasEntity findRecargaById(Long id);


    RecargasEntity save(RecargasEntity poteEntity);

    @Query("SELECT p FROM RecargasEntity p WHERE p.created_by = :user")
    List<RecargasEntity> findRecargasByUser(UserEntity user);


    @Query("SELECT p FROM RecargasEntity p WHERE p.id_usuario=:user AND p.created_by=:aliado")
    List<RecargasEntity> findRecargasByAliadoAndUSer(UserEntity user,UserEntity aliado);



    void deleteById(Long id);
}
