package com.klnsdr.axon.shs.service.legacy;

import com.klnsdr.axon.shs.entity.analysis.legacy.Group;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void save(Group group) {
        groupRepository.save(group);
    }

    @Transactional
    public void deleteAllGroupsAndStudents() {
        final List<Group> groups = groupRepository.findAll();
        for (Group group : groups) {
            group.setStudents(null);
        }
        groupRepository.saveAll(groups);
        groupRepository.deleteAll();
    }

    public List<Group> getSinglePairs() {
        return groupRepository.getSinglePairs();
    }

    public List<Group> getGroupPairs() {
        return groupRepository.getGroupPairs();
    }

    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }
}
