package com.example.portfolio.api.controller.feed;

import com.example.portfolio.api.model.PostInfoDetailsResponse;
import com.example.portfolio.api.model.PostResponse;
import com.example.portfolio.service.Business.feed.LikeService;
import com.example.portfolio.service.Entity.feed.PostEntity;
import com.example.portfolio.service.exception.AuthenticationFailedException;
import com.example.portfolio.service.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LikeController {
    @Autowired
    private LikeService likeService;

    @RequestMapping(method = RequestMethod.POST,path = "/like/{postId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PostResponse> getPosts(@RequestHeader("authorization") final String authorizationToken, @PathVariable(name = "postId") final String postId) throws AuthenticationFailedException, ObjectNotFoundException {
        PostEntity postEntity = likeService.likePost(authorizationToken,postId);
        PostResponse postResponse = new PostResponse().id(postEntity.getUuid()).status("LIKED");
        return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
    }

}
