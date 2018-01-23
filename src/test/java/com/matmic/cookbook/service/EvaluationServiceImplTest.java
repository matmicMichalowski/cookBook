package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.EvaluationDtoToEvaluation;
import com.matmic.cookbook.converter.EvaluationToEvaluationDto;
import com.matmic.cookbook.domain.Evaluation;
import com.matmic.cookbook.domain.Rating;
import com.matmic.cookbook.domain.User;
import com.matmic.cookbook.dto.EvaluationDTO;
import com.matmic.cookbook.repository.EvaluationRepository;
import com.matmic.cookbook.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class EvaluationServiceImplTest {


    private EvaluationToEvaluationDto toEvaluationDto = new EvaluationToEvaluationDto();

    private EvaluationDtoToEvaluation toEvaluation = new EvaluationDtoToEvaluation();

    @Mock
    private EvaluationRepository evaluationRepository;


    @Mock
    private UserRepository userRepository;

    private EvaluationService evaluationService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        evaluationService = new EvaluationServiceImpl(toEvaluationDto, toEvaluation, evaluationRepository, userRepository);
    }

    @Test
    public void getEvaluations() throws Exception {
        Evaluation ev = new Evaluation();
        ev.setRating(new Rating());
        ev.setUser(new User());
        List<Evaluation> evaluations = new ArrayList<>();
        evaluations.add(ev);

        when(evaluationRepository.findAll()).thenReturn(evaluations);

        Page<EvaluationDTO> evaluationDTOList = evaluationService.getEvaluations(any());

        assertNotNull(evaluationDTOList);
        assertEquals(1, evaluationDTOList.getTotalElements());

    }

    @Test
    public void findEvaluationById() throws Exception {
        Evaluation ev = new Evaluation();
        ev.setId(1L);
        ev.setRating(new Rating());
        ev.setUser(new User());

        EvaluationDTO evDto = new EvaluationDTO();
        evDto.setId(1L);

        when(evaluationRepository.findById(anyLong())).thenReturn(Optional.of(ev));

        EvaluationDTO dto = evaluationService.findEvaluationById(1L);

        assertNotNull(dto);
        verify(evaluationRepository, times(1)).findById(anyLong());
    }

    @Test
    public void saveEvaluation() throws Exception {
        Evaluation evaluation = new Evaluation();
        evaluation.setId(1L);
        evaluation.setUser(new User());
        evaluation.getUser().setId(3L);
        evaluation.setRating(new Rating());
        evaluation.getRating().setId(5L);
        evaluation.setScore(4);

        when(evaluationRepository.save(any())).thenReturn(evaluation);

        EvaluationDTO evSaved = evaluationService.saveNewEvaluation(any());

        assertNotNull(evSaved);
        assertEquals(evaluation.getUser().getId(), evSaved.getUserId());
        assertEquals(evaluation.getRating().getId(), evSaved.getRatingId());
        assertEquals(evaluation.getScore(), evSaved.getScore());
        verify(evaluationRepository, times(1)).save(any());
    }

    @Test
    public void updateEvaluation() throws Exception {
        Evaluation evaluation = new Evaluation();
        evaluation.setId(1L);
        evaluation.setUser(new User());
        evaluation.getUser().setId(3L);
        evaluation.setRating(new Rating());
        evaluation.getRating().setId(5L);
        evaluation.setScore(4);

        EvaluationDTO evaluationDTO = new EvaluationDTO();
        evaluationDTO.setId(evaluation.getId());
        evaluationDTO.setRatingId(evaluation.getRating().getId());
        evaluationDTO.setUserId(evaluation.getUser().getId());
        evaluationDTO.setScore(2);

        when(evaluationRepository.findById(anyLong())).thenReturn(Optional.of(evaluation));
        when(evaluationRepository.save(any())).thenReturn(evaluation);

        EvaluationDTO updatedEvaluation = evaluationService.updateEvaluation(evaluationDTO);

        assertNotNull(updatedEvaluation);
        assertEquals(evaluation.getScore(), updatedEvaluation.getScore());
    }

}