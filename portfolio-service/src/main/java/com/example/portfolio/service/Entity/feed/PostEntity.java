package com.example.portfolio.service.Entity.feed;

import com.example.portfolio.service.Entity.UserEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.soap.Text;

@Entity
@Table(name = "POSTS")
@NamedQueries(
        {
                @NamedQuery(name = "getAllPosts", query = "select p from PostEntity p"),
                @NamedQuery(name = "postByUuid", query = "select p from PostEntity p where p.uuid = :uuid"),
                @NamedQuery(name = "postByUserId", query = "select p from PostEntity p where p.user.uuid = :uuid")
        }
)
public class PostEntity {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "CONTENT",columnDefinition = "TEXT")
    @NotNull
    private String content;

    @Column(name = "LIKES")
    private Integer likes;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private UserEntity user;


}
