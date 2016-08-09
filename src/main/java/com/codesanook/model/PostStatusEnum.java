package com.codesanook.model;

public enum PostStatusEnum {

    PUBLISHED(1),
    UNPUBLISHED(2),
    DELETED(3),
    SCHEDULED(4);

    private final int id;

    PostStatusEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static PostStatusEnum fromId(int id) {
        for (PostStatusEnum type : PostStatusEnum.values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        throw new IllegalStateException(
                String.format("no %s with id %d", PostStatusEnum.class.getSimpleName(), id));
    }

}
