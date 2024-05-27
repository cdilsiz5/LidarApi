package com.luxoft.app.lidarapi.service;


import com.luxoft.app.lidarapi.dto.MetadataDto;
import com.luxoft.app.lidarapi.request.LidarDataRequest;

import java.io.IOException;

public interface DataResourceService {
     MetadataDto getMetadataDto() throws IOException;



}
