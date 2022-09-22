package com.camsys.datafeedmanager.model.entities;

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
public class TransitDataInfo {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String sourceURI;

    @Column
    private String targetName;

    @Column
    private Boolean merge;

    @ManyToOne
    @JoinColumn(name="fk_FeedInfoId")
    private FeedInfo feedInfo;

}
