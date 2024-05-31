package com.luxoft.app.lidarapi.service;

import com.luxoft.app.lidarapi.dto.MetadataDto;
import com.luxoft.app.lidarapi.dto.SessionDto;
import com.luxoft.app.lidarapi.request.SessionLidarDataRequest;

import java.io.IOException;
import java.util.List;

public interface SessionResourceService {


    MetadataDto getMetadataDto(String sessionId) throws IOException;

    byte[] getBinaryFile(SessionLidarDataRequest request) throws IOException;

    List<SessionDto> getSessionList();


}
