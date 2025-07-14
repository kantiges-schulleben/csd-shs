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

    public static Teacher map(TeacherDTO teacherDTO) {
        final Teacher teacher = new Teacher();
        teacher.setName(teacherDTO.getName());
        teacher.setSureName(teacherDTO.getSureName());
        teacher.setMail(teacherDTO.getMail());
        teacher.setSubject(teacherDTO.getSubject());
        teacher.setGrade(teacherDTO.getGrade());
        teacher.setGroup(teacherDTO.isGroup());
        teacher.setPhoneNumber(teacherDTO.getPhoneNumber());
        teacher.setTargetGrade(teacherDTO.getTargetGrade());
        return teacher;
    }
}
