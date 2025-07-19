package com.klnsdr.axon.shs.service;

import com.klnsdr.axon.shs.entity.analysis.AnalysisConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisConfigRepository extends JpaRepository<AnalysisConfig, String> {
}
