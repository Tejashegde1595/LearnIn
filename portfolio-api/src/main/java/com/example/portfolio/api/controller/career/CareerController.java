package com.example.portfolio.api.controller.career;

import com.example.portfolio.api.model.*;
import com.example.portfolio.service.Business.career.CareerService;
import com.example.portfolio.service.Business.education.BachelorService;
import com.example.portfolio.service.Entity.career.CareerEntity;
import com.example.portfolio.service.Entity.education.BachelorsEntity;
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

import static com.example.portfolio.service.common.Constants.CAREER_CREATION_MESSAGE;
import static com.example.portfolio.service.common.Constants.COLLEGE_CREATION_MESSAGE;
import static com.example.portfolio.service.common.GenericErrorCode.DATE_001;

@RestController
@RequestMapping("/career")
public class CareerController {

    @Autowired
    private CareerService careerService;

    @RequestMapping(method = RequestMethod.POST,path = "/career/create",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<BachelorsResponse> creatCareers(final CareerRequest careerRequest, @RequestHeader("authorization") final String authorizationToken) throws DateConvertException, AuthenticationFailedException {
        CareerEntity careerEntity = requestToEntity(careerRequest);
        careerEntity = careerService.createCareer(careerEntity,authorizationToken);
        BachelorsResponse bachelorsResponse = new BachelorsResponse().id(careerEntity.getUuid()).status(CAREER_CREATION_MESSAGE);
        return new ResponseEntity<BachelorsResponse>(bachelorsResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/career/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<CareerInfoDetailsResponse>> getAllCareersInfo(@RequestHeader("authorization") final String authorizationToken) throws AuthenticationFailedException{
        List<CareerEntity> careerEntityList = careerService.getAllCareersInfo(authorizationToken);
        List<CareerInfoDetailsResponse> careerInfoDetailsResponses = entityToResponseList(careerEntityList);
        return new ResponseEntity<List<CareerInfoDetailsResponse>>(careerInfoDetailsResponses,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/career/all/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<CareerInfoDetailsResponse>> getCareersInfoByUser(@RequestHeader("authorization") final String authorizationToken,@PathVariable(name="userId") final String userId) throws AuthenticationFailedException, UserNotFoundException {
        List<CareerEntity> careerEntityList = careerService.getAllCareersByUserId(authorizationToken,userId);
        List<CareerInfoDetailsResponse> careerInfoDetailsResponses = entityToResponseList(careerEntityList);
        return new ResponseEntity<List<CareerInfoDetailsResponse>>(careerInfoDetailsResponses,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE,path = "/career/delete/{careerId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CareerInfoDeleteResponse> deleteCareersInfo(@RequestHeader("authorization") final String authorizationToken,@PathVariable(name="careerId") final String careerId) throws AuthenticationFailedException, ObjectNotFoundException, AuthorizationFailedException {
        CareerEntity careerEntity = careerService.deleteCareers(authorizationToken,careerId);
        CareerInfoDeleteResponse careerInfoDeleteResponse = new CareerInfoDeleteResponse().id(careerEntity.getUuid()).status("SUCCESSFULLY DELETED THE CAREER INFO");
        return new ResponseEntity<CareerInfoDeleteResponse>(careerInfoDeleteResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT,path = "/career/edit/{careerId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CareerInfoEditResponse> editCareersInfo(@RequestHeader("authorization") final String authorizationToken, @PathVariable(name="careerId") final String careerId, CareerInfoEditRequest careerInfoEditRequest) throws AuthenticationFailedException, ObjectNotFoundException, AuthorizationFailedException,DateConvertException {
        CareerEntity careerEntity = getEditRequestToEntity(careerInfoEditRequest);
        CareerEntity careerEntity1 = careerService.editCareers(authorizationToken,careerId,careerEntity);
        CareerInfoEditResponse careerInfoEditResponse = new CareerInfoEditResponse().id(careerEntity1.getUuid()).status("SUCCESSFULLY EDITED THE CAREER INFO");
        return new ResponseEntity<CareerInfoEditResponse>(careerInfoEditResponse,HttpStatus.OK);
    }

    private CareerEntity requestToEntity(final CareerRequest careerRequest) throws DateConvertException {
        DateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        CareerEntity careerEntity = new CareerEntity();
        careerEntity.setUuid(UUID.randomUUID().toString());
        careerEntity.setCompanyName(careerRequest.getCompanyName());
        careerEntity.setDesignation(careerRequest.getDesignation());
        try {
            careerEntity.setFromDate(formatter.parse(careerRequest.getFromDate()));
            careerEntity.setToDate(formatter.parse(careerRequest.getToDate()));
        }catch (ParseException exception){
            throw new DateConvertException(DATE_001.getDefaultMessage(),DATE_001.getCode());
        }
        return careerEntity;
    }

    private List<CareerInfoDetailsResponse> entityToResponseList(final List<CareerEntity> careerEntityList){
        Iterator<CareerEntity> iterator = careerEntityList.listIterator();
        List<CareerInfoDetailsResponse> careerInfoDetailsResponses = new ArrayList<CareerInfoDetailsResponse>();
        while(iterator.hasNext()){
            CareerEntity careerEntity = iterator.next();
            CareerInfoDetailsResponse careerInfoDetailsResponse = new CareerInfoDetailsResponse().companyName(careerEntity.getCompanyName()).fromDate(careerEntity.getFromDate().toString()).
                    toDate(careerEntity.getToDate().toString()).designation(careerEntity.getDesignation()).id(careerEntity.getUuid());
            careerInfoDetailsResponses.add(careerInfoDetailsResponse);
        }
        return careerInfoDetailsResponses;
    }

    private CareerEntity getEditRequestToEntity(final CareerInfoEditRequest careerInfoEditRequest) throws DateConvertException{
        DateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
        CareerEntity careerEntity = new CareerEntity();
        careerEntity.setCompanyName(careerInfoEditRequest.getCompanyName());
        careerEntity.setDesignation(careerInfoEditRequest.getDesignation());
        try{
            careerEntity.setFromDate(formatter.parse(careerInfoEditRequest.getFromDate()));
            careerEntity.setToDate(formatter.parse(careerInfoEditRequest.getToDate()));
        }catch (ParseException exception){
            throw new DateConvertException(DATE_001.getDefaultMessage(),DATE_001.getCode());
        }
        return careerEntity;
    }

}
