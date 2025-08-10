package com.klnsdr.axon.users;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdatePermissionsDTO {
    private List<Long> permissionIds;
}
