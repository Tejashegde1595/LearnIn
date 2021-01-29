package com.example.portfolio.service.Business.feed;

import com.example.portfolio.service.Dao.UserDao;
import com.example.portfolio.service.Dao.feed.PostsDao;
import com.example.portfolio.service.Entity.UserAuthTokenEntity;
import com.example.portfolio.service.Entity.feed.PostEntity;
import com.example.portfolio.service.exception.AuthenticationFailedException;
import com.example.portfolio.service.exception.AuthorizationFailedException;
import com.example.portfolio.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

import static com.example.portfolio.service.common.GenericErrorCode.*;
import static com.example.portfolio.service.common.GenericErrorCode.ATHR_003_COMMON;

@Service
public class LikeService {
    @Autowired
    private PostsDao postsDao;

    @Autowired
    private UserDao userDao;

    @Transactional
    public PostEntity likePost(final String authorizationToken, final String postId) throws AuthenticationFailedException, ObjectNotFoundException {
        UserAuthTokenEntity userAuthToken = getUserAuthToken(authorizationToken);
        PostEntity postEntity = postsDao.getPostById(postId);
        if(postEntity==null){
            throw new ObjectNotFoundException(POS_001.getDefaultMessage(),POS_001.getCode());
        }
        postEntity.setLikes(postEntity.getLikes()+1);
        return postsDao.editPost(postEntity);
    }
    private UserAuthTokenEntity getUserAuthToken(final String authorizationToken) throws AuthenticationFailedException{
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);
        if (userAuthTokenEntity == null) {
            throw new AuthenticationFailedException(SGOR_001.getCode(), SGOR_001.getDefaultMessage());
        }
        if (userAuthTokenEntity.getLogoutAt() != null || userAuthTokenEntity.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthenticationFailedException(SGOR_001.getCode(), SGOR_001.getDefaultMessage());
        }
        return userAuthTokenEntity;
    }
}
