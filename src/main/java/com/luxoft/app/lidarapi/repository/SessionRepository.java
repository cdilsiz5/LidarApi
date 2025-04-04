package com.luxoft.app.lidarapi.repository;

import com.luxoft.app.lidarapi.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findBySessionId(String sessionId);

    boolean existsBySessionId(String sessionId);

}
