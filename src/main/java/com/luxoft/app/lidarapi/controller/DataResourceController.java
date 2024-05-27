package com.luxoft.app.lidarapi.controller;

import com.luxoft.app.lidarapi.dto.MetadataDto;
import com.luxoft.app.lidarapi.request.LidarDataRequest;
import com.luxoft.app.lidarapi.service.DataResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.luxoft.app.lidarapi.constants.Constant.*;


@RestController
@RequestMapping(API_PREFIX + API_VERSION_V1 + API_LUXOFT)
@RequiredArgsConstructor
@Tag(name = "Data Resource", description = "Lidar data management APIs")
public class DataResourceController {

    private final DataResourceService dataResourceService;

    @Operation(summary = "Get Metadata",
            description = "Returns metadata information of Lidar data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved metadata",
                            content = @Content(schema = @Schema(implementation = MetadataDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters")
            })
    @GetMapping("/metadata")
    public MetadataDto getMetadata() throws IOException {
        return dataResourceService.getMetadataDto();
    }

}
