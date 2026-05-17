package com.sisc_it.sisc_rookie_web.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sisc_it.sisc_rookie_web.feedback.domain.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
