package com.meta.sports.user.adapter.out.persistence.mapper;

import com.meta.sports.common.CountryEntity;
import com.meta.sports.common.StateEntity;
import com.meta.sports.catalog.domain.Catalog;
import com.meta.sports.user.adapter.out.persistence.AddressEntity;
import com.meta.sports.user.domain.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressEntity mapToJpaEntity(Long user, Address address) {

        CountryEntity country = new CountryEntity(address.getCountry().getId().intValue(), "", null, null);

        return new AddressEntity(user, country, new StateEntity(address.getState().getKey(), "", country),
            address.getCity(), address.getStreet(), address.getSuite(), address.getZipCode());
    }

    public Address mapToDomainEntity(AddressEntity entity, int securityLevel) {
        Address address = new Address(entity.getCity(),
            new Catalog(entity.getCountry().getId().longValue(), null, entity.getCountry().getName()),
            new Catalog(null, entity.getState().getCode(), entity.getState().getName()),
            entity.getStreet(), entity.getSuite(), entity.getZipCode());

        switch (securityLevel) {
            case 1:
                address.setStreet(null);
                address.setSuite(null);
                address.setZipCode(null);
                return address;
            default: return address;
        }
    }

}
