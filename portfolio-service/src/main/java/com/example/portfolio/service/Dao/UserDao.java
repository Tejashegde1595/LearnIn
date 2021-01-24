package com.example.portfolio.service.Dao;

import com.example.portfolio.service.Entity.UserAuthTokenEntity;
import com.example.portfolio.service.Entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {
    private final Logger log = LoggerFactory.getLogger(UserDao.class);
    @PersistenceContext
    private EntityManager entityManager;

    /** To persist user entity in db
     * @param userEntity
     * @return
     */
    public UserEntity createUser(UserEntity userEntity) {
        log.info("create a new user in the database");
        entityManager.persist(userEntity);
        log.info("succesfully created a new user in the database");
        return userEntity;
    }

    /** get an user from db based on user uuid
     * @param userUuid
     * @return
     */
    public UserEntity getUser(final String userUuid) {
        log.info("get an user from the database based on User id");
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", userUuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            log.info("no user present in the database with the User id");
            return null;
        }
    }

    /** get an user from db based on user email
     * @param email
     * @return
     */
    public UserEntity getUserByEmail(final String email) {
        log.info("get an user from the database based on User Email");
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            log.info("no user present in the database with the User email");
            return null;
        }
    }

    /** get an user from db based on user name
     * @param UserName
     * @return
     */
    public UserEntity getUserByUserName(final String UserName) {
        log.info("get an user from the database based on User Name");
        try {
            return entityManager.createNamedQuery("userByUserName", UserEntity.class).setParameter("userName", UserName).getSingleResult();
        } catch (NoResultException nre) {
            log.info("no user present in the database with the User Name");
            return null;
        }
    }

    /** To persist user auth-token entity in db
     * @param userAuthTokenEntity
     * @return
     */
    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        log.info("creating user-auth token entity in database");
        entityManager.persist(userAuthTokenEntity);
        log.info("succesffully created user-auth token entity in database");
        return userAuthTokenEntity;
    }

    /** To update an already existing user auth-token entity in db
     * @param userAuthTokenEntity
     * @return
     */
    public UserAuthTokenEntity updateAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        log.info("updating user-auth token entity in database");
        entityManager.merge(userAuthTokenEntity);
        log.info("succesfully updated user-auth token entity in database");
        return userAuthTokenEntity;
    }

    /** get user auth-token entity from db based on token string
     * @param accessToken
     * @return
     */
    public UserAuthTokenEntity getUserAuthToken(final String accessToken) {
        log.info("get user-auth token entity from database based on the token");
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            log.info("no user auth token present in the database with the following access token");
            return null;
        }
    }

    /** to remove an user from the user table
     * @param userEntity
     */
    public void deleteUser(final UserEntity userEntity) {
        log.info("remove an user from the database");
        entityManager.remove(userEntity);
        log.info("succesfully removed an user from the database");
    }
}
