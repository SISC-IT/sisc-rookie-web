package com.sisc_it.sisc_rookie_web.event.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;
import com.sisc_it.sisc_rookie_web.event.domain.EventStatus;
import com.sisc_it.sisc_rookie_web.event.repository.EventRepository;
import com.sisc_it.sisc_rookie_web.member.domain.Member;
import com.sisc_it.sisc_rookie_web.member.domain.Role;
import com.sisc_it.sisc_rookie_web.member.repository.MemberRepository;

@SpringBootTest
class EventControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void crudEvents() throws Exception {
        Member admin = memberRepository.save(new Member(
            "Event Admin",
            "event-admin@sisc.test",
            "hashed-password",
            Role.ADMIN
        ));

        MvcResult createResult = mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "title": "Spring Rookie OT",
                      "description": "신입 부원 대상 OT",
                      "status": "%s",
                      "createdByMemberId": %d
                    }
                    """.formatted(EventStatus.OPEN, admin.getId())))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Spring Rookie OT"))
            .andExpect(jsonPath("$.status").value("OPEN"))
            .andExpect(jsonPath("$.createdByMemberId").value(admin.getId()))
            .andReturn();

        Long eventId = ((Number) JsonPath.read(createResult.getResponse().getContentAsString(), "$.id")).longValue();

        mockMvc.perform(get("/api/events/{eventId}", eventId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(eventId))
            .andExpect(jsonPath("$.title").value("Spring Rookie OT"));

        mockMvc.perform(get("/api/events"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").exists());

        mockMvc.perform(patch("/api/events/{eventId}", eventId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "title": "Updated Rookie OT",
                      "status": "%s"
                    }
                    """.formatted(EventStatus.CLOSED)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Updated Rookie OT"))
            .andExpect(jsonPath("$.description").value("신입 부원 대상 OT"))
            .andExpect(jsonPath("$.status").value("CLOSED"));

        mockMvc.perform(delete("/api/events/{eventId}", eventId))
            .andExpect(status().isNoContent());

        assertThat(eventRepository.existsById(eventId)).isFalse();
    }
}
