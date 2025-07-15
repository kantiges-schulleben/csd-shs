package com.klnsdr.axon.shs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "analysis_result")
public class AnalysisResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id = 1L;

    @Column(name = "was_successful")
    private boolean wasSuccessful;

    @Column(name = "error_message")
    private String errorMessage;
}
