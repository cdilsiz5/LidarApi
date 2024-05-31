package com.luxoft.app.lidarapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxoft.app.lidarapi.dto.MetadataDto;
import com.luxoft.app.lidarapi.dto.SessionDto;
import com.luxoft.app.lidarapi.exception.SessionNotFoundException;
import com.luxoft.app.lidarapi.model.BinaryFile;
import com.luxoft.app.lidarapi.model.GroupInfo;
import com.luxoft.app.lidarapi.model.Session;
import com.luxoft.app.lidarapi.repository.SessionRepository;
import com.luxoft.app.lidarapi.request.SessionLidarDataRequest;
import com.luxoft.app.lidarapi.util.FileUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Data
public class SessionResourceServiceImpl implements SessionResourceService {

    private String dataFilePath;
    private String idxFilePath;
    private final FileUtil fileUtil;
    private BinaryFile binaryFile;
    private final ObjectMapper objectMapper;
    private final SessionRepository sessionRepository;
    private HashMap<String, BinaryFile> map = new HashMap<>();

    private void loadBinaryFile(String sessionId) throws IOException {
        dataFilePath = fileUtil.getDataFilePath(sessionId).toString();
        idxFilePath = fileUtil.getIdxFilePath(sessionId).toString();
        binaryFile = new BinaryFile(dataFilePath, idxFilePath, objectMapper);
        map.put(sessionId, binaryFile);
    }

    private void validateAndLoadSession(String sessionId) throws IOException {
        Session session = sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session ID " + sessionId + " not found."));
        if (!map.containsKey(session.getSessionId())) {
            loadBinaryFile(session.getSessionId());
        } else {
            binaryFile = map.get(sessionId);
        }
    }

    @Override
    public MetadataDto getMetadataDto(String sessionId) throws IOException {
        validateAndLoadSession(sessionId);

        List<GroupInfo> groups = binaryFile.getMetadata().getGroups();
        int totalGroups = groups.size();
        int startGroupId = groups.get(0).getGroup_id();
        long maxGroupSize = groups.stream().mapToLong(GroupInfo::getPoints).max().orElse(0);

        log.info("Fetched metadata with total groups: {}, start group ID: {}, and max group size: {}.", totalGroups, startGroupId, maxGroupSize);
        return MetadataDto.builder()
                .groups(totalGroups)
                .startGroupId(startGroupId)
                .maxGroupSize(maxGroupSize)
                .build();
    }

    @Override
    public byte[] getBinaryFile(SessionLidarDataRequest request) throws IOException {
        validateAndLoadSession(request.getSessionId());
        if (request.getStartGroupId() > request.getEndGroupId()) {
            throw new IllegalArgumentException("Start group ID must be less than or equal to end group ID.");
        }
        return binaryFile.getDataSegment(request.getStartGroupId(), request.getEndGroupId());
    }

    @Override
    public List<SessionDto> getSessionList() {
        List<Session> sessions = sessionRepository.findAll();
        return sessions.stream()
                .map(session -> SessionDto.builder()
                        .id(session.getId())
                        .sessionId(session.getSessionId())
                        .build())
                .collect(Collectors.toList());
    }
}
