package com.sisc_it.sisc_rookie_web.event.dto;

import com.sisc_it.sisc_rookie_web.domain.EventStatus;

public record EventCreateRequest(
    String title,
    String description,
    EventStatus status,
    Long createdByMemberId
) {
}
