package com.example.portfolio.api.controller.education;

import com.example.portfolio.api.model.*;
import com.example.portfolio.service.Business.education.BachelorService;
import com.example.portfolio.service.Business.education.CollegeService;
import com.example.portfolio.service.Entity.education.BachelorsEntity;
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
public class BachelorsController {
    @Autowired
    private BachelorService bachelorService;

    @RequestMapping(method = RequestMethod.POST,path = "/bachelors/create",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<BachelorsResponse> creatBachelors(final BachelorsRequest bachelorsRequest, @RequestHeader("authorization") final String authorizationToken) throws DateConvertException, AuthenticationFailedException {
        BachelorsEntity bachelorsEntity = requestToEntity(bachelorsRequest);
        bachelorsEntity = bachelorService.createBachelors(bachelorsEntity,authorizationToken);
        BachelorsResponse bachelorsResponse = new BachelorsResponse().id(bachelorsEntity.getUuid()).status(COLLEGE_CREATION_MESSAGE);
        return new ResponseEntity<BachelorsResponse>(bachelorsResponse, HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.GET,path = "/bachelors/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<BachelorsInfoDetailsResponse>> getAllCollegeInfo(@RequestHeader("authorization") final String authorizationToken) throws AuthenticationFailedException{
        List<BachelorsEntity> bachelorsEntityList = bachelorService.getAllBachelors(authorizationToken);
        List<BachelorsInfoDetailsResponse> bachelorsInfoDetailsResponses = entityToResponseList(bachelorsEntityList);
        return new ResponseEntity<List<BachelorsInfoDetailsResponse>>(bachelorsInfoDetailsResponses,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/bachelors/all/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<BachelorsInfoDetailsResponse>> getCollegeInfoByUser(@RequestHeader("authorization") final String authorizationToken,@PathVariable(name="userId") final String userId) throws AuthenticationFailedException, UserNotFoundException {
        List<BachelorsEntity> bachelorsEntityList = bachelorService.getAllBachelorsByUserId(authorizationToken,userId);
        List<BachelorsInfoDetailsResponse> bachelorsInfoDetailsResponses = entityToResponseList(bachelorsEntityList);
        return new ResponseEntity<List<BachelorsInfoDetailsResponse>>(bachelorsInfoDetailsResponses,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE,path = "/bachelors/delete/{bachelorsId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<BachelorsInfoDeleteResponse> deleteCollegeInfo(@RequestHeader("authorization") final String authorizationToken,@PathVariable(name="bachelorsId") final String bachelorsId) throws AuthenticationFailedException, ObjectNotFoundException, AuthorizationFailedException {
        BachelorsEntity bachelorsEntity = bachelorService.deleteBachelors(authorizationToken,bachelorsId);
        BachelorsInfoDeleteResponse bachelorsInfoDeleteResponse = new BachelorsInfoDeleteResponse().id(bachelorsEntity.getUuid()).status("SUCCESSFULLY DELETED THE BACHELOR COLLEGE INFO");
        return new ResponseEntity<BachelorsInfoDeleteResponse>(bachelorsInfoDeleteResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT,path = "/bachelors/edit/{bachelorsId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<BachelorsInfoEditResponse> editCollegeInfo(@RequestHeader("authorization") final String authorizationToken, @PathVariable(name="bachelorsId") final String bachelorsId, BachelorsInfoEditRequest bachelorsInfoEditRequest) throws AuthenticationFailedException, ObjectNotFoundException, AuthorizationFailedException,DateConvertException {
        BachelorsEntity bachelorsEntity = getEditRequestToEntity(bachelorsInfoEditRequest);
        BachelorsEntity bachelorsEntity1 = bachelorService.editBachelors(authorizationToken,bachelorsId,bachelorsEntity);
        BachelorsInfoEditResponse bachelorsInfoEditResponse = new BachelorsInfoEditResponse().id(bachelorsEntity1.getUuid()).status("SUCCESSFULLY EDITED THE BACHELOR THE COLLEGE INFO");
        return new ResponseEntity<BachelorsInfoEditResponse>(bachelorsInfoEditResponse,HttpStatus.OK);
    }

    private List<BachelorsInfoDetailsResponse> entityToResponseList(final List<BachelorsEntity> bachelorsEntityList){
        Iterator<BachelorsEntity> iterator = bachelorsEntityList.listIterator();
        List<BachelorsInfoDetailsResponse> bachelorsInfoDetailsResponses = new ArrayList<BachelorsInfoDetailsResponse>();
        while(iterator.hasNext()){
            BachelorsEntity bachelorsEntity = iterator.next();
            BachelorsInfoDetailsResponse bachelorsInfoDetailsResponse = new BachelorsInfoDetailsResponse().bachelorsCollegeName(bachelorsEntity.getCollegeName()).fromDate(bachelorsEntity.getFromDate().toString()).
                    toDate(bachelorsEntity.getToDate().toString()).grade(bachelorsEntity.getGrade()).id(bachelorsEntity.getUuid()).subject(bachelorsEntity.getSubject());
            bachelorsInfoDetailsResponses.add(bachelorsInfoDetailsResponse);
        }
        return bachelorsInfoDetailsResponses;
    }

    private BachelorsEntity requestToEntity(final BachelorsRequest bachelorsRequest) throws DateConvertException {
        DateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        BachelorsEntity bachelorsEntity = new BachelorsEntity();
        bachelorsEntity.setUuid(UUID.randomUUID().toString());
        bachelorsEntity.setCollegeName(bachelorsRequest.getBachelorsCollegeName());
        bachelorsEntity.setGrade(bachelorsRequest.getGrade());
        bachelorsEntity.setSubject(bachelorsRequest.getSubject());
        try {
            bachelorsEntity.setFromDate(formatter.parse(bachelorsRequest.getFromDate()));
            bachelorsEntity.setToDate(formatter.parse(bachelorsRequest.getToDate()));
        }catch (ParseException exception){
            throw new DateConvertException(DATE_001.getDefaultMessage(),DATE_001.getCode());
        }
        return bachelorsEntity;
    }

    private BachelorsEntity getEditRequestToEntity(final BachelorsInfoEditRequest bachelorsInfoEditRequest) throws DateConvertException{
        DateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        BachelorsEntity bachelorsEntity = new BachelorsEntity();
        bachelorsEntity.setCollegeName(bachelorsInfoEditRequest.getBachelorsCollegeName());
        bachelorsEntity.setGrade(bachelorsInfoEditRequest.getGrade());
        bachelorsEntity.setSubject(bachelorsInfoEditRequest.getSubject());
        try{
            bachelorsEntity.setFromDate(formatter.parse(bachelorsInfoEditRequest.getFromDate()));
            bachelorsEntity.setToDate(formatter.parse(bachelorsInfoEditRequest.getToDate()));
        }catch (ParseException exception){
            throw new DateConvertException(DATE_001.getDefaultMessage(),DATE_001.getCode());
        }
        return bachelorsEntity;
    }
}
