package com.camsys.datafeedmanager.repository;

import com.camsys.datafeedmanager.model.entities.RealtimeDataInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealtimeDataInfoRespository extends JpaRepository<RealtimeDataInfo, Long> {
}
