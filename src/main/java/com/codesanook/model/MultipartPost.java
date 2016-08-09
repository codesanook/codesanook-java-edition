package com.codesanook.model;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "multipart_posts")
public class MultipartPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,mappedBy = "multipartPost")
    private List<MultipartPostItem> multipartPostItems;

    //cascade
    //http://stackoverflow.com/questions/12755380/jpa-persisting-a-unidirectional-one-to-many-relationship-fails-with-eclipselin
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name =
                    "fk_multipart_post_user_id"))
    private User user;

    @Column(nullable = false)
    private DateTime utcCreateDate;
    private DateTime utcLastUpdate;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MultipartPostItem> getMultipartPostItems() {
        return this.multipartPostItems;
    }

    public void setMultipartPostItems(List<MultipartPostItem> multipartPostItems) {
        this.multipartPostItems = multipartPostItems;
    }

    public MultipartPost() {
        this.multipartPostItems = new ArrayList<>();
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DateTime getUtcCreateDate() {
        return utcCreateDate;
    }

    public void setUtcCreateDate(DateTime utcCreateDate) {
        this.utcCreateDate = utcCreateDate;
    }

    public DateTime getUtcLastUpdate() {
        return utcLastUpdate;
    }

    public void setUtcLastUpdate(DateTime utcLastUpdate) {
        this.utcLastUpdate = utcLastUpdate;
    }
}
