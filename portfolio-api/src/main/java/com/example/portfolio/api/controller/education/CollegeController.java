package com.example.portfolio.api.controller.education;

import com.example.portfolio.api.model.*;
import com.example.portfolio.service.Business.education.CollegeService;
import com.example.portfolio.service.Entity.education.CollegeEntity;
import com.example.portfolio.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.example.portfolio.service.common.Constants.COLLEGE_CREATION_MESSAGE;
import static com.example.portfolio.service.common.GenericErrorCode.DATE_001;

@RestController
@RequestMapping("/education")
public class CollegeController {
    @Autowired
    private CollegeService collegeService;

    @RequestMapping(method = RequestMethod.POST,path = "/college/create",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CollegeResponse> createCollege(final CollegeRequest collegeRequest, @RequestHeader("authorization") final String authorizationToken) throws DateConvertException, AuthenticationFailedException {
        CollegeEntity collegeEntity = requestToEntity(collegeRequest);
        collegeEntity = collegeService.createCollege(collegeEntity,authorizationToken);
        CollegeResponse collegeResponse = new CollegeResponse().id(collegeEntity.getUuid()).status(COLLEGE_CREATION_MESSAGE);
        return new ResponseEntity<CollegeResponse>(collegeResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/college/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<CollegeInfoDetailsResponse>> getAllCollegeInfo(@RequestHeader("authorization") final String authorizationToken) throws AuthenticationFailedException{
        List<CollegeEntity> collegeEntityList = collegeService.getAllCollege(authorizationToken);
        List<CollegeInfoDetailsResponse> collegeInfoDetailsResponses = entityToResponseList(collegeEntityList);
        return new ResponseEntity<List<CollegeInfoDetailsResponse>>(collegeInfoDetailsResponses,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/college/all/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<CollegeInfoDetailsResponse>> getCollegeInfoByUser(@RequestHeader("authorization") final String authorizationToken,@PathVariable(name="userId") final String userId) throws AuthenticationFailedException, UserNotFoundException {
        List<CollegeEntity> collegeEntityList = collegeService.getCollegeByUserId(authorizationToken,userId);
        List<CollegeInfoDetailsResponse> collegeInfoDetailsResponses = entityToResponseList(collegeEntityList);
        return new ResponseEntity<List<CollegeInfoDetailsResponse>>(collegeInfoDetailsResponses,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE,path = "/college/delete/{collegeId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CollegeInfoDeleteResponse> deleteCollegeInfo(@RequestHeader("authorization") final String authorizationToken,@PathVariable(name="collegeId") final String collegeId) throws AuthenticationFailedException, ObjectNotFoundException, AuthorizationFailedException {
        CollegeEntity collegeEntity = collegeService.deleteCollege(authorizationToken,collegeId);
        CollegeInfoDeleteResponse collegeInfoDeleteResponse = new CollegeInfoDeleteResponse().id(collegeEntity.getUuid()).status("SUCCESSFULLY DELETED THE COLLEGE INFO");
        return new ResponseEntity<CollegeInfoDeleteResponse>(collegeInfoDeleteResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT,path = "/college/edit/{collegeId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CollegeInfoEditResponse> editCollegeInfo(@RequestHeader("authorization") final String authorizationToken, @PathVariable(name="collegeId") final String collegeId, CollegeInfoEditRequest collegeInfoEditRequest) throws AuthenticationFailedException, ObjectNotFoundException, AuthorizationFailedException,DateConvertException {
        CollegeEntity collegeEntity = getEditRequestToEntity(collegeInfoEditRequest);
        CollegeEntity collegeEntity1 = collegeService.editCollege(authorizationToken,collegeId,collegeEntity);
        CollegeInfoEditResponse collegeInfoEditResponse = new CollegeInfoEditResponse().id(collegeEntity1.getUuid()).status("SUCCESSFULLY EDITED THE COLLEGE INFO");
        return new ResponseEntity<CollegeInfoEditResponse>(collegeInfoEditResponse,HttpStatus.OK);
    }
    private CollegeEntity requestToEntity(final CollegeRequest collegeRequest) throws DateConvertException {
        DateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        CollegeEntity collegeEntity = new CollegeEntity();
        collegeEntity.setUuid(UUID.randomUUID().toString());
        collegeEntity.setCollegeName(collegeRequest.getCollegeName());
        collegeEntity.setGrade(collegeRequest.getGrade());
        collegeEntity.setSubject(collegeRequest.getSubject());
        try {
            collegeEntity.setFromDate(formatter.parse(collegeRequest.getFromDate()));
            collegeEntity.setToDate(formatter.parse(collegeRequest.getToDate()));
        }catch (ParseException exception){
            throw new DateConvertException(DATE_001.getDefaultMessage(),DATE_001.getCode());
        }
        return collegeEntity;
    }

    private List<CollegeInfoDetailsResponse> entityToResponseList(final List<CollegeEntity> collegeEntityList){
        Iterator<CollegeEntity> iterator = collegeEntityList.listIterator();
        List<CollegeInfoDetailsResponse> collegeInfoDetailsResponses = new ArrayList<CollegeInfoDetailsResponse>();
        while(iterator.hasNext()){
            CollegeEntity collegeEntity = iterator.next();
            CollegeInfoDetailsResponse schoolInfoDetailsResponse = new CollegeInfoDetailsResponse().collegeName(collegeEntity.getCollegeName()).fromDate(collegeEntity.getFromDate().toString()).
                    toDate(collegeEntity.getToDate().toString()).grade(collegeEntity.getGrade()).id(collegeEntity.getUuid()).subject(collegeEntity.getSubject());
            collegeInfoDetailsResponses.add(schoolInfoDetailsResponse);
        }
        return collegeInfoDetailsResponses;
    }

    private CollegeEntity getEditRequestToEntity(final CollegeInfoEditRequest collegeInfoEditRequest) throws DateConvertException{
        DateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        CollegeEntity collegeEntity = new CollegeEntity();
        collegeEntity.setCollegeName(collegeInfoEditRequest.getCollegeName());
        collegeEntity.setGrade(collegeInfoEditRequest.getGrade());
        collegeEntity.setSubject(collegeInfoEditRequest.getSubject());
        try{
            collegeEntity.setFromDate(formatter.parse(collegeInfoEditRequest.getFromDate()));
            collegeEntity.setToDate(formatter.parse(collegeInfoEditRequest.getToDate()));
        }catch (ParseException exception){
            throw new DateConvertException(DATE_001.getDefaultMessage(),DATE_001.getCode());
        }
        return collegeEntity;
    }
}
