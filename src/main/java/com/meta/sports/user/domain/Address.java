package com.meta.sports.user.domain;

import com.meta.sports.catalog.domain.Catalog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String city;

    private Catalog country;

    private Catalog state;

    private String street;

    private String suite;

    private Integer zipCode;
}
