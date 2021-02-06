package com.example.portfolio.service.Business.education;

import com.example.portfolio.service.Dao.UserDao;
import com.example.portfolio.service.Dao.education.SchoolDao;
import com.example.portfolio.service.Entity.UserAuthTokenEntity;
import com.example.portfolio.service.Entity.UserEntity;
import com.example.portfolio.service.Entity.education.SchoolEntity;
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

@Service
public class SchoolService {

    @Autowired
    private SchoolDao schoolDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserAuth userAuth;

    @Transactional
    public SchoolEntity createSchool(final SchoolEntity schoolEntity,final String authorizationToken) throws AuthenticationFailedException {
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        schoolEntity.setUser_id(userAuthToken.getUser());
        return schoolDao.createSchoolEntity(schoolEntity);
    }

    public List<SchoolEntity> getAllSchool(final String authorizationToken) throws AuthenticationFailedException {
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        return schoolDao.getAllSchools();
    }

    public List<SchoolEntity> getSchoolByUserId(final String authorizationToken,final String userId) throws AuthenticationFailedException,UserNotFoundException {
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        UserEntity userEntity = userDao.getUser(userId);
        if(userEntity==null){
            throw new UserNotFoundException(USR_001_COMMON.getDefaultMessage(),USR_001_COMMON.getCode());
        }
        return schoolDao.getAllSchoolsByUser(userId);
    }

    @Transactional
    public SchoolEntity deleteSchool(final String authorizationToken,final String schoolId) throws AuthenticationFailedException,ObjectNotFoundException,AuthorizationFailedException{
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        SchoolEntity schoolEntity = schoolDao.getSchoolById(schoolId);
        if(schoolEntity==null){
            throw new ObjectNotFoundException(SCHL_001.getDefaultMessage(),SCHL_001.getCode());
        }
        if(schoolEntity.getUser_id()!=userAuthToken.getUser() && !userAuthToken.getUser().getRole().equals("admin")){
            throw new AuthorizationFailedException(ATHR_003_COMMON.getDefaultMessage(),ATHR_003_COMMON.getCode());
        }
        return schoolDao.deleteSchool(schoolEntity);
    }

    @Transactional
    public SchoolEntity editSchool(final String authorizationToken,final String schoolId,SchoolEntity editedSchoolEntity) throws AuthenticationFailedException,ObjectNotFoundException,AuthorizationFailedException{
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        SchoolEntity schoolEntity = schoolDao.getSchoolById(schoolId);
        if(schoolEntity==null){
            throw new ObjectNotFoundException(SCHL_001.getDefaultMessage(),SCHL_001.getCode());
        }
        if(schoolEntity.getUser_id()!=userAuthToken.getUser()){
            throw new AuthorizationFailedException(ATHR_003_COMMON.getDefaultMessage(),ATHR_003_COMMON.getCode());
        }
        schoolEntity = getEditedEntity(editedSchoolEntity,schoolEntity);
        return schoolDao.editSchool(schoolEntity);
    }


    private SchoolEntity getEditedEntity(SchoolEntity editedSchoolEntity,SchoolEntity schoolEntity){

        if(editedSchoolEntity.getSchoolName()!=null){
            schoolEntity.setSchoolName(editedSchoolEntity.getSchoolName());
        }
        if(editedSchoolEntity.getFromDate()!=null){
            schoolEntity.setFromDate(editedSchoolEntity.getFromDate());
        }
        if(editedSchoolEntity.getToDate()!=null){
            schoolEntity.setToDate(editedSchoolEntity.getToDate());
        }
        if(editedSchoolEntity.getGrade()!=null){
            schoolEntity.setGrade(editedSchoolEntity.getGrade());
        }
        return schoolEntity;
    }
}
