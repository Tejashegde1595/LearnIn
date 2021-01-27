package com.example.portfolio.service.Business.education;

import com.example.portfolio.service.Dao.UserDao;
import com.example.portfolio.service.Dao.education.SchoolDao;
import com.example.portfolio.service.Entity.UserAuthTokenEntity;
import com.example.portfolio.service.Entity.UserEntity;
import com.example.portfolio.service.Entity.education.SchoolEntity;
import com.example.portfolio.service.exception.AuthenticationFailedException;
import com.example.portfolio.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static com.example.portfolio.service.common.GenericErrorCode.SGOR_001;
import static com.example.portfolio.service.common.GenericErrorCode.USR_001_COMMON;

@Service
public class SchoolService {

    @Autowired
    private SchoolDao schoolDao;

    @Autowired
    private UserDao userDao;

    @Transactional
    public SchoolEntity createSchool(final SchoolEntity schoolEntity,final String authorizationToken) throws AuthenticationFailedException {
        UserAuthTokenEntity userAuthToken = getUserAuthToken(authorizationToken);

        if (userAuthToken.getLogoutAt() != null || userAuthToken.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthenticationFailedException(SGOR_001.getCode(), SGOR_001.getDefaultMessage());
        }
        schoolEntity.setUser_id(userAuthToken.getUser());
        return schoolDao.createSchoolEntity(schoolEntity);
    }

    public List<SchoolEntity> getAllSchool(final String authorizationToken) throws AuthenticationFailedException {
        UserAuthTokenEntity userAuthToken = getUserAuthToken(authorizationToken);

        if (userAuthToken.getLogoutAt() != null || userAuthToken.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthenticationFailedException(SGOR_001.getCode(), SGOR_001.getDefaultMessage());
        }

        return schoolDao.getAllSchools();
    }

    public List<SchoolEntity> getSchoolByUserId(final String authorizationToken,final String userId) throws AuthenticationFailedException,UserNotFoundException {
        UserAuthTokenEntity userAuthToken = getUserAuthToken(authorizationToken);

        if (userAuthToken.getLogoutAt() != null || userAuthToken.getExpiresAt().isBefore(ZonedDateTime.now())) {
            throw new AuthenticationFailedException(SGOR_001.getCode(), SGOR_001.getDefaultMessage());
        }
        UserEntity userEntity = userDao.getUser(userId);
        if(userEntity==null){
            throw new UserNotFoundException(USR_001_COMMON.getDefaultMessage(),USR_001_COMMON.getCode());
        }
        return schoolDao.getAllSchoolsByUser(userId);
    }


    private UserAuthTokenEntity getUserAuthToken(final String authorizationToken) throws
            AuthenticationFailedException{
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);
        if (userAuthTokenEntity == null) {
            throw new AuthenticationFailedException(SGOR_001.getCode(), SGOR_001.getDefaultMessage());
        }
        return userAuthTokenEntity;
    }
}
