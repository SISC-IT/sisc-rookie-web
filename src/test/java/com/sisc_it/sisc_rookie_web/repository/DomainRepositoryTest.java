package com.sisc_it.sisc_rookie_web.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sisc_it.sisc_rookie_web.domain.Application;
import com.sisc_it.sisc_rookie_web.domain.ApplicationStatus;
import com.sisc_it.sisc_rookie_web.domain.Event;
import com.sisc_it.sisc_rookie_web.domain.EventStatus;
import com.sisc_it.sisc_rookie_web.domain.Feedback;
import com.sisc_it.sisc_rookie_web.domain.Member;
import com.sisc_it.sisc_rookie_web.domain.Position;
import com.sisc_it.sisc_rookie_web.domain.Role;
import com.sisc_it.sisc_rookie_web.domain.Team;

@SpringBootTest
class DomainRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Test
    void savesMemberWithTeamAndPosition() {
        Team team = teamRepository.save(new Team("Team Alpha"));

        Member member = new Member(
            "Alice",
            "alice@sisc.test",
            "hashed-password",
            "https://example.com/profile.png",
            Role.MEMBER,
            team,
            Position.LEADER
        );

        Member savedMember = memberRepository.saveAndFlush(member);

        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getTeam()).isNotNull();
        assertThat(savedMember.getTeam().getId()).isEqualTo(team.getId());
        assertThat(savedMember.getPosition()).isEqualTo(Position.LEADER);
        assertThat(savedMember.getRole()).isEqualTo(Role.MEMBER);
    }

    @Test
    void savesApplicationAndFeedbackWithEventRelationships() {
        Team team = teamRepository.save(new Team("Team Beta"));

        Member admin = memberRepository.save(new Member(
            "Admin",
            "admin@sisc.test",
            "hashed-password",
            "https://example.com/admin.png",
            Role.ADMIN,
            team,
            Position.LEADER
        ));

        Member applicant = memberRepository.save(new Member(
            "Bob",
            "bob@sisc.test",
            "hashed-password",
            "https://example.com/bob.png",
            Role.MEMBER,
            team,
            Position.MEMBER
        ));

        Event event = eventRepository.save(new Event(
            "Spring Rookie OT",
            "세투연 신규 부원 대상 OT",
            EventStatus.OPEN,
            admin
        ));

        Application application = applicationRepository.saveAndFlush(
            new Application(event, applicant, team, ApplicationStatus.PENDING)
        );

        Feedback feedback = feedbackRepository.saveAndFlush(
            new Feedback(event, applicant, team, "행사 진행이 매끄러웠습니다.")
        );

        assertThat(event.getId()).isNotNull();
        assertThat(event.getCreatedAt()).isNotNull();
        assertThat(application.getId()).isNotNull();
        assertThat(application.getAppliedAt()).isNotNull();
        assertThat(applicationRepository.existsByEventIdAndMemberId(event.getId(), applicant.getId())).isTrue();
        assertThat(feedback.getId()).isNotNull();
        assertThat(feedback.getCreatedAt()).isNotNull();
        assertThat(feedback.getContent()).isEqualTo("행사 진행이 매끄러웠습니다.");
    }
}
