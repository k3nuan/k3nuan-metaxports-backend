package com.meta.sports.user.adapter.out.persistence;

import com.meta.sports.common.CountryEntity;
import com.meta.sports.common.StateEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_address")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressEntity {

    @Id
    private Long userId;

    @OneToOne
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    @OneToOne
    @JoinColumn(name = "state_code")
    private StateEntity state;

    @Column(nullable = false)
    private String city;

    @Column(name = "street_address", nullable = false)
    private String street;

    @Column
    private String suite;

    @Column
    private Integer zipCode;

}
