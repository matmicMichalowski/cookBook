package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.util.HttpHeadersUtil;
import com.matmic.cookbook.controller.util.PaginationUtil;
import com.matmic.cookbook.dto.CommentDTO;
import com.matmic.cookbook.security.AuthoritiesConstants;
import com.matmic.cookbook.security.SecurityUtil;
import com.matmic.cookbook.service.CommentService;
import com.matmic.cookbook.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Comment
 */
@RestController
@RequestMapping("/api")
public class CommentController {


    public static final String ENTITY_NAME = "comment";
    private final CommentService commentService;
    private final UserService userService;

    public CommentController(CommentService commentService, UserService userService) {

        this.commentService = commentService;
        this.userService = userService;
    }

    /**
     * GET /:id : get one Comment by id
     *
     * @param id the Comment id
     * @return ResponseEntity with status 200 OK and Comment in body
     */
    @GetMapping("/comment/{id}")
    public ResponseEntity<CommentDTO> getOneComment(@PathVariable Long id){
        CommentDTO comment = commentService.findCommentById(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    /**
     * GET /all : get all comments
     *
     * @param pageable pagination information
     * @return ResponseEntity with status 200 OK and body with list of comments
     */
    @GetMapping("/comments")
    public ResponseEntity<List<CommentDTO>> getAllComments(Pageable pageable){
        Page<CommentDTO> page = commentService.getAllComments(pageable);
        HttpHeaders headers = PaginationUtil.paginationHttpHeader(page, "/api/comments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * POST : create and save new Comment
     *
     * @param commentDTO Comment to be saved
     * @return ResponseEntity with status 200 OK and body with saved commentDTO,
     * or with status 400 BadRequest if commentDTO has already an ID parameter
     * or with status 400 BadRequest if comment cannot be saved
     * @throws URISyntaxException if the Comment Location URI syntax is incorrect
     */
    @PostMapping("/comment")
    public ResponseEntity<CommentDTO> createNewComment(@RequestBody CommentDTO commentDTO) throws URISyntaxException{
        if (commentDTO.getId() != null){
            return ResponseEntity.badRequest().headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "Given id already exists.")).body(null);
        }
        commentDTO.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<CommentDTO> isSaved = commentService.saveNewComment(commentDTO);
        if (!isSaved.isPresent()){
            return ResponseEntity.badRequest().headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "Cannot create comment."))
                    .body(null);
        }
        CommentDTO newComment = isSaved.get();
        return ResponseEntity.created(new URI("/api/comment/" + newComment.getId())).headers(HttpHeadersUtil
        .createdEntityAlert(ENTITY_NAME, newComment.getId().toString())).body(newComment);
    }

    /**
     * PUT  : update Comment with given id
     *
     * @param commentDTO comment to be saved
     * @return the ResponseEntity with status 200 OK and with body the updated commentDTO,
     * or with status 400 Bad Request if the commentDTO is not valid,
     * @throws URISyntaxException if the Comment Location URI syntax is incorrect
     */
    @PutMapping("/comment/update")
    public ResponseEntity<CommentDTO> updateComment(@RequestBody CommentDTO commentDTO) throws URISyntaxException{
        if (commentDTO.getId() == null){
            return createNewComment(commentDTO);
        }

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        if (!commentDTO.getUserName().equals(principal.getName())){
            if (!SecurityUtil.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
                return ResponseEntity.badRequest().headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "User not allowed to edit this comment."))
                        .body(null);
            }
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
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.ok().headers(HttpHeadersUtil.deleteEntityAlert(ENTITY_NAME, id.toString())).build();
    }

}
