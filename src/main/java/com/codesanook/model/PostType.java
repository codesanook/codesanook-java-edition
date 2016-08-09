package com.codesanook.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SciMeta on 12/3/2015.
 */
@Entity
@Table(name = "post_types")
public class PostType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(mappedBy = "postType")
    private List<Post> posts;

    public PostType() {
        posts = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

}
