package com.luxoft.app.lidarapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LidarPoint {
    private long x;
    private long y;
    private long z;
    private long intensity;
}
