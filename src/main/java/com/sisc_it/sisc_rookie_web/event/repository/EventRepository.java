package com.sisc_it.sisc_rookie_web.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sisc_it.sisc_rookie_web.event.domain.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
