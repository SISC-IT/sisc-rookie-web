package com.sisc_it.sisc_rookie_web.event.dto;

import java.time.LocalDateTime;

import com.sisc_it.sisc_rookie_web.domain.Event;
import com.sisc_it.sisc_rookie_web.domain.EventStatus;

public record EventResponse(
    Long id,
    String title,
    String description,
    EventStatus status,
    LocalDateTime createdAt,
    Long createdByMemberId,
    String createdByMemberName
) {

    public static EventResponse from(Event event) {
        return new EventResponse(
            event.getId(),
            event.getTitle(),
            event.getDescription(),
            event.getStatus(),
            event.getCreatedAt(),
            event.getCreatedBy().getId(),
            event.getCreatedBy().getName()
        );
    }
}
