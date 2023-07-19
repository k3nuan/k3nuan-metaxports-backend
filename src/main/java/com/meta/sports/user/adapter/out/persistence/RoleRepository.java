package com.meta.sports.user.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    RoleEntity findByName(String name);

}
