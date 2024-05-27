package com.luxoft.app.lidarapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class BinaryFile {
    private byte[] fullData;
    private Metadata metadata;

    public void initializeAndLoadFullData(Metadata metadata, String dataFilePath) throws IOException {
        log.info("Initializing and loading full data from {}.", dataFilePath);
        this.metadata = metadata;

        try (RandomAccessFile file = new RandomAccessFile(dataFilePath, "r");
             FileChannel channel = file.getChannel()) {
            long fileSize = file.length();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            this.fullData = new byte[(int) fileSize];
            buffer.get(this.fullData);
        }
    }


}
