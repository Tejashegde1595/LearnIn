package com.example.portfolio.service.Business.education;

import com.example.portfolio.service.Dao.UserDao;
import com.example.portfolio.service.Dao.career.CareerDao;
import com.example.portfolio.service.Dao.education.BachelorDao;
import com.example.portfolio.service.Dao.education.CollegeDao;
import com.example.portfolio.service.Entity.UserAuthTokenEntity;
import com.example.portfolio.service.Entity.UserEntity;
import com.example.portfolio.service.Entity.education.BachelorsEntity;
import com.example.portfolio.service.Entity.education.CollegeEntity;
import com.example.portfolio.service.common.UserAuth;
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
import static com.example.portfolio.service.common.GenericErrorCode.ATHR_003_COMMON;

@Service
public class BachelorService {

    @Autowired
    private BachelorDao bachelorDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserAuth userAuth;

    @Transactional
    public BachelorsEntity createBachelors(final BachelorsEntity bachelorsEntity, final String authorizationToken) throws AuthenticationFailedException {
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        bachelorsEntity.setUser_id(userAuthToken.getUser());
        return bachelorDao.createBachelorsEntity(bachelorsEntity);
    }

    public List<BachelorsEntity> getAllBachelors(final String authorizationToken) throws AuthenticationFailedException {
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        return bachelorDao.getAllBachelors();
    }

    public List<BachelorsEntity> getAllBachelorsByUserId(final String authorizationToken,final String userId) throws AuthenticationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        UserEntity userEntity = userDao.getUser(userId);
        if(userEntity==null){
            throw new UserNotFoundException(USR_001_COMMON.getDefaultMessage(),USR_001_COMMON.getCode());
        }
        return bachelorDao.getAllBachelorsByUser(userId);
    }

    @Transactional
    public BachelorsEntity deleteBachelors(final String authorizationToken,final String bachelorsId) throws AuthenticationFailedException, ObjectNotFoundException, AuthorizationFailedException {
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        BachelorsEntity bachelorsEntity = bachelorDao.getBachelorsById(bachelorsId);
        if(bachelorsEntity==null){
            throw new ObjectNotFoundException(BACH_001.getDefaultMessage(),BACH_001.getCode());
        }
        if(bachelorsEntity.getUser_id()!=userAuthToken.getUser() && !userAuthToken.getUser().getRole().equals("admin")){
            throw new AuthorizationFailedException(ATHR_003_COMMON.getDefaultMessage(),ATHR_003_COMMON.getCode());
        }
        return bachelorDao.deleteBachelors(bachelorsEntity);
    }


    @Transactional
    public BachelorsEntity editBachelors(final String authorizationToken,final String bachelorsId,BachelorsEntity editedBachelorsEntity) throws AuthenticationFailedException,ObjectNotFoundException,AuthorizationFailedException{
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        BachelorsEntity bachelorsEntity = bachelorDao.getBachelorsById(bachelorsId);
        if( bachelorsEntity == null){
            throw new ObjectNotFoundException(BACH_001.getDefaultMessage(),BACH_001.getCode());
        }
        if(bachelorsEntity.getUser_id()!=userAuthToken.getUser()){
            throw new AuthorizationFailedException(ATHR_003_COMMON.getDefaultMessage(),ATHR_003_COMMON.getCode());
        }
        bachelorsEntity = getEditedEntity(editedBachelorsEntity,bachelorsEntity);
        return bachelorDao.editCollege(bachelorsEntity);
    }

    private BachelorsEntity getEditedEntity(BachelorsEntity editedBachelorsEntity,BachelorsEntity bachelorsEntity){

        if(editedBachelorsEntity.getCollegeName()!=null){
            bachelorsEntity.setCollegeName(editedBachelorsEntity.getCollegeName());
        }
        if(editedBachelorsEntity.getFromDate()!=null){
            bachelorsEntity.setFromDate(editedBachelorsEntity.getFromDate());
        }
        if(editedBachelorsEntity.getToDate()!=null){
            bachelorsEntity.setToDate(editedBachelorsEntity.getToDate());
        }
        if(editedBachelorsEntity.getGrade()!=null){
            bachelorsEntity.setGrade(editedBachelorsEntity.getGrade());
        }
        if(editedBachelorsEntity.getSubject()!=null){
            bachelorsEntity.setSubject(editedBachelorsEntity.getSubject());
        }
        return bachelorsEntity;
    }


}
