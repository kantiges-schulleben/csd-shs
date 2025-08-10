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

    public Student toStudent() {
        final Student student = new Student();
        student.setName(this.getName());
        student.setSureName(this.getSureName());
        student.setMail(this.getMail());
        student.setSubject(this.getSubject());
        student.setGrade(this.getGrade());
        student.setGroup(this.isGroup());
        student.setPhoneNumber(this.getPhoneNumber());
        return student;
    }
}
