package com.masterpiece.ToitEnVueBackEnd.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingFromUser;
import com.masterpiece.ToitEnVueBackEnd.model.booking.StatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserBookingsDto {
    private int id;
    private Long interested;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date beginningDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date endDate;
    private HousingFromUser housingDto;
    private StatusEnum statusEnum;
}
