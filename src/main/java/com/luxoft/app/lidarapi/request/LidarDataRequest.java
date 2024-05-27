package com.luxoft.app.lidarapi.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LidarDataRequest {
    @Min(1)
    private int startGroupId;

    @Min(1)
    private int endGroupId;


}
