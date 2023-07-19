package com.meta.sports.events.adapter.in.web;

import com.meta.sports.events.EventsService;
import com.meta.sports.events.domain.Event;
import com.meta.sports.global.Constants;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.user.application.port.in.FindUser;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RestController
@AllArgsConstructor
public class FindEventController {

    private final FindUser findUser;

    private final EventsService eventsService;


    @GetMapping("/api/v1/events")
    @PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "') or hasRole('" + Constants.ROLE_USER + "')")
    public Page<Event> findEventsByParams(Authentication authentication,
                                          @RequestParam(required = false) String from, @RequestParam(required = false) String to,
                                          @RequestParam Integer page, @RequestParam Integer items) {

        LocalDateTime fromDate = null, toDate = null;

        if (from != null && from.trim().length() > 9) {
            try {
                fromDate = LocalDateTime.parse(from);
            } catch (DateTimeParseException e) {
                throw new BadRequestException(101);
            }
        }

        if (to != null && to.trim().length() > 9) {
            try {
                toDate = LocalDateTime.parse(to);
            } catch (DateTimeParseException e) {
                throw new BadRequestException(101);
            }
        }

        if ((fromDate != null && toDate == null) || (fromDate == null && toDate != null) ||
                (fromDate != null && toDate != null && fromDate.isAfter(toDate)))
            throw new BadRequestException(101);

        return null;
    }

}
