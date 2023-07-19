package com.meta.sports.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "state", uniqueConstraints = @UniqueConstraint(name = "stateCountry", columnNames = { "code", "country_id" }))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateEntity implements Serializable {

    @Id
    private String code;

    @Column(nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "country_id")
    private CountryEntity country;

}
