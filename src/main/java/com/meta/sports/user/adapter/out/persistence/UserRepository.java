package com.meta.sports.user.adapter.out.persistence;

import com.meta.sports.general.entitys.ApuestasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmailContainingIgnoreCase(String email);

    List<UserEntity> findAll();

    @Query("SELECT ap FROM UserEntity ap WHERE ap.codigo_referido = :codigo")
    UserEntity findByCodigo(String codigo);

    UserEntity findByEmailAndPassword(String email, String password);

    UserEntity findUserByid(Long id);





    @Modifying
    @Query("UPDATE UserEntity u SET u.firstName = :firstName, u.lastName = :lastName, u.gender = :gender, u.phone = :phone," +
            "modified_by = :modifiedBy, modified = CURRENT_TIMESTAMP WHERE u.id = :id")
    void saveBasicDataById(String firstName, String lastName, String gender, String phone, Long modifiedBy, Long id);

    @Modifying
    @Query("UPDATE UserEntity u SET u.origin = :origin, u.lastLogin = CURRENT_TIMESTAMP WHERE u.id = :id")
    void saveLastLoginById(String origin, Long id);

    @Modifying
    @Query("UPDATE UserEntity u SET modified_by = :modifiedBy, modified = CURRENT_TIMESTAMP WHERE u.id = :id")
    void saveModifiedBy(Long modifiedBy, Long id);

    @Modifying
    @Query("UPDATE UserEntity u SET u.imgProfile = :document, " +
            "modified_by = :modifiedBy, modified = CURRENT_TIMESTAMP WHERE u.id = :id")
    void saveProfilePicById(String document, Long modifiedBy, Long id);

    @Modifying
    @Query("UPDATE UserEntity u SET u.status = :status, " +
           "modified_by = :modifiedBy, modified = CURRENT_TIMESTAMP WHERE u.id = :id")
    void saveStatusById(String status, Long modifiedBy, Long id);


}