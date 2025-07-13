package com.klnsdr.axon.shs;

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
}
