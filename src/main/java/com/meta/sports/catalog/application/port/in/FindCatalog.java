package com.meta.sports.catalog.application.port.in;

import com.meta.sports.catalog.domain.Catalog;

import java.util.List;

public interface FindCatalog {

    List<Catalog> findCountries();

    List<Catalog> findStates(Integer country);

}
