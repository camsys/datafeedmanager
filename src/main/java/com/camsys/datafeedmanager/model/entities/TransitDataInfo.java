package com.camsys.datafeedmanager.model.entities;

import javax.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "UniqueTransitDataNameAndFeedInfo", columnNames = { "name", "fk_FeedInfoId" })
})
public class TransitDataInfo {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String sourceURI;

    @Column
    private String targetName;

    @Column
    private Boolean merge;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name="fk_FeedInfoId")
    private FeedInfo feedInfo;

}
