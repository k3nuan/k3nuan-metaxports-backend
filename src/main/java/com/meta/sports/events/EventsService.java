package com.meta.sports.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meta.sports.events.adapter.out.persistence.EventMapper;
import com.meta.sports.events.adapter.out.persistence.EventRepository;
import com.meta.sports.events.domain.Event;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Log4j2
@AllArgsConstructor
public class EventsService {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final ObjectMapper jsonMapper;


    @Transactional
    public void createEvent(Event event) {
        String data = null;
        String initResource = null;
        String finalResource = null;

        if (event.getDataMap().size() > 0) {
            try {
                data = this.jsonMapper.writeValueAsString(event.getDataMap());
            } catch (Exception e) {
                log.error("cant convert datamap to JSON", e);
            }
        }

        if (event.getInitResourceObject() != null) {
            try {
                initResource = this.jsonMapper.writeValueAsString(event.getInitResourceObject());
            } catch (Exception e) {
                log.error("cant convert initial resource to JSON", e);
            }
        }

        if (event.getFinalResourceObject() != null) {
            try {
                finalResource = this.jsonMapper.writeValueAsString(event.getFinalResource());
            } catch (Exception e) {
                log.error("cant convert final resource to JSON", e);
            }
        }

        eventRepository.saveEvent(event.getSource(), event.getOrigin(), event.getType().getId(), event.getUser().getId(),
            event.getDetail(), data, event.getErrorCode(), event.getMessage(),
            event.getCreatedBy() == null ? event.getUser().getId() : event.getCreatedBy().getId());
    }

}
