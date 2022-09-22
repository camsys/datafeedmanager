package com.camsys.datafeedmanager.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedConfigurationDto {

    private Long id;

    private String feedConfigName;

    private String targetDirectory;

    private String backupDirectory;

}
