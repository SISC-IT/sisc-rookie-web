package com.sisc_it.sisc_rookie_web.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private EventApplication application;

    @Column(nullable = false)
    private LocalDateTime checkedInAt;

    protected Attendance() {
    }

    public Attendance(Event event, Member member, EventApplication application, LocalDateTime checkedInAt) {
        this.event = event;
        this.member = member;
        this.application = application;
        this.checkedInAt = checkedInAt;
    }
}
