package com.cobra.authenticationserver.dto;

public enum RoleName {

    ADMIN, USER, SUPER_ADMIN;

    private static final String PREFIX = "ROLE_";

    public String getRoleName() {
        return PREFIX + this.name();
    }
}
