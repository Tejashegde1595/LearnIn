package com.example.portfolio.service.Business.education;

import com.example.portfolio.service.Dao.UserDao;
import com.example.portfolio.service.Dao.education.CollegeDao;
import com.example.portfolio.service.Entity.UserAuthTokenEntity;
import com.example.portfolio.service.Entity.UserEntity;
import com.example.portfolio.service.Entity.education.CollegeEntity;
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
public class CollegeService {

    @Autowired
    private CollegeDao collegeDao;

    @Autowired
    private UserDao userDao;

    @Transactional
    public CollegeEntity createCollege(final CollegeEntity collegeEntity,final String authorizationToken) throws AuthenticationFailedException {
        UserAuthTokenEntity userAuthToken = getUserAuthToken(authorizationToken);

        if (userAuthToken.getLogoutAt() != null || userAuthToken.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthenticationFailedException(SGOR_001.getCode(), SGOR_001.getDefaultMessage());
        }
        collegeEntity.setUser_id(userAuthToken.getUser());
        return collegeDao.createCollegeEntity(collegeEntity);
    }

    public List<CollegeEntity> getAllCollege(final String authorizationToken) throws AuthenticationFailedException {
        UserAuthTokenEntity userAuthToken = getUserAuthToken(authorizationToken);

        if (userAuthToken.getLogoutAt() != null || userAuthToken.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthenticationFailedException(SGOR_001.getCode(), SGOR_001.getDefaultMessage());
        }

        return collegeDao.getAllColleges();
    }

    public List<CollegeEntity> getCollegeByUserId(final String authorizationToken,final String userId) throws AuthenticationFailedException,UserNotFoundException {
        UserAuthTokenEntity userAuthToken = getUserAuthToken(authorizationToken);

        if (userAuthToken.getLogoutAt() != null || userAuthToken.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthenticationFailedException(SGOR_001.getCode(), SGOR_001.getDefaultMessage());
        }
        UserEntity userEntity = userDao.getUser(userId);
        if(userEntity==null){
            throw new UserNotFoundException(USR_001_COMMON.getDefaultMessage(),USR_001_COMMON.getCode());
        }
        return collegeDao.getAllCollegesByUser(userId);
    }

    @Transactional
    public CollegeEntity deleteCollege(final String authorizationToken,final String collegeId) throws AuthenticationFailedException,ObjectNotFoundException,AuthorizationFailedException{
        UserAuthTokenEntity userAuthToken = getUserAuthToken(authorizationToken);

        if (userAuthToken.getLogoutAt() != null || userAuthToken.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthenticationFailedException(SGOR_001.getCode(), SGOR_001.getDefaultMessage());
        }
        CollegeEntity CollegeEntity = collegeDao.getCollegeById(collegeId);
        if(CollegeEntity==null){
            throw new ObjectNotFoundException(COL_001.getDefaultMessage(),COL_001.getCode());
        }
        if(CollegeEntity.getUser_id()!=userAuthToken.getUser() && !userAuthToken.getUser().getRole().equals("admin")){
            throw new AuthorizationFailedException(ATHR_003_COMMON.getDefaultMessage(),ATHR_003_COMMON.getCode());
        }
        return collegeDao.deleteCollege(CollegeEntity);
    }

    @Transactional
    public CollegeEntity editCollege(final String authorizationToken,final String collegeId,CollegeEntity editedCollegeEntity) throws AuthenticationFailedException,ObjectNotFoundException,AuthorizationFailedException{
        UserAuthTokenEntity userAuthToken = getUserAuthToken(authorizationToken);

        if (userAuthToken.getLogoutAt() != null || userAuthToken.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthenticationFailedException(SGOR_001.getCode(), SGOR_001.getDefaultMessage());
        }
        CollegeEntity CollegeEntity = collegeDao.getCollegeById(collegeId);
        if(CollegeEntity==null){
            throw new ObjectNotFoundException(SCHL_001.getDefaultMessage(),SCHL_001.getCode());
        }
        if(CollegeEntity.getUser_id()!=userAuthToken.getUser()){
            throw new AuthorizationFailedException(ATHR_003_COMMON.getDefaultMessage(),ATHR_003_COMMON.getCode());
        }
        CollegeEntity = getEditedEntity(editedCollegeEntity,CollegeEntity);
        return collegeDao.editCollege(CollegeEntity);
    }

    private UserAuthTokenEntity getUserAuthToken(final String authorizationToken) throws
            AuthenticationFailedException{
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);
        if (userAuthTokenEntity == null) {
            throw new AuthenticationFailedException(SGOR_001.getCode(), SGOR_001.getDefaultMessage());
        }
        return userAuthTokenEntity;
    }

    private CollegeEntity getEditedEntity(CollegeEntity editedCollegeEntity,CollegeEntity collegeEntity){

        if(editedCollegeEntity.getCollegeName()!=null){
            collegeEntity.setCollegeName(editedCollegeEntity.getCollegeName());
        }
        if(editedCollegeEntity.getFromDate()!=null){
            collegeEntity.setFromDate(editedCollegeEntity.getFromDate());
        }
        if(editedCollegeEntity.getToDate()!=null){
            collegeEntity.setToDate(editedCollegeEntity.getToDate());
        }
        if(editedCollegeEntity.getGrade()!=null){
            collegeEntity.setGrade(editedCollegeEntity.getGrade());
        }
        if(editedCollegeEntity.getSubject()!=null){
            collegeEntity.setSubject(editedCollegeEntity.getSubject());
        }
        return collegeEntity;
    }
}
