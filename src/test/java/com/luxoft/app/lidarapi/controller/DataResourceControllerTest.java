package com.luxoft.app.lidarapi.controller;

import com.luxoft.app.lidarapi.dto.MetadataDto;
import com.luxoft.app.lidarapi.service.DataResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DataResourceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DataResourceService dataResourceService;

    @InjectMocks
    private DataResourceController dataResourceController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dataResourceController).build();
    }

    @Test
    public void testGetMetadata() throws Exception {
        MetadataDto mockMetadataDto = new MetadataDto(5, 1, 1000);
        when(dataResourceService.getMetadataDto()).thenReturn(mockMetadataDto);

        mockMvc.perform(get("/api/v1/luxoft/metadata"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.groups").value(5))
                .andExpect(jsonPath("$.startGroupId").value(1))
                .andExpect(jsonPath("$.maxGroupSize").value(1000));
    }

    @Test
    public void testGetBinaryData() throws Exception {
        byte[] mockData = new byte[]{1, 2, 3, 4};
        when(dataResourceService.getBinaryFile(any())).thenReturn(mockData);

        mockMvc.perform(get("/api/v1/luxoft/data")
                        .param("startGroupId", "1")
                        .param("endGroupId", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(new byte[]{1, 2, 3, 4}));
    }
}
