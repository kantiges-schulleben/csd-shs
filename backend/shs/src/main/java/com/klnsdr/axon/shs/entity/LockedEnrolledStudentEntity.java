package com.klnsdr.axon.shs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "locked_enrolled_students")
public class LockedEnrolledStudentEntity {
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

    private static final String QUOTE = "\"";
    private static final String COMMA = ",";
    private static final String COLON = ":";

    public String asJson() {
        final StringBuilder builder = new StringBuilder();
        builder.append("{");
        setString(builder, "name", name  + " " + sureName);
        builder.append(COMMA);
        setString(builder, "mail", mail);
        builder.append(COMMA);
        setString(builder, "nachhilfe", isTeacher ? "1" : "0");
        builder.append(COMMA);
        setString(builder, "klasse", Integer.toString(grade) + "x");
        builder.append(COMMA);
        setString(builder, "facher", subject);
        builder.append(COMMA);
        setString(builder, "zielKlasse", Integer.toString(targetGrade));
        builder.append(COMMA);
        setString(builder, "einzelnachhilfe", isGroup ? "1" : "0"); // *chuckles* i'm in danger
        builder.append(COMMA);
        setString(builder, "zeit", "8191"); // set to 10 digits all 1 in binary, so all time slots are available
        builder.append(COMMA);
        setString(builder, "telefon", phoneNumber);
        builder.append(COMMA);
        setString(builder, "Bemerkung", "");
        builder.append(COMMA);
        setString(builder, "accountID", Long.toString(id));
        builder.append("}");

        return builder.toString();
    }

    private void setString(StringBuilder builder, String key, String value) {
        builder
                .append(QUOTE)
                .append(key)
                .append(QUOTE)
                .append(COLON)
                .append(QUOTE)
                .append(value)
                .append(QUOTE);
    }
}
