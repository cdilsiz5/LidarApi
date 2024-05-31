package com.luxoft.app.lidarapi.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SessionLidarDataRequest {
    @Min(0)
    private int startGroupId;

    @Min(0)
    private int endGroupId;

    @NotNull(message = "Session Id Cannot Be Null")
    private String sessionId;


}
