package com.meta.sports.user.application.service;

import com.meta.sports.user.adapter.out.persistence.CreateUserAdapter;
import com.meta.sports.user.application.port.in.UpdateProfileCommand;
import com.meta.sports.user.application.port.in.UpdateUser;
import com.meta.sports.user.application.port.out.FindUserPort;
import com.meta.sports.user.application.port.out.UpdateUserPort;
import com.meta.sports.user.domain.Address;
import com.meta.sports.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserService implements UpdateUser {

    @Autowired
    private CreateUserAdapter createUserAdapter;

    @Autowired
    private FindUserPort findUserPort;

    @Autowired
    private UpdateProfileCommand updateProfileCommand;

    @Autowired
    private UpdateUserPort updateUserPort;

    @Override
    public void lastLogin(Long id, String origin) {
        updateUserPort.lastLogin(id, origin);
    }

    @Override
    public void profileData(User profile, Long modifiedBy) {

        updateProfileCommand.validateData(profile);

        boolean newAddress = profile.getAddress() != null && findUserPort.address(profile.getId()) == null;

        Address address = profile.getAddress();

        //to avoid update
        if (newAddress) profile.setAddress(null);

        updateUserPort.profileData(profile, modifiedBy);

        if (newAddress) {
            createUserAdapter.address(address.getStreet(), address.getSuite(), address.getCity(),
                    address.getState().getKey(), address.getCountry().getId().intValue(), address.getZipCode(), profile.getId());
        }
    }


    @Override
    public void status(Long id, String status, User modifiedBy) {
        updateUserPort.status(id, status, modifiedBy.getId());
    }

}
