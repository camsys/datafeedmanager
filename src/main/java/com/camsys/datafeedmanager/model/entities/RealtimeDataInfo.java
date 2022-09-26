package com.camsys.datafeedmanager.model.entities;

import com.camsys.datafeedmanager.model.RealtimeFeedType;
import javax.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "UniqueRealtimeNameAndFeedInfo", columnNames = { "name", "fk_FeedInfoId" })
})
public class RealtimeDataInfo {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String feedURI;

    @Column
    private RealtimeFeedType feedType;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name="fk_FeedInfoId")
    private FeedInfo feedInfo;
}
