package com.luxoft.app.lidarapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Metadata {
    private String dataSource;
    private List<GroupInfo> groups;
}
