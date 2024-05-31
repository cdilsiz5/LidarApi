package com.luxoft.app.lidarapi.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileUtil {

    @Value("${lidar.files-base-path}")
    private String basePath;

    public Path getDataFilePath(String sessionId) {
        return Paths.get(basePath, "sessions", sessionId + ".data");
    }

    public Path getIdxFilePath(String sessionId) {
        return Paths.get(basePath, "sessions", sessionId + ".data.idx");
    }
}
