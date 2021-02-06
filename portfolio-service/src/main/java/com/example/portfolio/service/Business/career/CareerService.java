package com.example.portfolio.service.Business.career;
import com.example.portfolio.service.Entity.UserEntity;
import com.example.portfolio.service.Entity.career.CareerEntity;
import com.example.portfolio.service.Entity.education.CollegeEntity;
import com.example.portfolio.service.common.UserAuth;
import com.example.portfolio.service.Dao.UserDao;
import com.example.portfolio.service.Dao.career.CareerDao;
import com.example.portfolio.service.Dao.education.BachelorDao;
import com.example.portfolio.service.Entity.UserAuthTokenEntity;
import com.example.portfolio.service.Entity.education.BachelorsEntity;
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
public class CareerService {
    @Autowired
    private CareerDao careerDao;

    @Autowired
    private UserAuth userAuth;

    @Autowired
    private UserDao userDao;

    @Transactional
    public CareerEntity createCareer(final CareerEntity careerEntity, final String authorizationToken) throws AuthenticationFailedException {
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        careerEntity.setUser_id(userAuthToken.getUser());
        return careerDao.createCareerEntity(careerEntity);
    }
    public List<CareerEntity> getAllCareersInfo(final String authorizationToken) throws AuthenticationFailedException {
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        return careerDao.getAllCareers();
    }

    public List<CareerEntity> getAllCareersByUserId(final String authorizationToken,final String userId) throws AuthenticationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        UserEntity userEntity = userDao.getUser(userId);
        if(userEntity==null){
            throw new UserNotFoundException(USR_001_COMMON.getDefaultMessage(),USR_001_COMMON.getCode());
        }
        return careerDao.getAllCareersByUser(userId);
    }

    @Transactional
    public CareerEntity deleteCareers(final String authorizationToken,final String bachelorsId) throws AuthenticationFailedException, ObjectNotFoundException, AuthorizationFailedException {
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        CareerEntity careerEntity = careerDao.getCareersById(bachelorsId);
        if(careerEntity==null){
            throw new ObjectNotFoundException(CAR_001.getDefaultMessage(),CAR_001.getCode());
        }
        if(careerEntity.getUser_id()!=userAuthToken.getUser() && !userAuthToken.getUser().getRole().equals("admin")){
            throw new AuthorizationFailedException(ATHR_003_COMMON.getDefaultMessage(),ATHR_003_COMMON.getCode());
        }
        return careerDao.deleteCareerInfo(careerEntity);
    }


    @Transactional
    public CareerEntity editCareers(final String authorizationToken,final String careersId,CareerEntity editedCareersEntity) throws AuthenticationFailedException,ObjectNotFoundException,AuthorizationFailedException{
        UserAuthTokenEntity userAuthToken = userAuth.getUserAuthToken(authorizationToken);
        CareerEntity careerEntity = careerDao.getCareersById(careersId);
        if( careerEntity == null){
            throw new ObjectNotFoundException(CAR_001.getDefaultMessage(),CAR_001.getCode());
        }
        if(careerEntity.getUser_id()!=userAuthToken.getUser()){
            throw new AuthorizationFailedException(ATHR_003_COMMON.getDefaultMessage(),ATHR_003_COMMON.getCode());
        }
        careerEntity = getEditedEntity(editedCareersEntity,careerEntity);
        return careerDao.editCareerInfo(careerEntity);
    }

    private CareerEntity getEditedEntity(CareerEntity editedCareersEntity, CareerEntity careerEntity){

        if(editedCareersEntity.getCompanyName()!=null){
            careerEntity.setCompanyName(editedCareersEntity.getCompanyName());
        }
        if(editedCareersEntity.getFromDate()!=null){
            careerEntity.setFromDate(editedCareersEntity.getFromDate());
        }
        if(editedCareersEntity.getToDate()!=null){
            careerEntity.setToDate(editedCareersEntity.getToDate());
        }
        if(editedCareersEntity.getDesignation()!=null){
            careerEntity.setDesignation(editedCareersEntity.getDesignation());
        }
        return careerEntity;
    }

}
