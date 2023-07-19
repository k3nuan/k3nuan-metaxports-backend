package com.meta.sports.general.repositorys;

import com.meta.sports.general.entitys.EventsEntity;
import com.meta.sports.general.entitys.SoccerGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsRepository extends JpaRepository<EventsEntity, Long> {

    List<EventsEntity> findAll();


    EventsEntity findEventById(Long id);

    EventsEntity save(EventsEntity event);

    void deleteById(Long id);
}
