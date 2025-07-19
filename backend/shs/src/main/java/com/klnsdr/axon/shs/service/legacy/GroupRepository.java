package com.klnsdr.axon.shs.service.legacy;

import com.klnsdr.axon.shs.entity.analysis.legacy.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("SELECT g FROM Group g WHERE SIZE(g.students) = 1")
    List<Group> getSinglePairs();

    @Query("SELECT g FROM Group g WHERE SIZE(g.students) > 1")
    List<Group> getGroupPairs();
}
