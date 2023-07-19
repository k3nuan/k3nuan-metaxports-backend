package com.meta.sports.catalog.adapter.out.persistence;

import com.meta.sports.common.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<StateEntity, String> {

    List<StateEntity> findAllByCountryId(Integer country);

}
