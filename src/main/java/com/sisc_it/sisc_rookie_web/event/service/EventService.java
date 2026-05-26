package com.sisc_it.sisc_rookie_web.event.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sisc_it.sisc_rookie_web.event.domain.Event;
import com.sisc_it.sisc_rookie_web.event.domain.EventStatus;
import com.sisc_it.sisc_rookie_web.event.dto.EventCreateRequest;
import com.sisc_it.sisc_rookie_web.event.dto.EventResponse;
import com.sisc_it.sisc_rookie_web.event.dto.EventUpdateRequest;
import com.sisc_it.sisc_rookie_web.event.repository.EventRepository;
import com.sisc_it.sisc_rookie_web.member.domain.Member;
import com.sisc_it.sisc_rookie_web.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;

    public EventService(EventRepository eventRepository, MemberRepository memberRepository) {
        this.eventRepository = eventRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public EventResponse createEvent(EventCreateRequest request) {
        Member createdBy = findMemberById(request.createdByMemberId());
        EventStatus status = request.status() == null ? EventStatus.DRAFT : request.status();

        Event event = new Event(
            request.title(),
            request.description(),
            status,
            createdBy
        );

        return EventResponse.from(eventRepository.save(event));
    }

    public List<EventResponse> findEvents() {
        return eventRepository.findAll()
            .stream()
            .map(EventResponse::from)
            .toList();
    }

    public EventResponse findEvent(Long eventId) {
        return EventResponse.from(findEventById(eventId));
    }

    @Transactional
    public EventResponse updateEvent(Long eventId, EventUpdateRequest request) {
        Event event = findEventById(eventId);

        if (request.title() != null) {
            event.setTitle(request.title());
        }
        if (request.description() != null) {
            event.setDescription(request.description());
        }
        if (request.status() != null) {
            event.setStatus(request.status());
        }

        return EventResponse.from(event);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        Event event = findEventById(eventId);
        eventRepository.delete(event);
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
            .orElseThrow(() -> new IllegalArgumentException("행사를 찾을 수 없습니다."));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }
}
