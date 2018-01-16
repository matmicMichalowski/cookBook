package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.errors.RestResponseEntityExceptionHandler;
import com.matmic.cookbook.controller.viewmodel.ResetTokenAndPasswordVM;
import com.matmic.cookbook.controller.viewmodel.UserVM;
import com.matmic.cookbook.domain.Authority;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.UserDTO;
import com.matmic.cookbook.repository.UserRepository;
import com.matmic.cookbook.security.AuthoritiesConstants;
import com.matmic.cookbook.service.EmailService;
import com.matmic.cookbook.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserAccountControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    private User user;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        final UserAccountController controller = new UserAccountController(userRepository, userService, emailService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken("mail@mail.com", null);

        SecurityContextHolder.getContext().setAuthentication(auth);

        user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setPassword("password");
        user.setEmail("mail@mail.com");
        user.setActive(true);

    }

    @Test
    public void registerNewUserAccount() throws Exception {
        UserVM userVM = new UserVM(
                null, "TomTest", "password", "email@test.com",
                new HashSet<>(Collections.singletonList(AuthoritiesConstants.USER))
        );

        User user = new User();
        user.setName(userVM.getName());
        user.setPassword(userVM.getPassword());
        user.setEmail(userVM.getEmail());
        Authority auth = new Authority();
        auth.setName(AuthoritiesConstants.USER);
        user.getAuthorities().add(auth);

        when(userService.createUser(anyString(), anyString(), anyString())).thenReturn(user);
        when(userRepository.findOneByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findUserByName(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/register")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(TestUtil.asJsonBytes(userVM)))
                .andExpect(status().isCreated());

    }

    @Test
    public void userAccountActivation() throws Exception {
        final String activationToken = "testToken";

        User user = new User();
        user.setId(1L);
        user.setActivationToken(activationToken);

        when(userService.activateUser(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/activate?token={activationToken}", activationToken))
                .andExpect(status().isOk());

    }

    @Test
    public void getAccount() throws Exception {

        when(userService.getUserWithAuthorities()).thenReturn(user);
        when(userRepository.findOneWithAuthoritiesByName(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/account")
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    public void updateAndSaveAccount() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Test");
        userDTO.setEmail("mail@mail.com");
        userDTO.setActive(true);


        when(userRepository.findOneByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findUserByName(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/account")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(TestUtil.asJsonBytes(userDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void changePassword() throws Exception {

        when(userRepository.findUserByName(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/account/change-password").content("betterPassword"))
                .andExpect(status().isOk());
    }

    @Test
    public void resetPasswordRequest() throws Exception {

        when(userService.resetPasswordRequest(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/account/reset-password/request")
                    .content("mail@mail.com"))
                .andExpect(status().isOk());
    }

    @Test
    public void finishPasswordResetRequest() throws Exception {
        ResetTokenAndPasswordVM testResetVM = new ResetTokenAndPasswordVM();
        testResetVM.setNewPassword("newPass");
        testResetVM.setResetToken("newToken");

        when(userService.completeResetPasswordRequest(anyString(), anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/account/reset-password/complete")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(TestUtil.asJsonBytes(testResetVM)))
                .andExpect(status().isOk());
    }

    @Test
    public void finishPasswordResetRequestInvalidPassword() throws Exception {
        ResetTokenAndPasswordVM testResetVM = new ResetTokenAndPasswordVM();
        testResetVM.setNewPassword("pass");
        testResetVM.setResetToken("newToken");

        when(userService.completeResetPasswordRequest(anyString(), anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/account/reset-password/complete")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(TestUtil.asJsonBytes(testResetVM)))
                .andExpect(status().isBadRequest());
    }

}