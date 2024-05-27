package com.luxoft.app.lidarapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupInfo {
    private int group_id;
    private long points;
    private long offset;
    private long size;
}
