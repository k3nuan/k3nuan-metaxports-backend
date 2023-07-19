package com.meta.sports.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "country")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @Column
    private String shortName;

    @Column
    private String lang;

}