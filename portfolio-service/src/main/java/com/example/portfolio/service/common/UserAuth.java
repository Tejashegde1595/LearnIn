package com.example.portfolio.service.common;

import com.example.portfolio.service.Dao.UserDao;
import com.example.portfolio.service.Entity.UserAuthTokenEntity;
import com.example.portfolio.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

import static com.example.portfolio.service.common.GenericErrorCode.SGOR_001;

@Component
public class UserAuth {
    @Autowired
    private UserDao userDao;

    public UserAuthTokenEntity getUserAuthToken(final String authorizationToken) throws
            AuthenticationFailedException {
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
