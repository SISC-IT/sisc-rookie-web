package com.sisc_it.sisc_rookie_web.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Setter
    private String title;

    @Column(nullable = false, length = 2000)
    @Setter
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private EventStatus status;

    // 시스템이 기록하는 값이므로 외부 setter를 열지 않는다.
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // 작성자는 이벤트 생성 시점에 확정한다. 변경이 필요하면 DTO와 권한 검증을 거친 서비스 메서드로 처리한다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Member createdBy;

    protected Event() {
    }

    public Event(String title, String description, Member createdBy) {
        this(title, description, EventStatus.DRAFT, createdBy);
    }

    public Event(String title, String description, EventStatus status, Member createdBy) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdBy = createdBy;
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = EventStatus.DRAFT;
        }
    }
}
