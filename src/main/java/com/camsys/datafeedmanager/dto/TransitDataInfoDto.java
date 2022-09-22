package com.camsys.datafeedmanager.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitDataInfoDto {

    private Long id;

    private String name;

    private String description;

    private String sourceURI;

    private String targetName;

    private Boolean merge;
}
