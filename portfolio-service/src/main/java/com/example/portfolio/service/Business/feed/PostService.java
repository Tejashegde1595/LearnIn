package com.example.portfolio.service.Business.feed;

import com.example.portfolio.service.Dao.feed.PostsDao;
import com.example.portfolio.service.Dao.UserDao;
import com.example.portfolio.service.Entity.feed.LikeEntity;
import com.example.portfolio.service.Entity.feed.PostEntity;
import com.example.portfolio.service.Entity.UserAuthTokenEntity;
import com.example.portfolio.service.Entity.UserEntity;
import com.example.portfolio.service.common.FileUploadUtil;
import com.example.portfolio.service.common.UserAuth;
import com.example.portfolio.service.exception.AuthenticationFailedException;
import com.example.portfolio.service.exception.AuthorizationFailedException;
import com.example.portfolio.service.exception.ObjectNotFoundException;
import com.example.portfolio.service.exception.UserNotFoundException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;

import static com.example.portfolio.service.common.Constants.POST_IMAGE_LOCATION;
import static com.example.portfolio.service.common.GenericErrorCode.*;

@Service
public class PostService {

    @Autowired
    private PostsDao postsDao;

    @Autowired
    private UserDao userDao;


    @Autowired
    private UserAuth userAuth;

    @Transactional
    public PostEntity createPost(final PostEntity postEntity, final String authorizationToken, final MultipartFile multipartFile) throws AuthenticationFailedException, IOException {
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        postEntity.setUser(userAuthToken.getUser());
        String fileName = postEntity.getUuid();
        FileUploadUtil.saveFile(POST_IMAGE_LOCATION, fileName, multipartFile);
        return postsDao.createPost(postEntity);
    }

    public List<PostEntity> getAllPosts(final String authorizationToken) throws AuthenticationFailedException,IOException{
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        List<PostEntity> postEntityList = postsDao.getAllPosts();
        postEntityList = addImageToPost(postEntityList);
        return postEntityList;
    }

    public List<PostEntity> getAllPostsByUser(final String authorizationToken,final String userId) throws AuthenticationFailedException,UserNotFoundException,IOException{
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);

        UserEntity userEntity = userDao.getUser(userId);
        if(userEntity==null){
            throw new UserNotFoundException(USR_001_COMMON.getDefaultMessage(),USR_001_COMMON.getCode());
        }
        List<PostEntity> postEntityList = postsDao.getAllPostsByUserId(userId);
        postEntityList = addImageToPost(postEntityList);
        return postEntityList;
    }

    @Transactional
    public PostEntity deletePost(final String authorizationToken,final String postId) throws AuthenticationFailedException,ObjectNotFoundException,AuthorizationFailedException,IOException{
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);

        PostEntity postEntity = postsDao.getPostById(postId);
        if(postEntity==null){
            throw new ObjectNotFoundException(POS_001.getDefaultMessage(),POS_001.getCode());
        }
        if(postEntity.getUser()!=userAuthToken.getUser() && !userAuthToken.getUser().getRole().equals("admin"))
        {
            throw new AuthorizationFailedException(ATHR_003_COMMON.getDefaultMessage(),ATHR_003_COMMON.getCode());
        }
        FileUploadUtil.deleteFile(POST_IMAGE_LOCATION,postEntity.getUuid());
        return postsDao.deletePost(postEntity);
    }

    @Transactional
    public PostEntity editPost(final String authorizationToken,final String postId,final PostEntity editPostEntity,final MultipartFile multipartFile) throws AuthenticationFailedException,ObjectNotFoundException,AuthorizationFailedException,IOException{
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);

        PostEntity postEntity = postsDao.getPostById(postId);
        if(postEntity==null){
            throw new ObjectNotFoundException(POS_001.getDefaultMessage(),POS_001.getCode());
        }
        if(postEntity.getUser()!=userAuthToken.getUser())
        {
            throw new AuthorizationFailedException(ATHR_003_COMMON.getDefaultMessage(),ATHR_003_COMMON.getCode());
        }
        postEntity.setContent(editPostEntity.getContent());
        String fileName = postEntity.getUuid();
        FileUploadUtil.UpdateFile(POST_IMAGE_LOCATION, fileName, multipartFile);
        return postsDao.editPost(postEntity);
    }

    public List<LikeEntity> getLikesForPosts(final String authorizationToken, final String postId) throws AuthenticationFailedException,ObjectNotFoundException{
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);

        PostEntity postEntity = postsDao.getPostById(postId);
        if(postEntity==null){
            throw new ObjectNotFoundException(POS_001.getDefaultMessage(),POS_001.getCode());
        }
        return postEntity.getLikesInfo();

    }


    private List<PostEntity> addImageToPost(List<PostEntity> postEntities) throws IOException{
        Iterator<PostEntity> iterator = postEntities.iterator();
        while(iterator.hasNext()) {
            PostEntity postEntity = iterator.next();
            FileInputStream fs = new FileInputStream(POST_IMAGE_LOCATION+"/"+postEntity.getUuid());
            postEntity.setImage(IOUtils.toByteArray(fs));
        }
        return postEntities;
    }
}
