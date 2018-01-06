package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.util.HttpHeadersUtil;
import com.matmic.cookbook.controller.util.PaginationUtil;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.UserDTO;
import com.matmic.cookbook.repository.UserRepository;
import com.matmic.cookbook.service.EmailService;
import com.matmic.cookbook.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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

    @PostMapping("/user")
    public ResponseEntity createUser(@Valid @RequestBody User user) throws URISyntaxException{
        log.debug("REST request to save new User: {}", user);

        if (user.getId() != null){
            return ResponseEntity.badRequest()
                    .headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "User already have ID cannot be created."))
                    .body(null);
        }else if (userRepository.findByEmailOrName(user.getEmail(), user.getName().toLowerCase()).isPresent()){
            return ResponseEntity.badRequest()
                    .headers(HttpHeadersUtil.createEntityFailureAlert(ENTITY_NAME, "User with this name or email exists"))
                    .body(null);
        }else{
            User newUser = userService.createUser(user);
            emailService.activationEmail(newUser);
            return ResponseEntity.created(new URI("/api/user/" + newUser.getName()))
                    .headers(HttpHeadersUtil.createdEntityAlert(ENTITY_NAME, newUser.getName()))
                    .body(newUser);
        }
    }

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

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable){
        log.debug("REST request for all Users");
        Page<UserDTO> page  = userService.findAllUsers(pageable);
        HttpHeaders headers = PaginationUtil.paginationHttpHeader(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/user/{login}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        log.debug("REST request to get User: {}", id);
        return ResponseEntity.ok(userService.findUserDTOByID(id));
    }

    @GetMapping("/user/authorities")
    @Secured("ADMIN")
    public List<String> getAuthorities(){
        return userService.getAuthorities();
    }

    @DeleteMapping("/user/{id}")
    @Secured("ADMIN")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        log.debug("REST request to delete User: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok().headers(HttpHeadersUtil.deleteEntityAlert(ENTITY_NAME, id.toString())).build();
    }



}
