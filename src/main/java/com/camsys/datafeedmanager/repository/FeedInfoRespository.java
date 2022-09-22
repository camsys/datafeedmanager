package com.camsys.datafeedmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedInfoRespository extends JpaRepository<FeedInfo, Long> {
}
