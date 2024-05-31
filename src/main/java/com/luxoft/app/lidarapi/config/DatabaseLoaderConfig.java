package com.luxoft.app.lidarapi.config;
import com.luxoft.app.lidarapi.model.Session;
import com.luxoft.app.lidarapi.repository.SessionRepository;
import com.luxoft.app.lidarapi.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Arrays;

@Configuration
public class DatabaseLoaderConfig {

    private final SessionRepository sessionRepository;
    private final FileUtil fileUtil;

    public DatabaseLoaderConfig(SessionRepository sessionRepository, FileUtil fileUtil) {
        this.sessionRepository = sessionRepository;
        this.fileUtil = fileUtil;
    }

    @Bean
    CommandLineRunner loadSessionData() {
        return args -> {
            File basePath = new File("src/main/resources/files/sessions");
            if (basePath.exists() && basePath.isDirectory()) {
                Arrays.stream(basePath.listFiles())
                        .filter(file -> file.getName().endsWith(".data"))
                        .forEach(file -> {
                            String sessionId = file.getName().replace(".data", "");
                            if (!sessionRepository.existsBySessionId(sessionId)) {
                                sessionRepository.save(new Session(null, sessionId));
                            }
                        });
            }
        };
    }
}
