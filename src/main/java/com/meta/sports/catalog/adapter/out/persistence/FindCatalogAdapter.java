package com.meta.sports.catalog.adapter.out.persistence;

import com.meta.sports.catalog.adapter.out.persistence.mapper.CatalogMapper;
import com.meta.sports.catalog.application.port.out.FindCatalogPort;
import com.meta.sports.catalog.domain.Catalog;
import com.meta.sports.common.CountryEntity;
import com.meta.sports.common.StateEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FindCatalogAdapter implements FindCatalogPort {

    private final CatalogMapper catalogMapper;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;


    @Override
    public List<Catalog> findCountries() {

        List<Catalog> countries = new ArrayList<>();

        for (CountryEntity e: countryRepository.findAll(Sort.by("id").ascending())) {
            countries.add(new Catalog(e.getId().longValue(), e.getLang(), e.getName()));
        }

        return countries;
    }

    @Override
    public List<Catalog> findStates(Integer country) {

        List<StateEntity> states = stateRepository.findAllByCountryId(country);
        List<Catalog> countryStates = new ArrayList<>();

        for (StateEntity e: states)
            countryStates.add(new Catalog(null, e.getCode(), e.getName()));

        return countryStates;
    }

}
