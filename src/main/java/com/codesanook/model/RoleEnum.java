package com.codesanook.model;

public enum RoleEnum {

    ADMIN(1),
    AUTHOR(2);

    private final int id;

    RoleEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static RoleEnum fromId(int id) {
        for (RoleEnum type : RoleEnum.values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        throw new IllegalStateException(
                String.format("no %s with id %d", RoleEnum.class.getSimpleName(), id));
    }

}
