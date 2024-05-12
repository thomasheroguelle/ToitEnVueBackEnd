package com.masterpiece.ToitEnVueBackEnd.service.impl.booking;

import com.masterpiece.ToitEnVueBackEnd.dto.booking.BookingDetailsDto;
import com.masterpiece.ToitEnVueBackEnd.exceptions.booking.BookingException;
import com.masterpiece.ToitEnVueBackEnd.model.booking.Booking;
import com.masterpiece.ToitEnVueBackEnd.model.housing.Housing;
import com.masterpiece.ToitEnVueBackEnd.repository.booking.BookingRepository;
import com.masterpiece.ToitEnVueBackEnd.repository.housing.HousingRepository;
import com.masterpiece.ToitEnVueBackEnd.security.jwt.UserDetailsUtils;
import com.masterpiece.ToitEnVueBackEnd.service.booking.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private HousingRepository housingRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public boolean makeBooking(Long housingId, Date beginningDate, Date endDate) {
        Date currentDate = new Date();

        if (!isValidDate(beginningDate, endDate, currentDate)) {
            return false;
        }

        Long userId = UserDetailsUtils.getUserDetails().getId();

        Optional<Housing> housingOptional = housingRepository.findById(housingId);

        if (housingOptional.isPresent()) {
            Housing housing = housingOptional.get();

            if (Objects.equals(housing.getUser().getId(), userId)) {
                return false;
            }

            if (!isHousingAvailable(housingId, beginningDate, endDate)) {
                throw new BookingException("Les dates spécifiées sont déjà réservées pour ce logement.");
            }

            Booking booking = new Booking();
            booking.setBeginningDate(beginningDate);
            booking.setEndDate(endDate);
            booking.setUser(housing.getUser());
            booking.setHousing(housing);

            bookingRepository.save(booking);
            return true;
        }

        return false;
    }

    @Override
    public boolean isHousingAvailable(Long housingId, Date beginningDate, Date endDate) {
        List<Booking> bookings = bookingRepository.findByHousingIdAndDates(housingId, beginningDate, endDate);
        return bookings.isEmpty();
    }

    @Override
    public boolean isValidDate(Date beginningDate, Date endDate, Date currentDate) {
        return !beginningDate.before(currentDate) && !endDate.before(currentDate) && !endDate.before(beginningDate);
    }

    @Override
    public List<BookingDetailsDto> getBookingDetailsByHousingId(Long housingId) {
        List<Booking> bookings = bookingRepository.findByHousingId(housingId);
        return bookings.stream()
                .map(booking -> {
                    double totalCost = calculateTotalCost(booking.getHousing().getPrice(), booking.getBeginningDate(), booking.getEndDate());
                    BookingDetailsDto bookingDetailsDto = modelMapper.map(booking, BookingDetailsDto.class);
                    bookingDetailsDto.setPrice(totalCost);
                    return bookingDetailsDto;
                })
                .collect(Collectors.toList());
    }

    public double calculateTotalCost(double pricePerDay, Date beginningDate, Date endDate) {
        long numberOfDays = (endDate.getTime() - beginningDate.getTime()) / (1000 * 60 * 60 * 24);
        return pricePerDay * numberOfDays;
    }


}
