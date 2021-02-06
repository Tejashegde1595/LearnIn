package com.example.portfolio.api.controller.feed;

import com.example.portfolio.api.model.*;
import com.example.portfolio.service.Business.feed.PostService;
import com.example.portfolio.service.Entity.feed.LikeEntity;
import com.example.portfolio.service.Entity.feed.PostEntity;
import com.example.portfolio.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.example.portfolio.service.common.Constants.*;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @RequestMapping(method = RequestMethod.POST,path = "/create",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PostResponse> createPost(final PostRequest postRequest,final @RequestParam("image") MultipartFile multipartFile, @RequestHeader("authorization") final String authorizationToken) throws AuthenticationFailedException, IOException {
        PostEntity postEntity = requestToEntity(postRequest);
        postEntity = postService.createPost(postEntity,authorizationToken,multipartFile);
        PostResponse postResponse = new PostResponse().id(postEntity.getUuid()).status(POST_CREATION_MESSAGE);
        return new ResponseEntity<PostResponse>(postResponse,HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<PostInfoDetailsResponse>> getPosts(@RequestHeader("authorization") final String authorizationToken) throws AuthenticationFailedException,IOException{
        List<PostEntity> postEntityList = postService.getAllPosts(authorizationToken);
        List<PostInfoDetailsResponse> postInfoDetailsResponses = getResponseList(postEntityList);
        return new ResponseEntity<List<PostInfoDetailsResponse>>(postInfoDetailsResponses,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/all/likes/{postId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<LikeResponse>> getLikesForPosts(@RequestHeader("authorization") final String authorizationToken,@PathVariable("postId") final String postId) throws AuthenticationFailedException,ObjectNotFoundException{
        List<LikeEntity> likeEntityList = postService.getLikesForPosts(authorizationToken,postId);
        List<LikeResponse> likeResponses = getResponseLikeList(likeEntityList);
        return new ResponseEntity<List<LikeResponse>>(likeResponses,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/all/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<PostInfoDetailsResponse>> getPostsByUserId(@RequestHeader("authorization") final String authorizationToken, @PathVariable("userId") final String userId) throws AuthenticationFailedException, UserNotFoundException,IOException {
        List<PostEntity> postEntityList = postService.getAllPostsByUser(authorizationToken,userId);
        List<PostInfoDetailsResponse> postInfoDetailsResponses = getResponseList(postEntityList);
        return new ResponseEntity<List<PostInfoDetailsResponse>>(postInfoDetailsResponses,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE,path = "/delete/{postId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PostInfoDeleteResponse> deletePostsById(@RequestHeader("authorization") final String authorizationToken, @PathVariable("postId") final String postId) throws AuthenticationFailedException, ObjectNotFoundException, AuthorizationFailedException,IOException {
        PostEntity postEntity = postService.deletePost(authorizationToken,postId);
        PostInfoDeleteResponse postInfoDeleteResponse = new PostInfoDeleteResponse().id(postEntity.getUuid()).status(POST_DELETION_MESSAGE);
        return new ResponseEntity<PostInfoDeleteResponse>(postInfoDeleteResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT,path = "/edit/{postId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PostInfoEditResponse> editPostById(@RequestHeader("authorization") final String authorizationToken, @PathVariable("postId") final String postId,final @RequestParam("image") MultipartFile multipartFile,PostInfoEditRequest postInfoEditRequest) throws AuthenticationFailedException, ObjectNotFoundException, AuthorizationFailedException,IOException {
        PostEntity postEntity = new PostEntity();
        postEntity.setContent(postInfoEditRequest.getContent());
        postEntity = postService.editPost(authorizationToken,postId,postEntity,multipartFile);
        PostInfoEditResponse postInfoEditResponse = new PostInfoEditResponse().id(postEntity.getUuid()).status(POST_EDIT_MESSAGE);
        return new ResponseEntity<PostInfoEditResponse>(postInfoEditResponse,HttpStatus.OK);
    }

    private PostEntity requestToEntity(final PostRequest postRequest){
        PostEntity postEntity = new PostEntity();
        postEntity.setUuid(UUID.randomUUID().toString());
        postEntity.setContent(postRequest.getContent());
        postEntity.setLikes(0);
        return postEntity;
    }

    private List<PostInfoDetailsResponse> getResponseList(List<PostEntity> postEntities){
        Iterator<PostEntity> iterator = postEntities.listIterator();
        List<PostInfoDetailsResponse> postInfoDetailsResponses = new ArrayList<PostInfoDetailsResponse>();
        while(iterator.hasNext()){
            PostEntity postEntity = iterator.next();
            PostInfoDetailsResponse postInfoDetailsResponse = new PostInfoDetailsResponse().content(postEntity.getContent()).id(postEntity.getUuid())
                    .likes(postEntity.getLikes().toString()).user(postEntity.getUser().getUserName()).image(postEntity.getImage());
            postInfoDetailsResponses.add(postInfoDetailsResponse);
        }
        return postInfoDetailsResponses;
    }

    private List<LikeResponse> getResponseLikeList(List<LikeEntity> likeEntities){
        Iterator<LikeEntity> iterator = likeEntities.listIterator();
        List<LikeResponse> likeResponses = new ArrayList<LikeResponse>();
        while(iterator.hasNext()){
            LikeEntity likeEntity = iterator.next();
            LikeResponse likeResponse = new LikeResponse().userID(likeEntity.getUserEntity().getUuid()).userName(likeEntity.getUserEntity().getUserName())
                    .postID(likeEntity.getPostEntity().getUuid());
            likeResponses.add(likeResponse);
        }
        return likeResponses;
    }


}
