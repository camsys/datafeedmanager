package com.camsys.datafeedmanager.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RealtimeDataInfoDto {

    private Long id;

    private String name;

    private String description;

    private String feedURI;

    private String feedType;
}
