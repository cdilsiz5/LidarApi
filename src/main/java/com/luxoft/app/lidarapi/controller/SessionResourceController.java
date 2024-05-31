package com.luxoft.app.lidarapi.controller;

import com.luxoft.app.lidarapi.dto.MetadataDto;
import com.luxoft.app.lidarapi.dto.SessionDto;
import com.luxoft.app.lidarapi.model.Session;
import com.luxoft.app.lidarapi.request.LidarDataRequest;
import com.luxoft.app.lidarapi.request.SessionLidarDataRequest;
import com.luxoft.app.lidarapi.service.DataResourceService;
import com.luxoft.app.lidarapi.service.SessionResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.luxoft.app.lidarapi.constants.Constant.*;

@RestController
@RequestMapping(API_PREFIX + API_VERSION_V2 + API_LUXOFT)
@RequiredArgsConstructor
@Tag(name = "Data Resource", description = "Lidar data management APIs")
public class SessionResourceController {


    private final SessionResourceService sessionResourceService;

    @Operation(summary = "Get Metadata",
            description = "Returns metadata information of Lidar data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved metadata",
                            content = @Content(schema = @Schema(implementation = MetadataDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters")
            })
    @GetMapping("/metadata/{sessionId}")
    public MetadataDto getMetadata(@PathVariable("sessionId") String sessionId ) throws IOException {
        return sessionResourceService.getMetadataDto(sessionId);
    }
    @Operation(summary = "Get Binary Data",
            description = "Returns binary data based on group IDs",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved binary data",
                            content = @Content(schema = @Schema(implementation = byte[].class)))
            })
    @GetMapping("/data/{sessionId}")
    public byte [] getBinaryData(@PathVariable("sessionId") String sessionId,@RequestParam int startGroupId, @RequestParam int endGroupId) throws IOException {
        SessionLidarDataRequest request = SessionLidarDataRequest.builder()
                .startGroupId(startGroupId)
                .endGroupId(endGroupId)
                .sessionId(sessionId)
                .build();
        return sessionResourceService.getBinaryFile(request);
    }

    @Operation(summary = "Get Sessions List ",
            description = "Returns Sessions List",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved Sessions List",
                            content = @Content(schema = @Schema(implementation = List.class)))
            })
    @GetMapping("/sessions")
    public List<SessionDto> getSessions() {
        return sessionResourceService.getSessionList();
    }

}

