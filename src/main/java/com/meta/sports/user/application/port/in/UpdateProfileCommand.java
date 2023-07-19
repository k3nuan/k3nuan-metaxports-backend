package com.meta.sports.user.application.port.in;

import com.meta.sports.catalog.application.port.in.FindCatalog;
import com.meta.sports.catalog.domain.Catalog;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateProfileCommand {

    private final FindCatalog findCatalog;
    

    public User validateData(User user) {

        if (user == null || user.getFirstName() == null || user.getFirstName().trim().length() < 2
            || user.getLastName() == null || user.getLastName().trim().length() < 2 || user.getGender() == null
            || user.getGender().trim().length() == 0 || user.getGender().trim().length() > 10) {
            throw new BadRequestException(101);
        }
        else if (user.getPhone() == null || user.getPhone().trim().length() < 10) {
            throw new BadRequestException(101);
        }
        else if (user.getAddress() == null || user.getAddress().getCountry() == null || user.getAddress().getCountry().getId() == null ||
                 user.getAddress().getCountry().getId().intValue() < 1 || user.getAddress().getStreet() == null ||
                 user.getAddress().getStreet().trim().length() == 0 && user.getAddress().getState() == null ||
                 user.getAddress().getState().getKey() == null || user.getAddress().getState().getKey().trim().length() == 0 ||
                 user.getAddress().getCity() == null || user.getAddress().getCity().trim().length() == 0 || user.getAddress().getZipCode() == null ||
                 user.getAddress().getZipCode().intValue() < 1) {
            throw new BadRequestException(101);
        }

        List<Catalog> states = findCatalog.findStates(user.getAddress().getCountry().getId().intValue());

        if (states == null || states.size() == 0 || !states.stream().anyMatch(s -> s.getKey().equalsIgnoreCase(user.getAddress().getState().getKey()))) {
            throw new BadRequestException(101);
        }

        //trim strings
        user.setFirstName(user.getFirstName().trim());
        user.setLastName(user.getLastName().trim());
        user.setPhone(user.getPhone().trim());

        user.getAddress().setStreet(user.getAddress().getStreet().trim());
        user.getAddress().setSuite(user.getAddress().getSuite() != null ? user.getAddress().getSuite().trim() : null);
        user.getAddress().setCity(user.getAddress().getCity().trim());

        if (user.getGender().trim().equalsIgnoreCase("male")  ||
            user.getGender().trim().equalsIgnoreCase("female")) {
            user.setGender(user.getGender().trim().toLowerCase());
        }

        return user;
    }

}
