package com.klnsdr.axon.shs;

import com.klnsdr.axon.shs.entity.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDTO {
    private String name;
    private String sureName;
    private String mail;
    private String subject;
    private int grade;
    private boolean isGroup;
    private String phoneNumber;

    public static Student map(StudentDTO studentDTO) {
        final Student student = new Student();
        student.setName(studentDTO.getName());
        student.setSureName(studentDTO.getSureName());
        student.setMail(studentDTO.getMail());
        student.setSubject(studentDTO.getSubject());
        student.setGrade(studentDTO.getGrade());
        student.setGroup(studentDTO.isGroup());
        student.setPhoneNumber(studentDTO.getPhoneNumber());
        return student;
    }
}
