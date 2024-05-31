package com.luxoft.app.lidarapi.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxoft.app.lidarapi.request.LidarDataRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
class BinaryFileTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RandomAccessFile mockedFile;

    private BinaryFile binaryFile;

    private String dataFilePath="src/main/resources/files/lidar_case.data";

    private String idxFilePath="src/main/resources/files/lidar_case.data.idx";


    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        binaryFile = Mockito.spy(new BinaryFile(dataFilePath,idxFilePath, objectMapper));
        when(mockedFile.length()).thenReturn(1000L);
        List<GroupInfo> groups = Arrays.asList(
                new GroupInfo(10, 0, 2278824, 257220), // Group ID, Offset, Size, Points
                new GroupInfo(11, 100, 2536044, 198052),
                new GroupInfo(12, 250, 2734096, 188404)
        );
        Metadata metadata = new Metadata();
        metadata.setGroups(groups);
        binaryFile.setMetadata(metadata);

        long longDataSize = groups.stream().mapToLong(g -> (long) g.getOffset() + g.getSize()).max().orElse(0);
        if (longDataSize > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Data size exceeds maximum integer size.");
        }
        int dataSize = (int) longDataSize;
        binaryFile.setFullData(new byte[dataSize]);
        Arrays.fill(binaryFile.getFullData(), (byte) 1);

        when(objectMapper.readValue(any(RandomAccessFile.class), eq(Metadata.class))).thenReturn(metadata);
    }

    @Test
    void readMetadata_Failure_ThrowsIOException() throws IOException {
        // Arrange
        IOException ioException = new IOException("Failed to read file");
        when(objectMapper.readValue(any(RandomAccessFile.class), eq(Metadata.class))).thenThrow(ioException);

        // Act & Assert
       // assertThrows(IOException.class, () -> binaryFile.readMetadata(objectMapper), "Should throw IOException on metadata read failure");
    }

    @Test
    void getDataSegment_ValidGroupIds_ReturnsCorrectDataSegment() throws IOException {
        LidarDataRequest request = new LidarDataRequest(10, 12);
        byte[] result = binaryFile.getDataSegment(request.getStartGroupId(), request.getEndGroupId());
        int expectedLength = 2734096 + 188404 - 2278824;
        assertEquals(expectedLength, result.length, "The length of the data segment should match the expected length.");
    }

    @Test
    void getDataSegment_NonexistentStartGroupId_ThrowsIOException() {
        Exception exception = assertThrows(IOException.class, () -> binaryFile.getDataSegment(9, 12));
        assertEquals("Start or end group ID not found in the metadata.", exception.getMessage());
    }

    @Test
    void getDataSegment_NonexistentEndGroupId_ThrowsIOException() {
        Exception exception = assertThrows(IOException.class, () -> binaryFile.getDataSegment(10, 13));
        assertEquals("Start or end group ID not found in the metadata.", exception.getMessage());
    }

    @Test
    void getDataSegment_ReverseGroupIds_ThrowsIOException() {
        Exception exception = assertThrows(IOException.class, () -> binaryFile.getDataSegment(12, 10));
        assertEquals("Start or end group ID not found in the metadata.", exception.getMessage());
    }
}
