package com.camsys.datafeedmanager.repository;

import com.camsys.datafeedmanager.model.entities.FeedInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedInfoRespository extends JpaRepository<FeedInfo, Long> {
}
