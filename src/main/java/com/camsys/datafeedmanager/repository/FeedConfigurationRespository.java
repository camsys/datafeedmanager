package com.camsys.datafeedmanager.repository;

import com.camsys.datafeedmanager.model.entities.FeedConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedConfigurationRespository extends JpaRepository<FeedConfiguration, Long> {
}
