package com.meta.sports.general.repositorys;

import com.meta.sports.general.entitys.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TournamentRepository extends JpaRepository<TournamentEntity, Long> {



    List<TournamentEntity> findAll();


    TournamentEntity findByName(String name);

    TournamentEntity findTournamentById(Long id);

    @Modifying
    @Query("UPDATE TournamentEntity c SET c.name = :name, c.start_date = :startDate, " +
            "c.end_date = :endDate WHERE c.id = :id")
    void updateTournament(String name, LocalDateTime startDate, LocalDateTime endDate, Long id);


    TournamentEntity save(TournamentEntity campeonato);

    void deleteById(Long id);

}
