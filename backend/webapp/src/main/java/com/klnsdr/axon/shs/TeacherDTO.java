package com.klnsdr.axon.shs;

import com.klnsdr.axon.shs.entity.Teacher;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherDTO {
    private String name;
    private String sureName;
    private String mail;
    private String subject;
    private int grade;
    private boolean isGroup;
    private String phoneNumber;
    private int targetGrade;

    public Teacher toTeacher() {
        final Teacher teacher = new Teacher();
        teacher.setName(this.getName());
        teacher.setSureName(this.getSureName());
        teacher.setMail(this.getMail());
        teacher.setSubject(this.getSubject());
        teacher.setGrade(this.getGrade());
        teacher.setGroup(this.isGroup());
        teacher.setPhoneNumber(this.getPhoneNumber());
        teacher.setTargetGrade(this.getTargetGrade());
        return teacher;
    }
}
