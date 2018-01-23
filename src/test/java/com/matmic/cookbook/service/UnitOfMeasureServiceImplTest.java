package com.matmic.cookbook.service;

import com.matmic.cookbook.converter.UnitOfMeasureDtoToUnitOfMeasure;
import com.matmic.cookbook.converter.UnitOfMeasureToUnitOfMeasureDto;
import com.matmic.cookbook.domain.UnitOfMeasure;
import com.matmic.cookbook.dto.UnitOfMeasureDTO;
import com.matmic.cookbook.repository.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UnitOfMeasureServiceImplTest {

    @Mock
    private UnitOfMeasureRepository uomRepository;

    private UnitOfMeasureToUnitOfMeasureDto toUnitOfMeasureDto = new UnitOfMeasureToUnitOfMeasureDto();

    private UnitOfMeasureDtoToUnitOfMeasure toUnitOfMeasure = new UnitOfMeasureDtoToUnitOfMeasure();

    private UnitOfMeasureService unitOfMeasureService;

    private UnitOfMeasure uom;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        unitOfMeasureService = new UnitOfMeasureServiceImpl(uomRepository, toUnitOfMeasure, toUnitOfMeasureDto);

        uom = new UnitOfMeasure();
        uom.setId(1L);
        uom.setName("UoM one");
    }

    @Test
    public void getUomList() throws Exception {
        List<UnitOfMeasure> uomList = new ArrayList<>();
        uomList.add(uom);

        Page<UnitOfMeasure> unitPage = new PageImpl<>(uomList);

        Pageable pageable = PageRequest.of(0, 4);

        when(uomRepository.findAll(pageable)).thenReturn(unitPage);

        Page<UnitOfMeasureDTO> unitsFound = unitOfMeasureService.getUomList(pageable);

        assertNotNull(unitsFound);
        assertEquals(1, unitsFound.getTotalElements());
        verify(uomRepository, times(1)).findAll(pageable);
    }

    @Test
    public void save() throws Exception {
        UnitOfMeasureDTO uomDTO = new UnitOfMeasureDTO();
        uomDTO.setId(uom.getId());
        uomDTO.setName(uom.getName());

        when(uomRepository.save(any())).thenReturn(uom);

        UnitOfMeasureDTO unitSaved = unitOfMeasureService.save(uomDTO);

        assertNotNull(unitSaved);
        assertEquals(uom.getId(), unitSaved.getId());
        assertEquals(uom.getName(), unitSaved.getName());
    }

    @Test
    public void findUnitByName() throws Exception {

        when(uomRepository.findUnitOfMeasureByName(anyString())).thenReturn(Optional.of(uom));

        UnitOfMeasureDTO unitFound = unitOfMeasureService.findUnitByName(anyString());

        assertNotNull(unitFound);
        verify(uomRepository, times(1)).findUnitOfMeasureByName(anyString());
    }

    @Test
    public void findUnitById() throws Exception {

        when(uomRepository.findById(anyLong())).thenReturn(Optional.of(uom));

        UnitOfMeasureDTO unitFound = unitOfMeasureService.findUnitById(anyLong());

        assertNotNull(unitFound);
        verify(uomRepository, times(1)).findById(anyLong());
    }

    @Test
    public void deleteUnit() throws Exception {

        uomRepository.deleteById(anyLong());

        verify(uomRepository, times(1)).deleteById(anyLong());
        verify(uomRepository, never()).deleteAll();
    }

}