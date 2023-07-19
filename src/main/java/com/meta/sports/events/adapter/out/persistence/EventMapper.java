package com.meta.sports.events.adapter.out.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meta.sports.events.domain.Event;
import com.meta.sports.events.domain.EventType;
import com.meta.sports.user.adapter.out.persistence.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class EventMapper {

    private final UserMapper userMapper;

    public Event mapToDomainEntity(EventEntity entity) {

        Event event = new Event(entity.getSource(), entity.getOrigin(), mapToDomainEntity(entity.getType()),
            userMapper.mapToDomainEntity(entity.getUser(), 1), entity.getDetail(), entity.getData(), null,
            entity.getErrorCode(), entity.getMessage(), userMapper.mapToDomainEntity(entity.getCreatedBy(), 1),
            entity.getCreated(), null, null, null, null);

        return event;
    }

    public EventType mapToDomainEntity(EventTypeEntity entity) {
        return new EventType(entity.getId(), entity.getName(), entity.getDescription());
    }

    public Event mapToDomainEntity(EventEntity entity, ObjectMapper objectMapper) {

        Event event = new Event(entity.getSource(), entity.getOrigin(), mapToDomainEntity(entity.getType()),
                userMapper.mapToDomainEntity(entity.getUser(), 1), entity.getDetail(), entity.getData(), new HashMap<>(),
                entity.getErrorCode(), entity.getMessage(), userMapper.mapToDomainEntity(entity.getCreatedBy(), 1),
                entity.getCreated(), null, null, null, null);

        if (event.getData() != null) {
            try { event.setDataMap(objectMapper.readValue(event.getData(), Map.class)); }
            catch (IOException e) {}
        }

        return event;
    }
}
