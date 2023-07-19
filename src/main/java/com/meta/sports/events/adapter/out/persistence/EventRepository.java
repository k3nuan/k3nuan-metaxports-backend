package com.meta.sports.events.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<EventEntity, EventPK> {

    @Query(nativeQuery = true, value = "SELECT * FROM event WHERE data->>'club' = :clubId " +
        " AND created >= CAST(:from AS TIMESTAMP) AND created <= CAST(:to AS TIMESTAMP) ORDER BY created DESC",
        countQuery = "SELECT COUNT(*) FROM event WHERE data->>'club' = :clubId " +
        " AND created >= CAST(:from AS TIMESTAMP) AND created <= CAST(:to AS TIMESTAMP)")
    Page<EventEntity> findAll(String clubId, LocalDateTime from, LocalDateTime to, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM event WHERE data->>'club' = :clubId ORDER BY created DESC",
           countQuery = "SELECT COUNT(*) FROM event WHERE data->>'club' = :clubId")
    Page<EventEntity> findAll(String clubId, Pageable pageable);

    @Query("SELECT e FROM EventTypeEntity e WHERE e.id = :id")
    EventTypeEntity findEventTypeById(Integer id);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO event (source, origin, type, user_id, detail, data, error_code, message, created_by)"
            + " VALUES (:source, :origin, :type, :user, :detail, CAST(:data AS JSON), :error, :message, :createdBy)")
    void saveEvent(String source, String origin, Integer type, Long user, String detail, String data,
                   Integer error, String message, Long createdBy);

}
