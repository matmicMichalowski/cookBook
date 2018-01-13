package com.matmic.cookbook.service;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class EvaluationServiceImplTest {


    private EvaluationToEvaluationDto toEvaluationDto = new EvaluationToEvaluationDto();

    @Mock
    private EvaluationRepository evaluationRepository;


    @Mock
    private UserRepository userRepository;

    private EvaluationService evaluationService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        evaluationService = new EvaluationServiceImpl(toEvaluationDto, evaluationRepository, userRepository);
    }

    @Test
    public void getEvaluations() throws Exception {
        Evaluation ev = new Evaluation();
        ev.setRating(new Rating());
        ev.setUser(new User());
        List<Evaluation> evaluations = new ArrayList<>();
        evaluations.add(ev);

        when(evaluationRepository.findAll()).thenReturn(evaluations);

        List<EvaluationDTO> evaluationDTOList = evaluationService.getEvaluations();

        assertNotNull(evaluationDTOList);
        assertEquals(1, evaluationDTOList.size());

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

//    @Test
//    public void saveEvaluation() throws Exception {
//        EvaluationDTO evaluationDTO = new EvaluationDTO();
//        evaluationDTO.setId(1L);
//        evaluationDTO.setUserId(3L);
//
//        EvaluationDTO evDTO = new EvaluationDTO();
//        evDTO.setId(2L);
//        evDTO.setUserId(4L);
//
//
//
//        RatingDTO ratingDTO = new RatingDTO();
//        ratingDTO.getUsersEvaluations().add(evaluationDTO);
//        ratingDTO.getUsersEvaluations().add(evDTO);
//
//
//        when(ratingService.saveAndUpdateRating(any())).thenReturn(ratingDTO);
//
//        EvaluationDTO evSaved = evaluationService.saveEvaluation(evaluationDTO);
//
//        assertNotNull(evSaved);
//        verify(ratingService, times(1)).saveAndUpdateRating(any());
//    }


}