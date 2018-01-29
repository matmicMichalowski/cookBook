package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.util.HttpHeadersUtil;
import com.matmic.cookbook.controller.viewmodel.UserVM;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.UserDTO;
import com.matmic.cookbook.repository.UserRepository;
import com.matmic.cookbook.security.SecurityUtil;
import com.matmic.cookbook.service.EmailService;
import com.matmic.cookbook.service.UserService;
import com.matmic.cookbook.service.mail.Mail;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

/**
 * REST controller for managing UserAccount
 */
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

    /**
     * POST /register : register new user account
     *
     * @param userVM managed user View Model
     * @param request Http request
     * @return ResponseEntity with status 201 Created if the user is registered 400 BadRequest if the login or email
     * is in use
     */
    @ApiOperation(value = "Register new User")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created user."),
            @ApiResponse(code = 400, message = "Email or Username is in use.")
    })
    @PostMapping(path = "/register", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity registerNewUserAccount(@Valid @RequestBody UserVM userVM, HttpServletRequest request){
        String applicationUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        HttpHeaders plainHeaders = new HttpHeaders();
        plainHeaders.setContentType(MediaType.TEXT_PLAIN);
        if (!checkPasswordLen(userVM.getPassword())){
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        return userRepository.findOneByEmail(userVM.getEmail().toLowerCase())
                .map(user -> new ResponseEntity<>("email is in use", plainHeaders, HttpStatus.BAD_REQUEST))
                .orElseGet(()-> userRepository.findUserByName(userVM.getName())
                .map(user-> new ResponseEntity<>("username is in use", plainHeaders, HttpStatus.BAD_REQUEST))
                .orElseGet(()->{
                    User user = userService.createUser(userVM.getName(), userVM.getEmail().toLowerCase(), userVM.getPassword());

                    Mail mail = new Mail(user.getEmail(), user.getName(), "Account Activation", "activationEmail",
                            applicationUrl + "/api/activate?token=" + user.getActivationToken());
                    emailService.sendEmailMessage(mail);

                    log.debug("User created with id: {}", user.getId());
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }));
    }

    /**
     * Password length validation
     *
     * @param password password to be checked
     * @return true if password length is valid
     */
    private boolean checkPasswordLen(String password){
        return !password.isEmpty() &&
                password.length() >= 5 &&
                password.length() <= 100;
    }

    /**
     * GET /activate : activate user account
     *
     * @param activationToken the activation token
     * @return ResponseEntity with status 200 OK and body with activated user,
     * or status 500 Internal Server Error if user could not be activated
     */
    @GetMapping("/activate")
    public ResponseEntity<String> userAccountActivation(@RequestParam("token") String activationToken){
        return userService.activateUser(activationToken)
                .map(user -> new ResponseEntity<String>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET /account : get current account
     *
     * @return ResponseEntity with status 200 OK and the body with current userDTO,
     * or status 500 Internal Server Error if user could not be returned
     */
    @GetMapping("/account")
    public ResponseEntity<UserDTO> getAccount(){
        return Optional.ofNullable(userService.getUserWithAuthorities()).map(user -> new ResponseEntity<>(new UserDTO(user), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET /authenticate : check if user is authenticated and return it's login
     *
     * @param request Http request
     * @return login if the user is authenticated
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request){
        return request.getRemoteUser();
    }

    /**
     * POST /account : update and save user
     *
     * @param userDTO userDTO to be updated
     * @return ResponseEntity with status 200 OK,
     * or status 400 Bad Request if new email is in use,
     * or 500 Internal Server Error if the user could not be updated
     */
    @ApiResponse(code = 200, message = "Updated")
    @PutMapping("/account")
    public ResponseEntity updateAndSaveAccount(@Valid @RequestBody UserDTO userDTO){
        final String userLogin = SecurityUtil.getCurrentUser();
        Optional<User> checkIfExists = userRepository.findOneByEmail(userDTO.getEmail());
        if (checkIfExists.isPresent() && (!checkIfExists.get().getName().equalsIgnoreCase(userLogin))){
            return ResponseEntity.badRequest().headers(HttpHeadersUtil.createEntityFailureAlert("user-account", "Email is in use."))
                    .body(null);
        }
        return userRepository.findUserByName(userLogin)
                .map(user -> {
                    userService.updateUser(userDTO);
                    return new ResponseEntity(HttpStatus.OK);
                })
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST /account/change-password : changes the current user's password
     *
     * @param password new password to be saved
     * @return ResponseEntity with status 200 OK,
     * or status 400 Bad Request if the new password in invalid
     */
    @PostMapping(path = "/account/change-password", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity changePassword(@RequestBody String password){
        if (!checkPasswordLen(password)){
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /account/reset-password/request : send email to reset the password
     *
     * @param email user email
     * @param request Http request
     * @return
     */
    @PostMapping(path = "/account/reset-password/request", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity resetPasswordRequest(@RequestBody String email, HttpServletRequest request){
        return userService.resetPasswordRequest(email)
                .map(user -> {
                    String applicationUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                    Mail mail = new Mail(user.getEmail(), user.getName(), "Password Reset Request", "passwordResetEmail",
                            applicationUrl + "/api/account/reset-password/complete?token=" + user.getResetToken());
                    emailService.sendEmailMessage(mail);
                    return new ResponseEntity<>("email sent", HttpStatus.OK);
                }).orElse(new ResponseEntity<>("email address not found", HttpStatus.BAD_REQUEST));
    }

    /**
     * POST /account/reset-password/complete : change user password
     *
     * @param resetToken reset password token from email received by user
     * @param password new password
     * @return ResponseEntity with status 200 OK if password has been reset,
     * or status 400 Bad Request if password is incorrect,
     * or status 500 Internal Server Error if password could not ben saved
     */
    @PostMapping(path = "/account/reset-password/complete")
    public ResponseEntity<String> finishPasswordResetRequest(@RequestParam("token") String resetToken, @RequestBody String password){
        if (!checkPasswordLen((password))){
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        return userService.completeResetPasswordRequest(password, resetToken)
                .map(user -> new ResponseEntity<String>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
