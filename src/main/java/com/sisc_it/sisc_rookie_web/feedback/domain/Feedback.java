package com.sisc_it.sisc_rookie_web.feedback.domain;

import java.time.LocalDateTime;

import com.sisc_it.sisc_rookie_web.event.domain.Event;
import com.sisc_it.sisc_rookie_web.member.domain.Member;
import com.sisc_it.sisc_rookie_web.team.domain.Team;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(
    name = "feedbacks",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_feedback_event_member", columnNames = {"event_id", "member_id"})
    }
)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fixed at creation. Feedback edits should update content through a DTO and service permission check.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false, length = 2000)
    @Setter
    private String content;

    // Managed by the system, so no public setter.
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Feedback() {
    }

    public Feedback(Event event, Member member, String content) {
        this(event, member, member.getTeam(), content);
    }

    public Feedback(Event event, Member member, Team team, String content) {
        this.event = event;
        this.member = member;
        this.team = team;
        this.content = content;
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
