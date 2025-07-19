package com.klnsdr.axon.shs.entity.analysis.legacy;

import com.klnsdr.axon.shs.entity.LockedEnrolledStudentEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "analysis_groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", referencedColumnName = "user_id")
    private LockedEnrolledStudentEntity teacher;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "group_students",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<LockedEnrolledStudentEntity> students;

    @Column(name = "subject")
    private String subject;

    @Column(name = "is_released")
    private boolean isReleased;
}
