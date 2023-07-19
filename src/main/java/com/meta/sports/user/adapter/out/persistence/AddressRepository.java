package com.meta.sports.user.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    AddressEntity findByUserId(Long userId);

}