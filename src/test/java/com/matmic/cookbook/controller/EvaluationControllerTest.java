package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.errors.RestResponseEntityExceptionHandler;
import com.matmic.cookbook.converter.EvaluationDtoToEvaluation;
import com.matmic.cookbook.converter.EvaluationToEvaluationDto;
import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.repository.EvaluationRepository;
import com.matmic.cookbook.repository.UserRepository;
import com.matmic.cookbook.service.EvaluationService;
import com.matmic.cookbook.service.EvaluationServiceImpl;
import com.matmic.cookbook.service.RatingService;
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

import static com.matmic.cookbook.controller.TestUtil.asJsonBytes;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EvaluationControllerTest {


    @Mock
    private EvaluationRepository evaluationRepository;

    private EvaluationToEvaluationDto toEvaluationDto = new EvaluationToEvaluationDto();

    private EvaluationDtoToEvaluation toEvaluation = new EvaluationDtoToEvaluation();

    @Mock
    private UserRepository userRepository;

    @Mock
    private RatingService ratingService;

    @Mock
    private EvaluationService evaluationService;

    private MockMvc mockMvc;

    private Evaluation evaluation;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        evaluationService = new EvaluationServiceImpl(toEvaluationDto, toEvaluation, evaluationRepository, userRepository);

        final EvaluationController controller = new EvaluationController(evaluationService, ratingService);

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
    public void saveNewEvaluation() throws Exception {
        EvaluationDTO evaluationDTO = new EvaluationDTO();
        evaluationDTO.setUserId(evaluation.getUser().getId());
        evaluationDTO.setScore(evaluation.getScore());
        evaluationDTO.setRatingId(evaluation.getRating().getId());
        evaluationDTO.setScore(evaluation.getScore());

        EvaluationDTO evaluationDTOSaved = new EvaluationDTO();
        evaluationDTOSaved.setId(1L);
        evaluationDTOSaved.setUserId(evaluation.getUser().getId());
        evaluationDTOSaved.setScore(evaluation.getScore());
        evaluationDTOSaved.setRatingId(evaluation.getRating().getId());


        Evaluation evaluation = toEvaluation.convert(evaluationDTOSaved);


        when(evaluationRepository.save(any())).thenReturn(evaluation);


        mockMvc.perform(post("/api/evaluation")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(asJsonBytes(evaluationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.score").value(evaluation.getScore()))
                .andExpect(jsonPath("$.userId").value(evaluation.getUser().getId()))
                .andExpect(jsonPath("$.ratingId").value(evaluation.getRating().getId()));
    }

    @Test
    public void updateEvaluation() throws Exception {
        EvaluationDTO evaluationDTOUpdated = new EvaluationDTO();
        evaluationDTOUpdated.setId(1L);
        evaluationDTOUpdated.setUserId(evaluation.getUser().getId());
        evaluationDTOUpdated.setScore(2);
        evaluationDTOUpdated.setRatingId(evaluation.getRating().getId());

        Evaluation evaluation = toEvaluation.convert(evaluationDTOUpdated);

        when(evaluationRepository.findById(anyLong())).thenReturn(Optional.of(evaluation));
        when(evaluationRepository.save(any())).thenReturn(evaluation);

        mockMvc.perform(put("/api/evaluation")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(asJsonBytes(evaluationDTOUpdated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(evaluation.getId()))
                .andExpect(jsonPath("$.score").value(evaluation.getScore()))
                .andExpect(jsonPath("$.userId").value(evaluation.getUser().getId()))
                .andExpect(jsonPath("$.ratingId").value(evaluation.getRating().getId()));
    }


    @Test
    public void updateEvaluationFailure() throws Exception {
        EvaluationDTO evaluationDTO = new EvaluationDTO();
        evaluationDTO.setId(1L);
        evaluationDTO.setUserId(evaluation.getUser().getId());
        evaluationDTO.setScore(2);
        evaluationDTO.setRatingId(evaluation.getRating().getId());

        Evaluation evaluation = toEvaluation.convert(evaluationDTO);
        evaluation.getUser().setId(5L);

        when(evaluationRepository.findById(anyLong())).thenReturn(Optional.of(evaluation));


        mockMvc.perform(put("/api/evaluation")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(asJsonBytes(evaluationDTO)))
                .andExpect(status().isBadRequest());
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