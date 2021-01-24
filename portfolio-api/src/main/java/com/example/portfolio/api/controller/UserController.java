package com.example.portfolio.api.controller;

import com.example.portfolio.api.model.SigninResponse;
import com.example.portfolio.api.model.SignoutResponse;
import com.example.portfolio.api.model.SignupUserRequest;
import com.example.portfolio.api.model.SignupUserResponse;
import com.example.portfolio.service.Business.UserService;
import com.example.portfolio.service.Entity.UserAuthTokenEntity;
import com.example.portfolio.service.Entity.UserEntity;
import com.example.portfolio.service.common.Constants;
import com.example.portfolio.service.exception.AuthenticationFailedException;
import com.example.portfolio.service.exception.SignOutRestrictedException;
import com.example.portfolio.service.exception.SignUpRestrictedException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userBussinessService;

    @Value("${user.default.role}")
    private String defaultRole;

    /** To create an user based on sign-up request details
     * @param signupUserRequest
     * @return
     * @throws SignUpRestrictedException
     */
    @RequestMapping(method = RequestMethod.POST, path = "/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> userSignup(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {
        final UserEntity userEntity = convertToUserEntity(signupUserRequest);
        final UserEntity createdUserEntity = userBussinessService.signup(userEntity);
        SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status(Constants.USER_REGISTRATION_MESSAGE);
        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
    }


    /** To sign-in an user based on authentication
     * @param authorization
     * @return
     * @throws AuthenticationFailedException
     */
    @RequestMapping(method = RequestMethod.POST, path = "/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> signin(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {


        UserAuthTokenEntity userAuthToken = userBussinessService.authenticate(authorization);

        SigninResponse signinResponse = new SigninResponse().id(userAuthToken.getUuid()).message(Constants.LOGIN_MESSAGE);

        HttpHeaders headers = new HttpHeaders();
        headers.add("access_token", userAuthToken.getAccessToken());
        return new ResponseEntity<SigninResponse>(signinResponse, headers, HttpStatus.OK);

    }

    /** To logout a signed in user
     * @param authorization
     * @return
     * @throws SignOutRestrictedException
     */
    @RequestMapping(method = RequestMethod.POST, path = "/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization") final String authorization) throws SignOutRestrictedException {

        final UserEntity user = userBussinessService.signoutUser(authorization);

        SignoutResponse signoutResponse = new SignoutResponse().id(user.getUuid()).message(Constants.LOGOUT_MESSAGE);
        return new ResponseEntity<SignoutResponse>(signoutResponse, HttpStatus.OK);
    }

    /** map the request object to user entity
     * @param signupUserRequest
     * @return
     */
    private UserEntity convertToUserEntity(final SignupUserRequest signupUserRequest) {
        UserEntity userEntity = modelMapper.map(signupUserRequest, UserEntity.class);
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setRole(defaultRole);
        return userEntity;

    }
}
