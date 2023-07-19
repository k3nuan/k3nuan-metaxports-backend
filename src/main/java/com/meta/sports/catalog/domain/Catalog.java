package com.meta.sports.catalog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Catalog {

    private Long id;

    private String key;

    private String value;

    private Integer limit;

    public Catalog(Long id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }
}
