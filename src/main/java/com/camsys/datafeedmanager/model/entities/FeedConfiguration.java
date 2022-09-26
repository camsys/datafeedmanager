package com.camsys.datafeedmanager.model.entities;

import javax.persistence.*;

import lombok.*;

import java.util.*;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class FeedConfiguration {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(unique=true)
    private String feedConfigName;

    @Column
    private String targetDirectory;

    @Column
    private String backupDirectory;

    @OneToMany(cascade=CascadeType.PERSIST, orphanRemoval = true, mappedBy = "feedConfiguration", fetch = FetchType.EAGER)
    private List<FeedInfo> feedInfo = new ArrayList<>();

    public void addFeedInfo(FeedInfo feedInfo){
        this.feedInfo.add(feedInfo);
    }

    public void addFeedInfo(Set<FeedInfo> feedInfo){
        this.feedInfo.addAll(feedInfo);
    }

    public void removeFeedInfo(FeedInfo feedInfo){
        this.feedInfo.remove(feedInfo);
    }

    public void removeFeedInfo(Set<FeedInfo> feedInfo){
        this.feedInfo.removeAll(feedInfo);
    }
}
