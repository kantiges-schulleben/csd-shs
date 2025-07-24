package com.klnsdr.axon.shs.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGroupDTO {
    private Long teacherId;
    private Long studentId;
    private String subject;
}
