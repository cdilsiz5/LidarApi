package com.luxoft.app.lidarapi.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Slf4j
@RequiredArgsConstructor
public class BinaryFile {
    private byte[] fullData;

    private Metadata metadata;

    private String dataFilePath;

    private String idxFilePath;

    private ObjectMapper objectMapper;


    public BinaryFile(String dataFilePath, String idxFilePath,ObjectMapper objectMapper) throws IOException {
        this.dataFilePath = dataFilePath;
        this.idxFilePath = idxFilePath;
        this.objectMapper=objectMapper;
        initializeAndLoadFullData();
        initializeAndLoadMetaData();
    }

    private void initializeAndLoadFullData() throws IOException {
        log.info("Initializing and loading full data from {}.", dataFilePath);
        try (RandomAccessFile file = new RandomAccessFile(dataFilePath, "r");
             FileChannel channel = file.getChannel()) {
            long fileSize = file.length();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            this.fullData = new byte[(int) fileSize];
            buffer.get(this.fullData);

        }
    }
    private void initializeAndLoadMetaData() throws IOException {
        log.info("Initializing and loading full data from {}.", dataFilePath);
        log.info("Reading metadata from {}", idxFilePath);
        try (RandomAccessFile idxFile = new RandomAccessFile(idxFilePath, "r")) {
            this.metadata= objectMapper.readValue(idxFile, Metadata.class);
        } catch (IOException e) {
            log.error("Error reading metadata from {}: {}", idxFilePath, e.getMessage());
            throw e;
        }
    }

    public byte[] getDataSegment(int startGroupId, int endGroupId) throws IOException {
        long startOffset = -1;
        long endOffset = -1;
        List<GroupInfo> groups = this.metadata.getGroups();

        for (GroupInfo group : groups) {
            if (group.getGroup_id() == startGroupId) {
                startOffset = group.getOffset();
                log.info("Found start offset: {}", startOffset);
            }
            if (group.getGroup_id() == endGroupId) {
                endOffset = group.getOffset() + group.getSize();
                log.info("Calculated end offset: {}", endOffset);
                break;
            }
        }

        if (startOffset == -1 || endOffset == -1) {
            log.error("Start or end group ID not found in the metadata.");
            throw new IOException("Start or end group ID not found in the metadata.");
        }

        int length = (int) (endOffset - startOffset);
        byte[] segmentData = new byte[length];
        System.arraycopy(this.fullData, (int) startOffset, segmentData, 0, length);
        return segmentData;
    }


}
