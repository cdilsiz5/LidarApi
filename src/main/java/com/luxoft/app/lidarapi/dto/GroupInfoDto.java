package com.luxoft.app.lidarapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfoDto {
    private int groupId;
    private long points;
    private long offset;
    private long size;
}
