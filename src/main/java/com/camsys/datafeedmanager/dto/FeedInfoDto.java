package com.camsys.datafeedmanager.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FeedInfoDto {

    private Long id;

    private String agency;

    private String feedName;

    private String serviceName;

    private Boolean enabled;

    private Long feedConfigurationId;

    private List<TransitDataInfoDto> transitDataInfo;

    private List<RealtimeDataInfoDto> realtimeDataInfo;
}
