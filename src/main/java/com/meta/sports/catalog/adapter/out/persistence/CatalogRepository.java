package com.meta.sports.catalog.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogRepository extends JpaRepository<CatalogEntity, Long> {

    List<CatalogEntity> findByListKeyOrderByOrder(String listKey);

}
