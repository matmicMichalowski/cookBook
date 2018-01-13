package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.errors.RestResponseEntityExceptionHandler;
import com.matmic.cookbook.converter.EvaluationToEvaluationDto;
import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.repository.EvaluationRepository;
import com.matmic.cookbook.repository.UserRepository;
import com.matmic.cookbook.service.EvaluationService;
import com.matmic.cookbook.service.EvaluationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EvaluationControllerTest {

    @Mock
    private EvaluationRepository evaluationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EvaluationService evaluationService;

    private MockMvc mockMvc;

    private Evaluation evaluation;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);


        evaluationService = new EvaluationServiceImpl(new EvaluationToEvaluationDto(), evaluationRepository, userRepository);

        final EvaluationController controller = new EvaluationController(evaluationService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        evaluation = new Evaluation();
        evaluation.setId(1L);
        evaluation.setUser(new User());
        evaluation.getUser().setId(2L);
        evaluation.setRating(new Rating());
        evaluation.getRating().setId(3L);
        evaluation.setScore(4);
    }

    @Test
    public void getAllEvaluations() throws Exception {

        List<Evaluation> evaluations = new ArrayList<>();
        evaluations.add(evaluation);

        when(evaluationRepository.findAll()).thenReturn(evaluations);

        mockMvc.perform(get("/api/evaluations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].score").value(hasItem(4)));
    }

    @Test
    public void getEvaluationById() throws Exception {

        when(evaluationRepository.findById(anyLong())).thenReturn(Optional.of(evaluation));

        mockMvc.perform(get("/api/evaluation/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.score").value(4));
    }

    @Test
    public void getEvaluationsByUser() throws Exception {
        User user = evaluation.getUser();
        user.getEvaluations().add(evaluation);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/evaluation/user/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].score").value(4));
    }

}