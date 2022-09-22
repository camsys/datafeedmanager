package com.camsys.datafeedmanager.model.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class FeedInfo {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String agency;

    @Column
    private String feedName;

    @Column
    private String serviceName;

    @Column
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name="fk_FeedConfigurationId")
    private FeedConfiguration feedConfiguration;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "feedInfo", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<TransitDataInfo> transitDataInfo;

    @OneToMany(cascade=CascadeType.ALL, mappedBy = "feedInfo", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<RealtimeDataInfo> realtimeDataInfo;

    /*@PreRemove
    private void removeFeedInfoFromConfiguration() {
        if(this.feedConfiguration != null){
            feedConfiguration.getFeedInfo().remove(this);
            this.feedConfiguration = null;
        }
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedInfo feedInfo = (FeedInfo) o;
        return Objects.equals(id, feedInfo.id) && Objects.equals(agency, feedInfo.agency) && Objects.equals(feedName, feedInfo.feedName) && Objects.equals(serviceName, feedInfo.serviceName) && Objects.equals(enabled, feedInfo.enabled) && Objects.equals(feedConfiguration, feedInfo.feedConfiguration) && Objects.equals(transitDataInfo, feedInfo.transitDataInfo) && Objects.equals(realtimeDataInfo, feedInfo.realtimeDataInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, agency, feedName, serviceName, enabled, feedConfiguration, transitDataInfo, realtimeDataInfo);
    }
}
