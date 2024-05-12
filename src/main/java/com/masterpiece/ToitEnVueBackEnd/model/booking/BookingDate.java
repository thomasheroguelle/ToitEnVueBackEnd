package com.masterpiece.ToitEnVueBackEnd.model.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class BookingDate {
    @JsonFormat(pattern="dd-MM-yyyy")
    private Date beginningDate;
    @JsonFormat(pattern="dd-MM-yyy")
    private Date endDate;
}
