package com.example.portfolio.service.Entity.feed;

import com.example.portfolio.service.Entity.UserEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@NamedQueries(
        {
                @NamedQuery(name = "getAllPosts", query = "select p from PostEntity p"),
                @NamedQuery(name = "postByUuid", query = "select p from PostEntity p where p.uuid = :uuid"),
                @NamedQuery(name = "postByUserId", query = "select p from PostEntity p where p.user.uuid = :uuid")
        }
)
public class LikeEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    public PostEntity getPostEntity() {
        return postEntity;
    }

    public void setPostEntity(PostEntity postEntity) {
        this.postEntity = postEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Column(name = "UUID")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @NotNull
    private PostEntity postEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private UserEntity userEntity;
}
