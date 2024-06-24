package com.masterpiece.ToitEnVueBackEnd.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class BookingDetailsDto {
    private int id;
    private int housingId;
    private Long userId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date beginningDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date endDate;
    private double price;
    }
