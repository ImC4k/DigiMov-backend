package com.team_linne.digimov.service;

import com.team_linne.digimov.exception.GenreNotFoundException;
import com.team_linne.digimov.exception.HouseNotFoundException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    void should_return_empty_array_when_get_all_given_no_houses() {
        //given
        List<House> houseList = new ArrayList<>();
        when(houseRepository.findAll()).thenReturn(houseList);

        //when
        final List<House> actual = houseService.getAll();

        //then
        assertEquals(houseList, actual);
    }

    @Test
    void should_return_specific_house_when_get_by_id_given_list_of_houses_and_valid_house_id() {
        //given
        House house1 = new House("", "house 1", 200);
        House house2 = new House("", "house 2", 300);
        house1.setId("1");
        house2.setId("2");
        List<House> houseList = new ArrayList<>();
        houseList.add(house1);
        houseList.add(house2);
        when(houseRepository.findById("1")).thenReturn(Optional.of(house1));

        //when
        final House actual = houseService.getById("1");

        //then
        assertEquals(house1, actual);
    }

    @Test
    void should_throw_house_not_found_exception_when_get_by_id_given_list_of_houses_and_invalid_house_id() {
        //given
        //when
        //then
        assertThrows(HouseNotFoundException.class, () -> houseService.getById("999"), "House not found");
    }

    @Test
    void should_return_created_house_when_create_house_given_new_house() {
        //given
        House house1 = new House("", "house 1", 200);
        when(houseRepository.save(house1)).thenReturn(house1);

        //when
        final House actual = houseService.create(house1);

        //then
        assertEquals(house1, actual);
    }

    @Test
    void should_return_updated_house_when_update_house_given_house_id_and_updated_house_info() {
        //given
        House house1 = new House("", "house 1", 200);
        House house2 = new House("", "house 2", 300);
        house1.setId("1");
        when(houseRepository.findById("1")).thenReturn(Optional.of(house1));
        when(houseRepository.save(any(House.class))).thenReturn(house2);

        //when
        final House actual = houseService.update("1", house2);

        //then
        assertEquals(house2, actual);
    }

    @Test
    void should_throw_house_not_found_exception_when_update_house_given_invalid_house_id_and_updated_house_info() {
        //given
        House house = new House("", "house 1", 200);
        house.setId("1");

        //when
        //then
        assertThrows(HouseNotFoundException.class, () -> houseService.update("999", house), "House not found");
    }
}
