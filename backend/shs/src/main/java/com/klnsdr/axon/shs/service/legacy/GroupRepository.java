package com.klnsdr.axon.shs.service.legacy;

import com.klnsdr.axon.shs.entity.analysis.legacy.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
