package com.luxoft.app.lidarapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataDto {
    private int groups;
    private int startGroupId;
    private long maxGroupSize;
}
