package com.masterpiece.ToitEnVueBackEnd.controller.housing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.CreateHousingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingDto;
import com.masterpiece.ToitEnVueBackEnd.repository.housing.HousingRepository;
import com.masterpiece.ToitEnVueBackEnd.service.booking.BookingService;
import com.masterpiece.ToitEnVueBackEnd.service.housing.HousingService;
import com.masterpiece.ToitEnVueBackEnd.service.impl.authentication.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.*;


import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class HousingControllerTest {
    @Autowired
    private HousingController housingController;
    @MockBean
    private HousingService housingService;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private HousingRepository housingRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_create_housing_with_files() throws Exception {
        // Given
        CreateHousingDto createHousingDto = new CreateHousingDto();
        createHousingDto.setUser_id(1L);
        createHousingDto.setUsername("testUser");

        String createHousingJson = objectMapper.writeValueAsString(createHousingDto);

        MockMultipartFile file1 = new MockMultipartFile("files", "file1.jpg", "image/jpeg", "file1 content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "file2.jpg", "image/jpeg", "file2 content".getBytes());

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testUser", "toto@yahoo.com", "101214", List.of());


        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);

        HousingDto savedHousing = new HousingDto();
        savedHousing.setHousing_id(1);
        when(housingService.saveHousingWithFiles(eq(createHousingDto), eq(Arrays.asList(file1, file2)))).thenReturn(savedHousing);

        // When

        var response = housingController.createHousing(createHousingJson, List.of(file1, file2));
        // then
        assertEquals(CREATED, response.getStatusCode());

    }
}