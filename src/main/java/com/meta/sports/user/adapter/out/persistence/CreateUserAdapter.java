package com.meta.sports.user.adapter.out.persistence;

import com.meta.sports.common.CountryEntity;
import com.meta.sports.common.StateEntity;
import com.meta.sports.global.Constants;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.user.application.port.out.CreateUserPort;
import com.meta.sports.catalog.domain.Catalog;
import com.meta.sports.user.domain.User;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class CreateUserAdapter implements CreateUserPort {

    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public void address(String street, String suite, String city, String state, Integer country, Integer zipCode, Long user) {

        CountryEntity countryEntity = new CountryEntity(country, "", null, null);

        addressRepository.saveAndFlush(new AddressEntity(user, countryEntity, new StateEntity(state, "", countryEntity), city, street, suite, zipCode));
    }

    @Override
    @Transactional
    public User byEmail(User user) {

        System.out.println(user.getRole());

        Integer role = roleRepository.findByName(user.getRole()).getId();

        UserEntity entity = new UserEntity();
        entity.setPassword(user.getPassword());
        entity.setRole(new RoleEntity(role, user.getRole()));
        entity.setStatus(user.getStatus());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());

        if (entity.getRole().getName().equalsIgnoreCase("ADMIN")) {
            // Manejar el caso en que el usuario no sea aliado
            entity.setSaldo(0);
        }

        if (entity.getRole().getName().equalsIgnoreCase("ALIADO")) {
            // Manejar el caso en que el usuario no sea aliado
            entity.setSaldo(0);
        }
        if (entity.getRole().getName().equalsIgnoreCase("USER")) {
            // Manejar el caso en que el usuario no sea aliado
            entity.setSaldo(100);
        }

        entity.setEmail(user.getEmail());
        entity.setPhone(user.getPhone());
        entity.setBirthDate(user.getBirthDate());
        entity.setGender(user.getGender());
        entity.setCreated(LocalDate.now());

        // Generar código de referido único
        String codigoReferido = generateCodigoReferido();
        entity.setCodigo_referido(codigoReferido.toUpperCase());


        // Asignar referente si se proporcionó un código de referido
        if (user.getCodigoReferente() != null) {
            UserEntity referente = userRepository.findByCodigo(user.getCodigoReferente().toUpperCase());

            if (referente == null) {
                // Manejar el caso en que no se encuentre el juego
                throw new BadRequestException(164);

            }

            if (referente != null) {
                entity.setReferente(referente);
                entity.setCodigo_referente(referente.getCodigo_referido().toUpperCase());
            }
        }

        userRepository.saveAndFlush(entity);

        user.setSaldo(entity.getSaldo());
        user.setCodigoReferido(codigoReferido);
        user.setCodigoReferente(entity.getCodigo_referente());
        user.setId(entity.getId());



        return user;
    }

    @Override
    @Transactional
    public User createAliado(User user) {

        System.out.println(user.getRole());

        Integer role = roleRepository.findByName(user.getRole()).getId();

        UserEntity entity = new UserEntity();
        entity.setPassword(user.getPassword());
        entity.setRole(new RoleEntity(role, user.getRole()));
        entity.setStatus(user.getStatus());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setSaldo(0);
        entity.setEmail(user.getEmail());
        entity.setPhone(user.getPhone());
        entity.setBirthDate(user.getBirthDate());
        entity.setGender(user.getGender());
        entity.setCreated(LocalDate.now());

        // Generar código de referido único
        String codigoReferido = generateCodigoReferido();
        entity.setCodigo_referido(codigoReferido.toUpperCase());



        userRepository.saveAndFlush(entity);

        user.setSaldo(entity.getSaldo());
        user.setCodigoReferido(codigoReferido);
        user.setCodigoReferente(entity.getCodigo_referente());
        user.setId(entity.getId());



        return user;
    }

    private String generateCodigoReferido() {
        // Generar un código aleatorio único de 6 caracteres
        String codigoReferido = RandomStringUtils.randomAlphanumeric(8).toUpperCase();

        // Verificar si el código ya existe en la base de datos
        while (userRepository.findByCodigo(codigoReferido) != null) {
            codigoReferido = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
        }

        return codigoReferido;
    }

}
