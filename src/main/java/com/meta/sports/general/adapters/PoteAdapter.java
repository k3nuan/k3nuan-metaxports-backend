package com.meta.sports.general.adapters;

import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.domain.Pote;
import com.meta.sports.general.entitys.ApuestaExactaEntity;
import com.meta.sports.general.entitys.ApuestasEntity;
import com.meta.sports.general.entitys.PoteEntity;
import com.meta.sports.general.ports.out.PotePort;
import com.meta.sports.general.repositorys.ApuestaExactaRepository;
import com.meta.sports.general.repositorys.ApuestasRepository;
import com.meta.sports.general.repositorys.PoteRepository;
import com.meta.sports.global.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class PoteAdapter implements PotePort {

    private final PoteRepository poteRepository;
    private final ApuestasRepository apuestasRepository;

    private final ApuestaExactaRepository apuestaExactaRepository;



    @Override
    public Pote findpotebyIdApuesta(Long id) {

        ApuestasEntity apuestasEntity=apuestasRepository.findApuestaById(id);

        PoteEntity poteEntity = poteRepository.findPoteByApuesta(apuestasEntity);

        if (poteEntity == null) {
            throw new BadRequestException(323);
        }

        Pote pote = new Pote();
        pote.setId(poteEntity.getId());

        pote.setIdApuesta(poteEntity.getId_apuesta().getId());



        pote.setSaldo(poteEntity.getSaldo());
        pote.setNroApuestas(poteEntity.getNro_apuestas());
        pote.setNroGanadores(poteEntity.getNro_ganadores());

        return pote;

    }

    @Override
    @Transactional
    public Pote findpotebyIdApuestaExacta(Long id) {

        ApuestaExactaEntity apuestasEntity=apuestaExactaRepository.findApuestaExactaById(id);

        PoteEntity poteEntity = poteRepository.findPoteByApuestaExacta(apuestasEntity);

        if (poteEntity == null) {
            throw new BadRequestException(323);
        }

        Pote pote = new Pote();
        pote.setId(poteEntity.getId());


        pote.setIdApuestaExacta(poteEntity.getId_apuesta_exacta().getId());




        pote.setSaldo(poteEntity.getSaldo());
        pote.setNroApuestas(poteEntity.getNro_apuestas());
        pote.setNroGanadores(poteEntity.getNro_ganadores());

        return pote;

    }
}
