package com.meta.sports.general.repositorys;

import com.meta.sports.general.entitys.DescuentosEntity;
import com.meta.sports.general.entitys.RecargasEntity;
import com.meta.sports.user.adapter.out.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DescuentosRepository extends JpaRepository<DescuentosEntity, Long> {

    List<DescuentosEntity> findAll();


    DescuentosEntity findDescuentoById(Long id);


    DescuentosEntity save(DescuentosEntity descuentos);


    @Query("SELECT p FROM DescuentosEntity p WHERE p.created_by = :user")
    List<DescuentosEntity> findDescuentosByUser(UserEntity user);

    @Query("SELECT p FROM DescuentosEntity p WHERE p.id_usuario=:user AND p.created_by=:aliado")
    List<DescuentosEntity> findDescuentosByAliadoAndUSer(UserEntity user,UserEntity aliado);



}
