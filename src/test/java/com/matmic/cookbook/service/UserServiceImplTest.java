package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.EvaluationToEvaluationDto;
import com.matmic.cookbook.converter.RecipeToRecipeDto;
import com.matmic.cookbook.converter.UserDtoToUser;
import com.matmic.cookbook.converter.UserToUserDto;
import com.matmic.cookbook.domain.Authority;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.UserDTO;
import com.matmic.cookbook.repository.AuthorityRepository;
import com.matmic.cookbook.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserToUserDto toUserDto;

    @Mock
    private UserDtoToUser toUser;

    @Mock
    private RecipeToRecipeDto toRecipeDto;

    @Mock
    private EvaluationToEvaluationDto toEvaluationDto;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthorityRepository authorityRepository;

    private UserService userService;

    private User user;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Authentication auth = new UsernamePasswordAuthenticationToken("mail@mail.com", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        user = new User();
        user.setId(1L);
        user.setName("TestUser");
        user.setEmail("mail@mail.com");

        passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return "test" + charSequence + "test";
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals("test" + charSequence + "test");
            }
        };

        userService = new UserServiceImpl(userRepository, toUserDto, toUser, toRecipeDto,
                toEvaluationDto, passwordEncoder, authorityRepository);
    }

    @Test
    public void activateUser() throws Exception {
        String testToken = "activationToken";
        user.setActivationToken(testToken);

        when(userRepository.findOneByActivationToken(anyString())).thenReturn(Optional.of(user));

        User userActive = userService.activateUser(testToken).get();

        assertNotNull(userActive);
        assertEquals(true, userActive.isActive());
        assertEquals(null, userActive.getActivationToken());
    }

    @Test
    public void resetPasswordRequest() throws Exception {
        user.setActive(true);

        when(userRepository.findOneByEmail(anyString())).thenReturn(Optional.of(user));

        User userWithRequest = userService.resetPasswordRequest(user.getEmail()).get();

        assertNotNull(userWithRequest);
        assertNotNull(userWithRequest.getResetToken());
    }

    @Test
    public void assertOnlyActiveUserCanRequestForPasswordReset() throws Exception{

        when(userRepository.findOneByEmail(anyString())).thenReturn(Optional.of(user));

        Optional<User> userNotFound = userService.resetPasswordRequest(user.getEmail());

        assertEquals(Optional.empty(), userNotFound);
    }

    @Test
    public void completeResetPasswordRequest() throws Exception {
        String pass = "newPass";
        String resetToken = "resetToken";

        user.setResetToken(resetToken);

        when(userRepository.findOneByResetToken(anyString())).thenReturn(Optional.of(user));

        User userFound = userService.completeResetPasswordRequest(pass, resetToken).get();

        assertNotNull(userFound);
        assertNotNull(userFound.getPassword());
        assertEquals(passwordEncoder.encode(pass), userFound.getPassword());
        assertNull(userFound.getResetToken());
    }

    @Test
    public void changePassword() throws Exception {

        when(userRepository.findUserByName(anyString())).thenReturn(Optional.of(user));

        userService.changePassword("password");

        assertEquals(passwordEncoder.encode("password"), user.getPassword());
    }

    @Test
    public void createUserByUserModel() throws Exception {

        Authority authority = new Authority();
        authority.setName("ADMIN");

        when(userRepository.count()).thenReturn(0L);
        when(authorityRepository.findOneByName(anyString())).thenReturn(authority);
        User createdUser = userService.createUser(user);


        assertNotNull(createdUser);
        assertNotNull(createdUser.getActivationToken());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals("ADMIN", createdUser.getAuthorities().iterator().next().getName());

    }

    @Test
    public void createUserByUserData() throws Exception {
        String name = "Test";
        String email = "test@mail.com";
        String pass = "testPass";

        Authority auth = new Authority();
        auth.setName("USER");

        when(authorityRepository.findOneByName(anyString())).thenReturn(auth);

        User createdUser = userService.createUser(name, email, pass);

        assertNotNull(createdUser);
        assertEquals("USER", createdUser.getAuthorities().iterator().next().getName());
        assertEquals(name, createdUser.getName());
        assertEquals(email, createdUser.getEmail());
        assertEquals(passwordEncoder.encode(pass), createdUser.getPassword());
    }

    @Test
    public void updateUser() throws Exception {
       User userToUpdate = new User();
       userToUpdate.setName("modelName");
       userToUpdate.setId(2L);

        UserDTO userDTO = new UserDTO();
        userDTO.setName("dtoTestName");
        userDTO.setId(2L);
        userDTO.setEmail("good@mail.com");

        User userS = new User();
        userS.setName("dtoTestName");
        userS.setId(2L);
        userS.setEmail("good@mail.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userToUpdate));
        when(userRepository.save(any())).thenReturn(userS);
        when(toUserDto.convert(any())).thenReturn(userDTO);
        UserDTO userUpdated = userService.updateUser(userDTO);

        assertNotNull(userUpdated);
        assertEquals(userDTO.getName(), userUpdated.getName());
        assertEquals(userDTO.getEmail(), userUpdated.getEmail());

    }

    @Test
    public void getAuthorities() throws Exception {
        List<Authority> authorities = new ArrayList<>();

        Authority auth1 = new Authority();
        auth1.setName("ADMIN");
        authorities.add(auth1);

        Authority auth2 = new Authority();
        auth2.setName("USER");
        authorities.add(auth2);

        when(authorityRepository.findAll()).thenReturn(authorities);

        List<String> availableAuthorities = userService.getAuthorities();

        assertNotNull(availableAuthorities);
        assertEquals(2, availableAuthorities.size());
        assertEquals("ADMIN", availableAuthorities.get(0));
    }


    @Test
    public void deleteUser() throws Exception {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).delete(any());
        verify(userRepository, never()).deleteAll();
    }

}