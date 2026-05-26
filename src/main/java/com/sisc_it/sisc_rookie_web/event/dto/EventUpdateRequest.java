package com.sisc_it.sisc_rookie_web.event.dto;

import com.sisc_it.sisc_rookie_web.event.domain.EventStatus;

public record EventUpdateRequest(
    String title,
    String description,
    EventStatus status
) {
}
