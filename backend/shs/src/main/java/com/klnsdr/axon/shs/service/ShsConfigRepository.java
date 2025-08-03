package com.klnsdr.axon.shs.service;

import com.klnsdr.axon.shs.entity.ShsConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShsConfigRepository extends JpaRepository<ShsConfigEntity, String> {
}
