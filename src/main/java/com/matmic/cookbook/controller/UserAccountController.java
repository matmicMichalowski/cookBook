package com.matmic.cookbook.controller;

import com.matmic.cookbook.repository.UserRepository;
import com.matmic.cookbook.service.EmailService;
import com.matmic.cookbook.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserAccountController {

    private final Logger log = LoggerFactory.getLogger(UserAccountController.class);

    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;

    public UserAccountController(UserRepository userRepository, UserService userService, EmailService emailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping(path = "/register", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity registerNewUserAccount(@Valid @RequestBody )

}
