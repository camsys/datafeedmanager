package com.camsys.datafeedmanager.model.entities;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class FeedConfiguration {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String feedConfigName;

    @Column
    private String targetDirectory;

    @Column
    private String backupDirectory;

    @OneToMany(cascade=CascadeType.PERSIST, orphanRemoval = true, mappedBy = "feedConfiguration", fetch = FetchType.EAGER)
    private List<FeedInfo> feedInfo = new ArrayList<>();

    /*@PreRemove
    private void removeFeedInfoFromConfiguration() {
        feedInfo.getFeedInfo().remove(this);
    }*/

    public void addFeedInfo(FeedInfo feedInfo){
        this.feedInfo.add(feedInfo);
    }

    public void addFeedInfo(List<FeedInfo> feedInfo){
        this.feedInfo.addAll(feedInfo);
    }

    public void removeFeedInfo(FeedInfo feedInfo){
        this.feedInfo.remove(feedInfo);
    }

    public void removeFeedInfo(List<FeedInfo> feedInfo){
        this.feedInfo.removeAll(feedInfo);
    }
}
