package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.errors.RestResponseEntityExceptionHandler;
import com.matmic.cookbook.controller.viewmodel.UserVM;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.RecipeDTO;
import com.matmic.cookbook.dto.UserDTO;
import com.matmic.cookbook.repository.UserRepository;
import com.matmic.cookbook.service.EmailService;
import com.matmic.cookbook.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

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

        final UserController controller = new UserController(userService, emailService, userRepository);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setPassword("password");
        user.setEmail("mail@mail.com");
        user.setActive(true);
    }

    @Test
    public void createUser() throws Exception {
        Set<String> authorities = new HashSet<>();
        String testAuth = "testUserAuth";
        authorities.add(testAuth);

        UserVM userVM = new UserVM(
                null, "TestName", "TestPass", "test@mail.com", authorities
        );
        userVM.setEmail("mail@mail.com");
        userVM.setName("NameTest");


        when(userRepository.findOneByEmail(anyString())).thenReturn(Optional.of(user));
        when(userService.createUser(any())).thenReturn(user);

        mockMvc.perform(post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(TestUtil.asJsonBytes(userVM)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateUser() throws Exception {
        Set<String> authorities = new HashSet<>();
        String testAuth = "testUserAuth";
        authorities.add(testAuth);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());


        when(userRepository.findOneByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.findUserByName(anyString())).thenReturn(Optional.of(user));
        when(userService.updateUser(any())).thenReturn(userDTO);

        mockMvc.perform(put("/api/user")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(TestUtil.asJsonBytes(userDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllUsers() throws Exception {
        List<UserDTO> users = new ArrayList<>();

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        users.add(userDTO);

        Page<UserDTO> userDTOPage = new PageImpl<>(users);

        when(userService.findAllUsers(any())).thenReturn(userDTOPage);

        mockMvc.perform(get("/api/users?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].name").value(hasItem(userDTO.getName())));
    }

    @Test
    public void findUserRecipes() throws Exception {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setName("recipeTest");
        recipeDTO.setId(1L);
        recipeDTO.setUserId(3L);

        List<RecipeDTO> recipeList = new ArrayList<>();
        recipeList.add(recipeDTO);

        when(userService.findUserRecipes(anyLong())).thenReturn(recipeList);

        mockMvc.perform(get("/api/user/3/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].name").value(hasItem(recipeDTO.getName())));
    }

    @Test
    public void getUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());

        when(userService.findUserDTOByID(anyLong())).thenReturn(userDTO);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    public void getAuthorities() throws Exception {
        String auth = "ADMIN";
        String auth2 = "USER";

        List<String> authorities = new ArrayList<>();
        authorities.add(auth);
        authorities.add(auth2);

        when(userService.getAuthorities()).thenReturn(authorities);

        mockMvc.perform(get("/api/user/authorities")
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(containsInAnyOrder("ADMIN", "USER")));
    }

    @Test
    public void deleteUser() throws Exception {

        mockMvc.perform(delete("/api/user/2")
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }

}