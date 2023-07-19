package com.meta.sports.catalog.application.port.out;

import com.meta.sports.catalog.domain.Catalog;

import java.util.List;

public interface FindCatalogPort {

    List<Catalog> findCountries();

    List<Catalog> findStates(Integer country);

}
