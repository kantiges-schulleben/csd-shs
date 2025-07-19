package com.klnsdr.axon.shs.service;

import com.klnsdr.axon.shs.entity.analysis.AnalysisResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisResultRepository extends JpaRepository<AnalysisResultEntity, Long> {
}
