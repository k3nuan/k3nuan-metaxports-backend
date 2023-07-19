package com.meta.sports.catalog.adapter.out.persistence;

import com.meta.sports.common.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<CountryEntity, Integer> {

}
