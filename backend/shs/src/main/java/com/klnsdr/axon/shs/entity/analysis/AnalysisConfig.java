package com.klnsdr.axon.shs.entity.analysis;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "analysis_config")
public class AnalysisConfig {
    @Id
    @Column(name = "id")
    private String key;

    @Column(name = "value")
    private String value;
}
