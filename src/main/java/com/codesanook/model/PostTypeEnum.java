package com.codesanook.model;

public  enum PostTypeEnum {

    QUESTION(1), KNOWLEDGE(2);

    private final int id;
    PostTypeEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static PostTypeEnum fromId(int id) {
        for (PostTypeEnum type : PostTypeEnum.values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        throw new IllegalStateException(
                String.format("no %s with id %d", PostTypeEnum.class.getSimpleName(), id));
    }

}
