package com.meta.sports.general.repositorys;

import com.meta.sports.general.entitys.SoccerGameEntity;
import com.meta.sports.general.entitys.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SoccerGameRepository extends JpaRepository<SoccerGameEntity, Long> {

    List<SoccerGameEntity> findAll();


    SoccerGameEntity findSoccerGameById(Long id);


    @Modifying
    @Query("UPDATE TournamentEntity c SET c.name = :name, c.start_date = :startDate, " +
            "c.end_date = :endDate WHERE c.id = :id")
    void updateSoccerGame(String name, LocalDateTime startDate, LocalDateTime endDate, Long id);


    SoccerGameEntity save(SoccerGameEntity soccerGame);

    void deleteById(Long id);
}
