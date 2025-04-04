package com.luxoft.app.lidarapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxoft.app.lidarapi.dto.MetadataDto;
import com.luxoft.app.lidarapi.model.BinaryFile;
import com.luxoft.app.lidarapi.model.GroupInfo;
import com.luxoft.app.lidarapi.model.Metadata;
import com.luxoft.app.lidarapi.request.LidarDataRequest;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Data

public class DataResourceServiceImpl implements DataResourceService {

    @Value("${lidar.data-file-path}")
    private String dataFilePath;

    @Value("${lidar.idx-file-path}")
    private String idxFilePath;

    private final ObjectMapper objectMapper;

    private BinaryFile binaryFile;

    @Override
    public MetadataDto getMetadataDto() throws IOException {
        if (binaryFile == null) {
            log.info("Initializing binary file data for the first time.");
            binaryFile = new BinaryFile(dataFilePath,idxFilePath,objectMapper);
        }
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
    public byte[] getBinaryFile(LidarDataRequest request) throws IOException {
        if (request.getStartGroupId() > request.getEndGroupId()) {
            throw new IllegalArgumentException("Start group ID must be less than or equal to end group ID.");
        }
        if (binaryFile == null || binaryFile.getFullData() == null) {
            log.info("Loading binary file data due to cache miss.");
            binaryFile = new BinaryFile(dataFilePath,idxFilePath,objectMapper);
        }
        return binaryFile.getDataSegment(request.getStartGroupId(), request.getEndGroupId());
    }



}