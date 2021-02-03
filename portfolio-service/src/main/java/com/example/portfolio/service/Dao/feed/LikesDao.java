package com.example.portfolio.service.Dao.feed;

import com.example.portfolio.service.Entity.feed.LikeEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class LikesDao {

    @PersistenceContext
    private EntityManager entityManager;

    public LikeEntity addLike(LikeEntity likeEntity){
        entityManager.persist(likeEntity);
        return likeEntity;
    }

    public LikeEntity modifyLike(LikeEntity likeEntity){
        entityManager.merge(likeEntity);
        return likeEntity;
    }

    public LikeEntity getLike(final String postId,final String userId){
        try{
            return  entityManager.createNamedQuery("likeByUserPostId",LikeEntity.class).setParameter("uuid",userId).setParameter("pid",postId).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }
}
