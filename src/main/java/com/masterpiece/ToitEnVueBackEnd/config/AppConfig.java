package com.masterpiece.ToitEnVueBackEnd.config;

import com.masterpiece.ToitEnVueBackEnd.dto.booking.BookingDetailsDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingDto;
import com.masterpiece.ToitEnVueBackEnd.model.booking.Booking;
import com.masterpiece.ToitEnVueBackEnd.model.housing.Housing;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        PropertyMap<Housing, HousingDto> housingMapping = new PropertyMap<Housing, HousingDto>() {
            @Override
            protected void configure() {
                map().setUser_id(source.getUser().getId());
                map().setUsername(source.getUser().getUsername());
            }
        };
        modelMapper.addMappings(housingMapping);

        PropertyMap<Booking, BookingDetailsDto> bookingMaping = new PropertyMap<Booking, BookingDetailsDto>() {
            @Override
            protected void configure() {
                map().setPrice(source.getHousing().getPrice());
                map().setHousingId(source.getHousing().getHousing_id());
            }
        };
        modelMapper.addMappings(bookingMaping);

        return modelMapper;
    }
}