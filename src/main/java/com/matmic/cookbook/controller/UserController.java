package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.util.HttpHeadersUtil;
import com.matmic.cookbook.controller.util.PaginationUtil;
import com.matmic.cookbook.controller.viewmodel.UserVM;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.dto.UserDTO;
import com.matmic.cookbook.repository.UserRepository;
import com.matmic.cookbook.service.EmailService;
import com.matmic.cookbook.service.UserService;
import com.matmic.cookbook.service.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing User
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private static final String ENTITY_NAME = "user";
    private final UserService userService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public UserController(UserService userService, EmailService emailService, UserRepository userRepository) {
        this.userService = userService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @RequestMapping("/ok")
    public String mailTest(HttpServletRequest request){
        String applicationUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        User user = new User();
        user.setEmail("trajanesco@gmail.com");
        user.setName("newName");
        user.setActivationToken(UUID.randomUUID().toString());

        Mail mail = new Mail(user.getEmail(), user.getName(), "Testing", "passwordResetEmail",
                applicationUrl + "/activate?token=" + user.getActivationToken());
        emailService.sendEmailMessage(mail);
        return "ok";
    }

    /**
     * POST /user : create new User
     * @param userVM user view model
     * @param request Http Request
     * @return the ResponseEntity with status 201 Created and with body the new user, or with status 400 Bad Request
     * if the login or email is already in use
     * @throws URISyntaxException if the User Location URI syntax is incorrect
     */
    @PostMapping("/user")
    public ResponseEntity createUser(@Valid @RequestBody UserVM userVM, HttpServletRequest request) throws URISyntaxException{
        log.debug("REST request to save new User: {}", userVM);

        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        if (userVM.getId() != null){
            return ResponseEntity.badRequest()
                    .headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "User already have ID cannot be created."))
                    .body(null);
        }else if (userRepository.findByEmailOrName(userVM.getEmail(), userVM.getName().toLowerCase()).isPresent()){
            return ResponseEntity.badRequest()
                    .headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "User with this name or email exists"))
                    .body(null);
        }else{
            User newUser = userService.createUser(userVM);
            String applicationUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            Mail mail = new Mail(newUser.getEmail(), newUser.getName(), "Account Activation", "activationEmail",
                    applicationUrl + "/api/activate?token=" + newUser.getActivationToken());
            emailService.sendEmailMessage(mail);
            return ResponseEntity.created(new URI("/api/user/" + newUser.getName()))
                    .headers(HttpHeadersUtil.createdEntityAlert(ENTITY_NAME, newUser.getName()))
                    .body(newUser);
        }
    }

    /**
     * PUT  /user : Updates an existing User
     *
     * @param userDTO the user to update
     * @return the ResponseEntity with status 200 OK and with body the updated user,
     * or with status 400 Bad Request if the login or email is already in use,
     * or with status 500 Internal Server Error if the user couldn't be updated
     */
    @PutMapping("/user")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO){
        log.debug("REST request to update User: {}", userDTO);
        Optional<User> isExisting = userRepository.findOneByEmail(userDTO.getEmail());
        if (isExisting.isPresent() && (!isExisting.get().getId().equals(userDTO.getId()))){
            return ResponseEntity.badRequest().headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "Email is in use"))
                    .body(null);
        }
        isExisting = userRepository.findUserByName(userDTO.getName().toLowerCase());
        if (isExisting.isPresent() && (!isExisting.get().getId().equals(userDTO.getId()))){
            return ResponseEntity.badRequest().headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "Username is in use"))
                    .body(null);
        }
        UserDTO updatedUser = userService.updateUser(userDTO);
        return ResponseEntity.ok().headers(HttpHeadersUtil.updateEntityAlert(ENTITY_NAME, updatedUser.getId().toString()))
                .body(updatedUser);
    }

    /**
     * GET  /users : get all users
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 OK and with body all users
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable){
        log.debug("REST request for all Users");
        Page<UserDTO> page  = userService.findAllUsers(pageable);
        HttpHeaders headers = PaginationUtil.paginationHttpHeader(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /user/:id/recipes : get all user recipes
     *
     * @param id the id of the user
     * @return list of user's recipe list
     */
    @GetMapping("/user/{id}/recipes")
    public ResponseEntity<List<RecipeDTO>> findUserRecipes(@PathVariable String id){
        log.debug("REST request to get user recipe list : {}", id);
        return new ResponseEntity<>(userService.findUserRecipes(Long.valueOf(id)), HttpStatus.OK);
    }

    /**
     * GET  /users/:id : get the user by id
     *
     * @param id the id of the user to find
     * @return the ResponseEntity with status 200 OK and with body the userDTO
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        log.debug("REST request to get User: {}", id);
        return ResponseEntity.ok(userService.findUserDTOByID(id));
    }

    /**
     * GET /user/authentication : get all authorities
     *
     * @return list of string of all the roles
     */
    @GetMapping("/user/authorities")
    @Secured("ADMIN")
    public List<String> getAuthorities(){
        return userService.getAuthorities();
    }


    /**
     * DELETE /user/:id : delete User with given id
     *
     * @param id the id of the User to delete
     * @return ResponseEntity with status 200 OK
     */
    @DeleteMapping("/user/{id}")
    //@Secured("ADMIN")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        log.debug("REST request to delete User: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok().headers(HttpHeadersUtil.deleteEntityAlert(ENTITY_NAME, id.toString())).build();
    }



}
