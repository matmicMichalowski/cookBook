package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.util.HttpHeadersUtil;
import com.matmic.cookbook.dto.CommentDTO;
import com.matmic.cookbook.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Comment
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final Logger log = LoggerFactory.getLogger(CommentController.class);

    public static final String ENTITY_NAME = "comment";
    private final CommentService commentService;

    public CommentController(CommentService commentService) {

        this.commentService = commentService;
    }

    /**
     * GET /:id : get one Comment by id
     *
     * @param id the Comment id
     * @return ResponseEntity with status 200 OK and Comment in body
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getOneComment(@PathVariable Long id){
        log.debug("REST request to get Comment by id: {}", id);
        CommentDTO comment = commentService.findCommentById(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    /**
     * GET /all : get all comments
     *
     * @return ResponseEntity with status 200 OK and body with list of comments
     */
    @GetMapping("/all")
    public ResponseEntity<List<CommentDTO>> getAllComments(){
        log.debug("REST request to get all comments");
        return new ResponseEntity<>(commentService.getAllComments(), HttpStatus.OK);
    }

    /**
     * POST : create and save new Comment
     *
     * @param commentDTO Comment to be saved
     * @return ResponseEntity with status 200 OK and body with saved commentDTO,
     * or with status 400 BadRequest if commentDTO has already an ID parameter
     * @throws URISyntaxException if the Comment Location URI syntax is incorrect
     */
    @PostMapping
    public ResponseEntity<CommentDTO> createNewComment(@RequestBody CommentDTO commentDTO) throws URISyntaxException{
        if (commentDTO.getId() != null){
            return ResponseEntity.badRequest().headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "Given id already exists.")).body(null);
        }
        CommentDTO newComment = commentService.saveOrUpdateComment(commentDTO);
        return ResponseEntity.created(new URI("/api/comment/" + newComment.getId())).headers(HttpHeadersUtil
        .createdEntityAlert(ENTITY_NAME, newComment.getId().toString())).body(newComment);
    }

    /**
     * PUT /:id : update Comment with given id
     *
     * @param commentDTO comment to be saved
     * @return the ResponseEntity with status 200 OK and with body the updated commentDTO,
     * or with status 400 Bad Request if the commentDTO is not valid,
     * @throws URISyntaxException if the Comment Location URI syntax is incorrect
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@RequestBody CommentDTO commentDTO) throws URISyntaxException{
        if (commentDTO.getId() == null){
            return createNewComment(commentDTO);
        }
        CommentDTO updatedComment = commentService.saveOrUpdateComment(commentDTO);
        return ResponseEntity.ok().headers(HttpHeadersUtil.updateEntityAlert(ENTITY_NAME, commentDTO.getId().toString()))
                .body(updatedComment);
    }

    /**
     * DELETE /:id : delete Comment with given id
     *
     * @param id the id of the Comment to delete
     * @return ResponseEntity with status 200 OK
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.ok().headers(HttpHeadersUtil.deleteEntityAlert(ENTITY_NAME, id.toString())).build();
    }

}
