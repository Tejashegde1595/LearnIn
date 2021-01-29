package com.example.portfolio.service.Business.feed;

import com.example.portfolio.service.Dao.feed.PostsDao;
import com.example.portfolio.service.Dao.UserDao;
import com.example.portfolio.service.Entity.feed.PostEntity;
import com.example.portfolio.service.Entity.UserAuthTokenEntity;
import com.example.portfolio.service.Entity.UserEntity;
import com.example.portfolio.service.exception.AuthenticationFailedException;
import com.example.portfolio.service.exception.AuthorizationFailedException;
import com.example.portfolio.service.exception.ObjectNotFoundException;
import com.example.portfolio.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static com.example.portfolio.service.common.GenericErrorCode.*;

@Service
public class PostService {

    @Autowired
    private PostsDao postsDao;

    @Autowired
    private UserDao userDao;

    @Transactional
    public PostEntity createPost(final PostEntity postEntity,final String authorizationToken) throws AuthenticationFailedException{
        UserAuthTokenEntity userAuthToken = getUserAuthToken(authorizationToken);
        postEntity.setUser(userAuthToken.getUser());
        return postsDao.createPost(postEntity);
    }

    public List<PostEntity> getAllPosts(final String authorizationToken) throws AuthenticationFailedException{
        UserAuthTokenEntity userAuthToken = getUserAuthToken(authorizationToken);
        return postsDao.getAllPosts();
    }

    public List<PostEntity> getAllPostsByUser(final String authorizationToken,final String userId) throws AuthenticationFailedException,UserNotFoundException{
        UserAuthTokenEntity userAuthToken = getUserAuthToken(authorizationToken);

        UserEntity userEntity = userDao.getUser(userId);
        if(userEntity==null){
            throw new UserNotFoundException(USR_001_COMMON.getDefaultMessage(),USR_001_COMMON.getCode());
        }
        return postsDao.getAllPostsByUserId(userId);
    }

    @Transactional
    public PostEntity deletePost(final String authorizationToken,final String postId) throws AuthenticationFailedException,ObjectNotFoundException,AuthorizationFailedException{
        UserAuthTokenEntity userAuthToken = getUserAuthToken(authorizationToken);

        PostEntity postEntity = postsDao.getPostById(postId);
        if(postEntity==null){
            throw new ObjectNotFoundException(POS_001.getDefaultMessage(),POS_001.getCode());
        }
        if(postEntity.getUser()!=userAuthToken.getUser() && !userAuthToken.getUser().getRole().equals("admin"))
        {
            throw new AuthorizationFailedException(ATHR_003_COMMON.getDefaultMessage(),ATHR_003_COMMON.getCode());
        }
        return postsDao.deletePost(postEntity);
    }

    @Transactional
    public PostEntity editPost(final String authorizationToken,final String postId,final PostEntity editPostEntity) throws AuthenticationFailedException,ObjectNotFoundException,AuthorizationFailedException{
        UserAuthTokenEntity userAuthToken = getUserAuthToken(authorizationToken);

        PostEntity postEntity = postsDao.getPostById(postId);
        if(postEntity==null){
            throw new ObjectNotFoundException(POS_001.getDefaultMessage(),POS_001.getCode());
        }
        if(postEntity.getUser()!=userAuthToken.getUser())
        {
            throw new AuthorizationFailedException(ATHR_003_COMMON.getDefaultMessage(),ATHR_003_COMMON.getCode());
        }
        postEntity.setContent(editPostEntity.getContent());
        return postsDao.editPost(postEntity);
    }


    private UserAuthTokenEntity getUserAuthToken(final String authorizationToken) throws
            AuthenticationFailedException{
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
