package com.luxoft.app.lidarapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxoft.app.lidarapi.dto.MetadataDto;
import com.luxoft.app.lidarapi.model.BinaryFile;
import com.luxoft.app.lidarapi.model.GroupInfo;
import com.luxoft.app.lidarapi.model.Metadata;
import com.luxoft.app.lidarapi.request.LidarDataRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DataResourceServiceImplTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private BinaryFile binaryFile;

    @InjectMocks
    private DataResourceServiceImpl service;

    private String dataFilePath = "src/main/resources/files/lidar_case.data";
    private String idxFilePath = "src/main/resources/files/lidar_case.data.idx";

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        Metadata metadata = new Metadata();
        metadata.setGroups(Arrays.asList(
                new GroupInfo(10, 0, 150, 50),
                new GroupInfo(11, 150, 200, 50),
                new GroupInfo(12, 350, 250, 50)
        ));

        when(binaryFile.getMetadata()).thenReturn(metadata);
        doNothing().when(binaryFile).initializeAndLoadFullData();
        when(binaryFile.getDataSegment(anyInt(), anyInt())).thenReturn(new byte[150]);
        when(binaryFile.getFullData()).thenReturn(new byte[600]); // Adjust size accordingly
        service.setBinaryFile(binaryFile);
        service.setDataFilePath(dataFilePath);
        service.setIdxFilePath(idxFilePath);

    }

    @Test
    void getBinaryFile_whenValidGroupIds_returnsData() throws IOException {
        LidarDataRequest request = new LidarDataRequest(10, 12);
        byte[] expectedData = new byte[150];
        when(binaryFile.getDataSegment(10, 12)).thenReturn(expectedData);

        byte[] result = service.getBinaryFile(request);

        assertArrayEquals(expectedData, result, "The fetched data segment should match the expected byte array.");
        verify(binaryFile).getDataSegment(10, 12);
    }

    @Test
    void getBinaryFile_whenInvalidGroupIds_throwsIllegalArgumentException() {
        LidarDataRequest request = new LidarDataRequest(12, 10);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.getBinaryFile(request);
        });

        assertEquals("Start group ID must be less than or equal to end group ID.", exception.getMessage());
    }

    @Test
    void getMetadata_whenCalled_returnsCorrectMetadata() throws IOException {
        MetadataDto metadataDto = service.getMetadataDto();

        assertNotNull(metadataDto, "Metadata should not be null.");
        assertEquals(3, metadataDto.getGroups(), "Should return correct number of groups.");
        assertEquals(10, metadataDto.getStartGroupId(), "Should return correct start group ID.");
        assertEquals(350, metadataDto.getMaxGroupSize(), "Should return correct max group size.");
    }
    @Test
    void getMetadataDto_initializesBinaryFileWhenNull() throws IOException {
        Metadata expectedMetadata = new Metadata();
        expectedMetadata.setGroups(Arrays.asList(new GroupInfo(10, 0, 150, 50)));

        when(objectMapper.readValue(any(RandomAccessFile.class), eq(Metadata.class))).thenReturn(expectedMetadata);

        MetadataDto result = service.getMetadataDto();

        assertNotNull(result, "Metadata DTO should not be null.");
        assertEquals(3, result.getGroups(), "Should return correct number of groups.");
        assertEquals(10, result.getStartGroupId(), "Should return correct start group ID.");
        assertEquals(350, result.getMaxGroupSize(), "Should return correct max group size.");

        assertNotNull(service.getBinaryFile(), "BinaryFile should have been initialized.");
    }
}

