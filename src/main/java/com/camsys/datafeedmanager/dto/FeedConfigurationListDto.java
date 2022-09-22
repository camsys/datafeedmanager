package com.camsys.datafeedmanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeedConfigurationDto {

    private Long id;

    private String feedConfigName;

    private String targetDirectory;

    private String backupDirectory;

    private List<FeedInfoDto> feedInfo;
}
