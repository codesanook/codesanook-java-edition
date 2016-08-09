package com.codesanook.query;

/**
 * Created by SciMeta on 3/14/2016.
 */
public class TagPostCount {

    public static final String mappingName = "TagPostCount";

    private int id;
    private String name;
    private long postCount;
    


    public TagPostCount(int id, String name, long postCount) {
        this.id = id;
        this.name = name;
        this.postCount = postCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPostCount() {
        return postCount;
    }

    public void setPostCount(long postCount) {
        this.postCount = postCount;
    }
}
