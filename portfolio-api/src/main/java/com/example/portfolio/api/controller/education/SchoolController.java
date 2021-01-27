package com.example.portfolio.api.controller.education;

import com.example.portfolio.api.model.*;
import com.example.portfolio.service.Business.education.SchoolService;
import com.example.portfolio.service.Entity.education.SchoolEntity;
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

import static com.example.portfolio.service.common.Constants.SCHOOL_CREATION_MESSAGE;
import static com.example.portfolio.service.common.GenericErrorCode.DATE_001;

@RestController
@RequestMapping("/education")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

   @RequestMapping(method = RequestMethod.POST,path = "/school/create",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SchoolResponse> createSchool(final SchoolRequest schoolRequest,@RequestHeader("authorization") final String authorizationToken) throws DateConvertException, AuthenticationFailedException {
        SchoolEntity schoolEntity = requestToEntity(schoolRequest);
        schoolEntity = schoolService.createSchool(schoolEntity,authorizationToken);
        SchoolResponse schoolResponse = new SchoolResponse().id(schoolEntity.getUuid()).status(SCHOOL_CREATION_MESSAGE);
        return new ResponseEntity<SchoolResponse>(schoolResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/school/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<SchoolInfoDetailsResponse>> getAllSchoolInfo(@RequestHeader("authorization") final String authorizationToken) throws AuthenticationFailedException{
        List<SchoolEntity> schoolEntityList = schoolService.getAllSchool(authorizationToken);
        List<SchoolInfoDetailsResponse> schoolInfoDetailsResponses = entityToResponseList(schoolEntityList);
        return new ResponseEntity<List<SchoolInfoDetailsResponse>>(schoolInfoDetailsResponses,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/school/all/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<SchoolInfoDetailsResponse>> getSchoolInfoByUser(@RequestHeader("authorization") final String authorizationToken,@PathVariable(name="userId") final String userId) throws AuthenticationFailedException, UserNotFoundException {
        List<SchoolEntity> schoolEntityList = schoolService.getSchoolByUserId(authorizationToken,userId);
        List<SchoolInfoDetailsResponse> schoolInfoDetailsResponses = entityToResponseList(schoolEntityList);
        return new ResponseEntity<List<SchoolInfoDetailsResponse>>(schoolInfoDetailsResponses,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE,path = "/school/delete/{schoolId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SchoolInfoDeleteResponse> deleteSchoolInfo(@RequestHeader("authorization") final String authorizationToken,@PathVariable(name="schoolId") final String schoolId) throws AuthenticationFailedException, ObjectNotFoundException, AuthorizationFailedException {
        SchoolEntity schoolEntity = schoolService.deleteSchool(authorizationToken,schoolId);
        SchoolInfoDeleteResponse schoolInfoDeleteResponse = new SchoolInfoDeleteResponse().id(schoolEntity.getUuid()).status("SUCCESSFULLY DELETED THE SCHOOL INFO");
        return new ResponseEntity<SchoolInfoDeleteResponse>(schoolInfoDeleteResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT,path = "/school/edit/{schoolId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SchoolInfoEditResponse> editSchoolInfo(@RequestHeader("authorization") final String authorizationToken, @PathVariable(name="schoolId") final String schoolId, SchoolInfoEditRequest schoolInfoEditRequest) throws AuthenticationFailedException, ObjectNotFoundException, AuthorizationFailedException,DateConvertException {
        SchoolEntity schoolEntity = getEditRequestToEntity(schoolInfoEditRequest);
        SchoolEntity schoolEntityResponse = schoolService.editSchool(authorizationToken,schoolId,schoolEntity);
        SchoolInfoEditResponse schoolInfoDeleteResponse = new SchoolInfoEditResponse().id(schoolEntity.getUuid()).status("SUCCESSFULLY EDITED THE SCHOOL INFO");
        return new ResponseEntity<SchoolInfoEditResponse>(schoolInfoDeleteResponse,HttpStatus.OK);
    }
    private SchoolEntity requestToEntity(final SchoolRequest schoolRequest) throws DateConvertException {
        DateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        SchoolEntity schoolEntity = new SchoolEntity();
        schoolEntity.setUuid(UUID.randomUUID().toString());
        schoolEntity.setSchoolName(schoolRequest.getSchoolName());
        schoolEntity.setGrade(schoolRequest.getGrade());
        try {
            schoolEntity.setFromDate(formatter.parse(schoolRequest.getFromDate()));
            schoolEntity.setToDate(formatter.parse(schoolRequest.getToDate()));
        }catch (ParseException exception){
            throw new DateConvertException(DATE_001.getDefaultMessage(),DATE_001.getCode());
        }
        return schoolEntity;
    }

    private List<SchoolInfoDetailsResponse> entityToResponseList(final List<SchoolEntity> schoolEntityList){
        Iterator<SchoolEntity> iterator = schoolEntityList.listIterator();
        List<SchoolInfoDetailsResponse> schoolInfoDetailsResponses = new ArrayList<SchoolInfoDetailsResponse>();
        while(iterator.hasNext()){
            SchoolEntity schoolEntity = iterator.next();
            SchoolInfoDetailsResponse schoolInfoDetailsResponse = new SchoolInfoDetailsResponse().schoolName(schoolEntity.getSchoolName()).fromDate(schoolEntity.getFromDate().toString()).
                    toDate(schoolEntity.getToDate().toString()).grade(schoolEntity.getGrade()).id(schoolEntity.getUuid());
            schoolInfoDetailsResponses.add(schoolInfoDetailsResponse);
        }
        return schoolInfoDetailsResponses;
    }

    private SchoolEntity getEditRequestToEntity(final SchoolInfoEditRequest schoolInfoEditRequest) throws DateConvertException{
       DateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
       SchoolEntity schoolEntity = new SchoolEntity();
       schoolEntity.setSchoolName(schoolInfoEditRequest.getSchoolName());
       schoolEntity.setGrade(schoolInfoEditRequest.getGrade());
       try{
           schoolEntity.setFromDate(formatter.parse(schoolInfoEditRequest.getFromDate()));
           schoolEntity.setToDate(formatter.parse(schoolInfoEditRequest.getToDate()));
       }catch (ParseException exception){
           throw new DateConvertException(DATE_001.getDefaultMessage(),DATE_001.getCode());
       }
        return schoolEntity;
    }
}

