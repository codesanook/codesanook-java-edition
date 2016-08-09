package com.codesanook.model;

import com.codesanook.query.TagPostCount;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.type.StandardBasicTypes;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@SqlResultSetMapping(name = TagPostCount.mappingName,
        classes = {
                @ConstructorResult(targetClass = TagPostCount.class,
                        columns = {
                                @ColumnResult(name = "id", type = int.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "postCount", type = long.class)
                        })
        })

@Entity
@Table(name = "tags")
@Access(value = AccessType.FIELD)
public class Tag {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Post> posts;

    public Tag() {
        posts = new ArrayList<>();
    }

    public void validateRequiredProperty() {
        if (StringUtils.isEmpty(name))
            throw new IllegalStateException("tag Name must have value");
    }

    public void rename(String newName) {
        if (StringUtils.isEmpty(newName))
            throw new IllegalStateException("newName is empty");

        if (!name.equals(newName)) name = newName;
    }

    public String getName() {
        return name;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
