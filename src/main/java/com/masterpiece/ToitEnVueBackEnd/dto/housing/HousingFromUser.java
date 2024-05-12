package com.masterpiece.ToitEnVueBackEnd.dto.housing;

import com.masterpiece.ToitEnVueBackEnd.dto.file.FileDto;
import com.masterpiece.ToitEnVueBackEnd.model.booking.BookingDate;
import com.masterpiece.ToitEnVueBackEnd.model.housing.CategoryEnum;
import com.masterpiece.ToitEnVueBackEnd.model.housing.HousingConditionEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class HousingFromUser {
    private int housing_id;
    private String title;
    private String address;
    private String city;
    private int zipcode;
    private String description;
    private double price;
    private CategoryEnum category;
    private int rooms;
    private int bedrooms;
    private int bathrooms;
    private boolean furnished;
    private int living_space;
    private Set<String> highlights;
    private int year_of_construction;
    private HousingConditionEnum housingCondition;
    private Long user_id;
    private String username;
    private List<BookingDate> bookingDate;
    private List<FileDto> files;
}
