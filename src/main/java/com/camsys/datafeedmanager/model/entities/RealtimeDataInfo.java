package com.camsys.datafeedmanager.model.entities;

import com.camsys.datafeedmanager.model.RealtimeFeedType;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class RealtimeDataInfo {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String feedURI;

    @Column
    private RealtimeFeedType feedType;

    @ManyToOne
    @JoinColumn(name="fk_FeedInfoId")
    private FeedInfo feedInfo;
}
