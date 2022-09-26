package com.camsys.datafeedmanager.model.entities;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "UniqueFeedNameAndConfig", columnNames = { "feedName", "fk_FeedConfigurationId" })
})
public class FeedInfo {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column
    private String agency;

    @Column(nullable = false)
    private String feedName;

    @Column
    private String serviceName;

    @Column(columnDefinition = "boolean default true")
    private Boolean enabled;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name="fk_FeedConfigurationId")
    private FeedConfiguration feedConfiguration;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true, mappedBy = "feedInfo", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<TransitDataInfo> transitDataInfo = new ArrayList<>();

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true, mappedBy = "feedInfo", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<RealtimeDataInfo> realtimeDataInfo = new ArrayList<>();

}
