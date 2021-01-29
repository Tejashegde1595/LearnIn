package com.example.portfolio.service.Dao.feed;

import com.example.portfolio.service.Entity.feed.PostEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PostsDao {
    @PersistenceContext
    private EntityManager entityManager;

    public PostEntity createPost(PostEntity postEntity){
        entityManager.persist(postEntity);
        return postEntity;
    }

    public List<PostEntity> getAllPosts(){
        return entityManager.createNamedQuery("getAllPosts",PostEntity.class).getResultList();
    }

    public List<PostEntity> getAllPostsByUserId(final String uuid){
        return entityManager.createNamedQuery("postByUserId",PostEntity.class).setParameter("uuid",uuid).getResultList();
    }

    public PostEntity deletePost(final PostEntity postEntity){
        entityManager.remove(postEntity);
        return postEntity;
    }

    public PostEntity getPostById(final String postId){
        try {
            return entityManager.createNamedQuery("postByUuid", PostEntity.class).setParameter("uuid", postId).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public PostEntity editPost(PostEntity postEntity){
        entityManager.merge(postEntity);
        return postEntity;
    }
}
