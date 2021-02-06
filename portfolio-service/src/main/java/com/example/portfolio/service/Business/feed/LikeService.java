package com.example.portfolio.service.Business.feed;

import com.example.portfolio.service.Dao.UserDao;
import com.example.portfolio.service.Dao.feed.LikesDao;
import com.example.portfolio.service.Dao.feed.PostsDao;
import com.example.portfolio.service.Entity.UserAuthTokenEntity;
import com.example.portfolio.service.Entity.feed.LikeEntity;
import com.example.portfolio.service.Entity.feed.PostEntity;
import com.example.portfolio.service.common.UserAuth;
import com.example.portfolio.service.exception.AuthenticationFailedException;
import com.example.portfolio.service.exception.AuthorizationFailedException;
import com.example.portfolio.service.exception.ObjectNotFoundException;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.example.portfolio.service.common.GenericErrorCode.*;
import static com.example.portfolio.service.common.GenericErrorCode.ATHR_003_COMMON;

@Service
public class LikeService {
    @Autowired
    private PostsDao postsDao;

    @Autowired
    private LikesDao likesDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserAuth userAuth;

    @Transactional
    public PostEntity likePost(final String authorizationToken, final String postId) throws AuthenticationFailedException, ObjectNotFoundException {
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        PostEntity postEntity = postsDao.getPostById(postId);
        if(postEntity==null){
            throw new ObjectNotFoundException(POS_001.getDefaultMessage(),POS_001.getCode());
        }
        LikeEntity likeEntity = getAndModifyLikeEntity(postEntity,userAuthToken);
        if(likeEntity.getLiked()) {
            postEntity.setLikes(postEntity.getLikes() + 1);
        }
        else {
            postEntity.setLikes(postEntity.getLikes() - 1);
        }
        return postsDao.editPost(postEntity);
    }

    private LikeEntity getAndModifyLikeEntity(PostEntity postEntity,UserAuthTokenEntity userAuthTokenEntity){
        LikeEntity likeEntity = likesDao.getLike(postEntity.getUuid(),userAuthTokenEntity.getUser().getUuid());
        if(likeEntity!=null){
            if(!likeEntity.getLiked()){
                likeEntity.setLiked(true);
            }else{
                likeEntity.setLiked(false);
            }
            return likesDao.modifyLike(likeEntity);
        }else {
            likeEntity = new LikeEntity();
            likeEntity.setUuid(UUID.randomUUID().toString());
            likeEntity.setUserEntity(userAuthTokenEntity.getUser());
            likeEntity.setPostEntity(postEntity);
            likeEntity.setLiked(true);
            return likesDao.addLike(likeEntity);
        }
    }

}
