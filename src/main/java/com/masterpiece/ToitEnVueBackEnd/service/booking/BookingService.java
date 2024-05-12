package com.masterpiece.ToitEnVueBackEnd.service.booking;

import org.springframework.stereotype.Service;

import java.util.Date;

public interface BookingService {
    boolean makeBooking(Long housingId, Date beginningDate, Date endDate);

    boolean isHousingAvailable(Long housingId, Date beginningDate, Date endDate);

    boolean isValidDate(Date beginningDate, Date endDate, Date currentDate);
}
