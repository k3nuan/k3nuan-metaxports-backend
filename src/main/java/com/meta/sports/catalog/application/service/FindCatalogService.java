package com.meta.sports.catalog.application.service;

import com.meta.sports.catalog.application.port.in.FindCatalog;
import com.meta.sports.catalog.application.port.out.FindCatalogPort;
import com.meta.sports.catalog.domain.Catalog;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FindCatalogService implements FindCatalog {

    private final FindCatalogPort findCatalogPort;

    @Override
    public List<Catalog> findCountries() {
        return findCatalogPort.findCountries();
    }

    @Override
    public List<Catalog> findStates(Integer country) {
        return findCatalogPort.findStates(country);
    }

}
