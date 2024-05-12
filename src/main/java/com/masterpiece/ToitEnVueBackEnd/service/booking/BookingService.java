package com.masterpiece.ToitEnVueBackEnd.service.booking;

import com.masterpiece.ToitEnVueBackEnd.dto.booking.BookingDetailsDto;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

public interface BookingService {
    boolean makeBooking(Long housingId, Date beginningDate, Date endDate);

    boolean isHousingAvailable(Long housingId, Date beginningDate, Date endDate);

    boolean isValidDate(Date beginningDate, Date endDate, Date currentDate);

    List<BookingDetailsDto> getBookingDetailsByHousingId(Long housingId);

    double calculateTotalCost(double pricePerDay, Date beginningDate, Date endDate);
}
