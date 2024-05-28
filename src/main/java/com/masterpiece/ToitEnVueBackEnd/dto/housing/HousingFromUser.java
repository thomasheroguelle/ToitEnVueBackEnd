package com.masterpiece.ToitEnVueBackEnd.dto.housing;

import com.masterpiece.ToitEnVueBackEnd.dto.file.FileDto;
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
    private double price;
    private List<FileDto> files;
}
