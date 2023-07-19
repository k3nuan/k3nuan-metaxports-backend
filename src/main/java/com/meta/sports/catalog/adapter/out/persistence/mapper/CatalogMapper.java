package com.meta.sports.catalog.adapter.out.persistence.mapper;

import com.meta.sports.catalog.adapter.out.persistence.CatalogEntity;
import com.meta.sports.catalog.domain.Catalog;
import org.springframework.stereotype.Component;


@Component
public class CatalogMapper {

    public CatalogEntity mapToJpaEntity(Catalog catalog, String listKey) {
        return new CatalogEntity(catalog.getId(), listKey, null, null, null);
    }

    public Catalog mapToDomainEntity(CatalogEntity entity) {
        return new Catalog(entity.getId(), null, entity.getItem());
    }

    public Catalog mapToDomainEntity(CatalogEntity entity, String key) {
        return new Catalog(entity.getId(), key, entity.getItem());
    }
}