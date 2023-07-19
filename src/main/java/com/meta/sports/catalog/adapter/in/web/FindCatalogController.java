package com.meta.sports.catalog.adapter.in.web;

import com.meta.sports.catalog.application.port.in.FindCatalog;
import com.meta.sports.catalog.domain.Catalog;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogs")
@RequiredArgsConstructor
public class FindCatalogController {

    private final FindCatalog findCatalog;

    @GetMapping("/countries")
    public List<Catalog> findCountries() {
        return findCatalog.findCountries();
    }

    @GetMapping("/states")
    public List<Catalog> findStates(@RequestParam(required = false) Integer country) {
        return findCatalog.findStates(country == null || country.intValue() < 1 ? 1 : country);
    }
}
