package com.codesanook.model;

public enum PostSubtypeEnum {

    ARTICLE(1), TIP(1), VIDEO(2);

    private final int id;

    PostSubtypeEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static PostSubtypeEnum fromId(int id) {
        for (PostSubtypeEnum type : PostSubtypeEnum.values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        throw new IllegalStateException(
                String.format("no %s with id %d", PostSubtypeEnum.class.getSimpleName(), id));
    }

}
