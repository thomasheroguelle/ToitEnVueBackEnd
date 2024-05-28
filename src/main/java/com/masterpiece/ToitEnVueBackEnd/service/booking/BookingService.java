package com.masterpiece.ToitEnVueBackEnd.service.booking;

import com.masterpiece.ToitEnVueBackEnd.dto.booking.BookingDetailsDto;
import com.masterpiece.ToitEnVueBackEnd.dto.booking.MakeBookingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.booking.OwnerChoiceDto;
import com.masterpiece.ToitEnVueBackEnd.dto.booking.UserBookingsDto;

import java.util.Date;
import java.util.List;

public interface BookingService {
    boolean makeBooking(MakeBookingDto makeBookingDto);

    OwnerChoiceDto ownerChoice(OwnerChoiceDto ownerChoiceDto);

    boolean isHousingAvailable(MakeBookingDto makeBookingDto);

    boolean isValidDate(MakeBookingDto makeBookingDto, Date currentDate);

    List<BookingDetailsDto> getBookingDetailsByHousingId(Long housingId);

    double calculateTotalCost(double pricePerDay, Date beginningDate, Date endDate);

    List<UserBookingsDto> findBookingByUserId();
}
