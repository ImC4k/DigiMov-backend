package com.team_linne.digimov.service;

import com.team_linne.digimov.model.Genre;
import com.team_linne.digimov.model.House;
import com.team_linne.digimov.repository.HouseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HouseServiceTest {
    @InjectMocks
    private HouseService houseService;
    @Mock
    private HouseRepository houseRepository;

    @Test
    void should_return_all_houses_when_get_all_given_list_of_houses() {
        //given
        House house1 = new House("", "house 1", 200);
        House house2 = new House("", "house 2", 300);
        List<House> houseList = new ArrayList<>();
        houseList.add(house1);
        houseList.add(house2);
        when(houseRepository.findAll()).thenReturn(houseList);

        //when
        final List<House> actual = houseService.getAll();

        //then
        assertEquals(houseList, actual);
    }
}
