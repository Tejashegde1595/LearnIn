package com.example.portfolio.service.Business;

import com.example.portfolio.service.Dao.UserDao;
import com.example.portfolio.service.Entity.UserAuthTokenEntity;
import com.example.portfolio.service.Entity.UserEntity;
import com.example.portfolio.service.common.Constants;
import com.example.portfolio.service.exception.AuthenticationFailedException;
import com.example.portfolio.service.exception.SignOutRestrictedException;
import com.example.portfolio.service.exception.SignUpRestrictedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Base64;

import static com.example.portfolio.service.common.GenericErrorCode.*;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    /**
     * Business logic to create an user based on sign-up request details
     *
     * @param userEntity
     * @return
     * @throws SignUpRestrictedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(final UserEntity userEntity) throws SignUpRestrictedException {
        validateUserDetails(userEntity);
        String[] encryptedText = cryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);
        UserEntity user = userDao.createUser(userEntity);
        return user;

    }

    /**
     * Business logic for signing-in an user based on authentication
     *
     * @param authorization
     * @return
     * @throws AuthenticationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity authenticate(final String authorization) throws AuthenticationFailedException {
        String username = "";
        String password = "";
        try {
            byte[] decode = Base64.getDecoder().decode(authorization.split(Constants.HEADER_STRING)[1]);
            String decodedText = new String(decode);
            String[] decodedArray = decodedText.split(":");
            username = decodedArray[0];
            password = decodedArray[1];
        } catch (Exception e) {
            throw new AuthenticationFailedException(ATH_001.getCode(), ATH_001.getDefaultMessage());

        }
        UserEntity userEntity = userDao.getUserByUserName(username);
        if (userEntity == null) {
            throw new AuthenticationFailedException(ATH_001.getCode(), ATH_001.getDefaultMessage());
        }
        UserAuthTokenEntity userAuthToken=null;
        final String encryptedPassword = cryptographyProvider.encrypt(password, userEntity.getSalt());
        if (encryptedPassword.equals(userEntity.getPassword())) {
            userAuthToken = createUserAuthToken(userEntity, encryptedPassword);
        } else {
            throw new AuthenticationFailedException(ATH_002.getCode(), ATH_002.getDefaultMessage());
        }
        return userAuthToken;
    }

    /**
     * to create an user auth-token
     *
     * @param userEntity
     * @param secret
     * @return
     */
    private UserAuthTokenEntity createUserAuthToken(UserEntity userEntity, String secret) {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secret);
        UserAuthTokenEntity userAuthToken = new UserAuthTokenEntity();
        userAuthToken.setUser(userEntity);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiresAt = now.plusHours(Constants.EXPIRATION_TIME);
        userAuthToken.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
        userAuthToken.setLoginAt(now);
        userAuthToken.setExpiresAt(expiresAt);
        userAuthToken.setUuid(userEntity.getUuid());
        userDao.createAuthToken(userAuthToken);
        return userAuthToken;
    }

    /**
     * Business logic to logout an already signed in user
     *
     * @param authorization
     * @return
     * @throws SignOutRestrictedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signoutUser(final String authorization) throws SignOutRestrictedException {
        UserAuthTokenEntity userAuthToken = getUserAuthToken(authorization);

        if (userAuthToken.getLogoutAt() != null) {
            throw new SignOutRestrictedException(SGOR_001.getCode(), SGOR_001.getDefaultMessage());
        }
        userAuthToken.setLogoutAt(ZonedDateTime.now());
        userAuthToken = userDao.updateAuthToken(userAuthToken);

        return userAuthToken.getUser();
    }


    /**
     * To fetch user auth-token details
     *
     * @param authorizationToken
     * @return
     * @throws SignOutRestrictedException
     */
    private UserAuthTokenEntity getUserAuthToken(final String authorizationToken) throws
            SignOutRestrictedException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);
        if (userAuthTokenEntity == null) {
            throw new SignOutRestrictedException(SGOR_001.getCode(), SGOR_001.getDefaultMessage());
        }
        return userAuthTokenEntity;
    }


    /**
     * method to validate user data for sign-up request
     *
     * @param user
     * @throws SignUpRestrictedException
     */
    private void validateUserDetails(final UserEntity user) throws
            SignUpRestrictedException {

        UserEntity userEntity = userDao.getUserByUserName(user.getUserName());
        if (userEntity != null) {
            throw new SignUpRestrictedException(SGUR_001.getCode(), SGUR_001.getDefaultMessage());
        }

        userEntity = userDao.getUserByEmail(user.getEmailAddress());
        if (userEntity != null) {
            throw new SignUpRestrictedException(SGUR_002.getCode(), SGUR_002.getDefaultMessage());
        }

    }
}
