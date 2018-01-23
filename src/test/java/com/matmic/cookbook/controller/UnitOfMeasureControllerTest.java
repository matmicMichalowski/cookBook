package com.matmic.cookbook.controller;

import com.matmic.cookbook.controller.errors.RestResponseEntityExceptionHandler;
import com.matmic.cookbook.domain.UnitOfMeasure;
import com.matmic.cookbook.dto.UnitOfMeasureDTO;
import com.matmic.cookbook.service.UnitOfMeasureService;
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

import java.util.ArrayList;
import java.util.List;

import static com.matmic.cookbook.controller.TestUtil.asJsonBytes;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UnitOfMeasureControllerTest {

    @Mock
    private UnitOfMeasureService uomService;

    private MockMvc mockMvc;


    private UnitOfMeasure unitOfMeasure;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        final UnitOfMeasureController controller = new UnitOfMeasureController(uomService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(1L);
        unitOfMeasure.setName("Cup");
    }

    @Test
    public void getAllUnits() throws Exception {

        List<UnitOfMeasureDTO> units = new ArrayList<>();
        UnitOfMeasureDTO unitOfMeasureDTO = new UnitOfMeasureDTO();
        unitOfMeasureDTO.setId(unitOfMeasure.getId());
        unitOfMeasureDTO.setName(unitOfMeasure.getName());
        units.add(unitOfMeasureDTO);
        Page<UnitOfMeasureDTO> page = new PageImpl<>(units);

        when(uomService.findAllUoms(any())).thenReturn(page);

        mockMvc.perform(get("/api/units?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].name").value(hasItem(unitOfMeasure.getName())));
    }

    @Test
    public void createNewUnitOfMeasure() throws Exception {
        UnitOfMeasureDTO unitOfMeasureDTO = new UnitOfMeasureDTO();
        unitOfMeasureDTO.setName(unitOfMeasure.getName());

        UnitOfMeasureDTO unitOfMeasureDTOCreated = new UnitOfMeasureDTO();
        unitOfMeasureDTOCreated.setId(1L);
        unitOfMeasureDTOCreated.setName(unitOfMeasure.getName());

        when(uomService.save(any())).thenReturn(unitOfMeasureDTOCreated);

        mockMvc.perform(post("/api/unit/new")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(asJsonBytes(unitOfMeasureDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(unitOfMeasure.getName()));
    }

    @Test
    public void createNewUnitOfMeasureUnitHaveId() throws Exception {
        UnitOfMeasureDTO unitOfMeasureDTO = new UnitOfMeasureDTO();
        unitOfMeasureDTO.setId(3L);
        unitOfMeasureDTO.setName(unitOfMeasure.getName());

        when(uomService.save(any())).thenReturn(unitOfMeasureDTO);

        mockMvc.perform(post("/api/unit/new")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(asJsonBytes(unitOfMeasureDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUnitById() throws Exception {
        UnitOfMeasureDTO unitOfMeasureDTO = new UnitOfMeasureDTO();
        unitOfMeasureDTO.setId(unitOfMeasure.getId());
        unitOfMeasureDTO.setName(unitOfMeasure.getName());

        when(uomService.findUnitById(anyLong())).thenReturn(unitOfMeasureDTO);

        mockMvc.perform(get("/api/unit/by-id/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name").value(unitOfMeasure.getName()));
    }

    @Test
    public void getUnitByName() throws Exception {
        UnitOfMeasureDTO unitOfMeasureDTO = new UnitOfMeasureDTO();
        unitOfMeasureDTO.setId(unitOfMeasure.getId());
        unitOfMeasureDTO.setName(unitOfMeasure.getName());

        when(uomService.findUnitByName(anyString())).thenReturn(unitOfMeasureDTO);

        mockMvc.perform(get("/api/unit/by-name/Cup"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name").value(unitOfMeasure.getName()));
    }

    @Test
    public void deleteUnitOfMeasureById() throws Exception {

        mockMvc.perform(delete("/api/unit/1")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }

}