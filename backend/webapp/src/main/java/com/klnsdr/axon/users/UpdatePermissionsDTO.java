package com.klnsdr.axon.users;

import lombok.Getter;

import java.util.List;

@Getter
public class UpdatePermissionsDTO {
    private List<Long> permissionIds;
}
