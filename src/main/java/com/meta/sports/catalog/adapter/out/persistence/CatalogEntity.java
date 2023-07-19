package com.meta.sports.catalog.adapter.out.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "catalog")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String listKey;

    @Column(nullable = false)
    private String item;

    @Column(nullable = false)
    private Integer order;

    @Column
    private Long parentItem;

}