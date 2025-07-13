package com.klnsdr.axon.shs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "enrolled_students")
public class EnrolledStudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "sure_name")
    private String sureName;

    @Column(name = "is_teacher")
    private boolean isTeacher;

    @Column(name = "mail")
    private String mail;

    @Column(name = "target_grade")
    private int targetGrade;

    @Column(name = "subject")
    private String subject;

    @Column(name = "grade")
    private int grade;

    @Column(name = "is_group")
    private boolean isGroup;

    @Column(name = "phone_number")
    private String phoneNumber;
}
