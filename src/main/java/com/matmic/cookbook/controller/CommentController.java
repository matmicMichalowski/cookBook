package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.util.HttpHeadersUtil;
import com.matmic.cookbook.dto.CommentDTO;
import com.matmic.cookbook.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    public static final String ENTITY_NAME = "comment";
    private final CommentService commentService;

    public CommentController(CommentService commentService) {

        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable Long id){
        CommentDTO comment = commentService.findCommentById(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommentDTO>> getAllComments(){
        return new ResponseEntity<>(commentService.getAllComments(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createNewComment(@RequestBody CommentDTO commentDTO) throws URISyntaxException{
        if (commentDTO.getId() != null){
            return ResponseEntity.badRequest().headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "Given id already exists.")).body(null);
        }
        CommentDTO newComment = commentService.saveOrUpdateComment(commentDTO);
        return ResponseEntity.created(new URI("/api/comment/" + newComment.getId())).headers(HttpHeadersUtil
        .createdEntityAlert(ENTITY_NAME, newComment.getId().toString())).body(newComment);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@RequestBody CommentDTO commentDTO) throws URISyntaxException{
        if (commentDTO.getId() == null){
            return createNewComment(commentDTO);
        }
        CommentDTO updatedComment = commentService.saveOrUpdateComment(commentDTO);
        return ResponseEntity.ok().headers(HttpHeadersUtil.updateEntityAlert(ENTITY_NAME, commentDTO.getId().toString()))
                .body(updatedComment);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.ok().headers(HttpHeadersUtil.deleteEntityAlert(ENTITY_NAME, id.toString())).build();
    }



}
