package com.camsys.datafeedmanager.repository;

import com.camsys.datafeedmanager.model.entities.TransitDataInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransitDataInfoRespository extends JpaRepository<TransitDataInfo, Long> {
}
