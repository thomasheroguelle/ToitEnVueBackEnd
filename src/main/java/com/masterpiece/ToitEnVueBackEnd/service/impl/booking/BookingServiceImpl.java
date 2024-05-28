package com.masterpiece.ToitEnVueBackEnd.service.impl.booking;

import com.masterpiece.ToitEnVueBackEnd.dto.booking.BookingDetailsDto;
import com.masterpiece.ToitEnVueBackEnd.dto.booking.MakeBookingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.booking.OwnerChoiceDto;
import com.masterpiece.ToitEnVueBackEnd.dto.booking.UserBookingsDto;
import com.masterpiece.ToitEnVueBackEnd.dto.file.FileDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingFromUser;
import com.masterpiece.ToitEnVueBackEnd.exceptions.UnauthorizedAccessException;
import com.masterpiece.ToitEnVueBackEnd.exceptions.booking.BookingException;
import com.masterpiece.ToitEnVueBackEnd.model.booking.Booking;
import com.masterpiece.ToitEnVueBackEnd.model.booking.StatusEnum;
import com.masterpiece.ToitEnVueBackEnd.model.housing.Housing;
import com.masterpiece.ToitEnVueBackEnd.model.user.User;
import com.masterpiece.ToitEnVueBackEnd.repository.booking.BookingRepository;
import com.masterpiece.ToitEnVueBackEnd.repository.housing.HousingRepository;
import com.masterpiece.ToitEnVueBackEnd.repository.user.UserRepository;
import com.masterpiece.ToitEnVueBackEnd.security.jwt.UserDetailsUtils;
import com.masterpiece.ToitEnVueBackEnd.service.booking.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean makeBooking(MakeBookingDto makeBookingDto) {
        Date currentDate = new Date();

        if (!isValidDate(makeBookingDto, currentDate)) {
            return false;
        }

        Long userId = UserDetailsUtils.getUserDetails().getId(); // user logged

        Optional<Housing> housingOptional = housingRepository.findById(makeBookingDto.housing_id);
        User user = userRepository.findUserById(userId);

        if (housingOptional.isPresent()) {
            Housing housing = housingOptional.get();

            if (Objects.equals(housing.getUser().getId(), userId)) {
                return false;
            }

            if (!isHousingAvailable(makeBookingDto)) {
                throw new BookingException("Les dates spécifiées sont déjà réservées pour ce logement.");
            }

            Booking booking = new Booking();
            booking.setBeginningDate(makeBookingDto.getBeginningDate());
            booking.setEndDate(makeBookingDto.getEndDate());
            booking.setUser(housing.getUser()); // owner
            booking.setHousing(housing);
            booking.setStatus(StatusEnum.PENDING);
            booking.setInterested(user); // logged

            bookingRepository.save(booking);
            return true;
        }

        return false;
    }

    @Override
    public OwnerChoiceDto ownerChoice(OwnerChoiceDto ownerChoiceDto) {
        Booking booking = bookingRepository.findById(ownerChoiceDto.getBookingId()).orElseThrow(() -> new BookingException("La réservation n'existe pas"));
        Long userId = UserDetailsUtils.getUserDetails().getId(); // user logged

        if (!Objects.equals(booking.getUser().getId(), userId)) {
            throw new UnauthorizedAccessException("Vous n'êtes pas autorisé à modifier cette réservation");
        }

        booking.setStatus(ownerChoiceDto.getBookingStatus());
        Booking savedBooking = bookingRepository.save(booking);

        OwnerChoiceDto responseDto = new OwnerChoiceDto();
        responseDto.setBookingId(savedBooking.getId());
        responseDto.setBookingStatus(savedBooking.getStatus());
        return responseDto;
    }


    @Override
    public boolean isHousingAvailable(MakeBookingDto makeBookingDto) {
        List<Booking> bookings = bookingRepository.findByHousingIdAndDates(makeBookingDto.housing_id, makeBookingDto.getBeginningDate(), makeBookingDto.getEndDate());
        return bookings.isEmpty();
    }

    @Override
    public boolean isValidDate(MakeBookingDto makeBookingDto, Date currentDate) {
        return !makeBookingDto.beginningDate.before(currentDate) && !makeBookingDto.endDate.before(currentDate) && !makeBookingDto.endDate.before(makeBookingDto.beginningDate);
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

    @Override
    public List<UserBookingsDto> findBookingByUserId() {
        Long userId = UserDetailsUtils.getUserDetails().getId(); // utilisateur connecté
        List<Booking> bookings = bookingRepository.findByUserInterestedId(userId);
        List<UserBookingsDto> bookingDetailsDtos = new ArrayList<>();

        for (Booking booking : bookings) {
            UserBookingsDto bookingDetailsDto = new UserBookingsDto();
            bookingDetailsDto.setId(Math.toIntExact(booking.getId()));
            bookingDetailsDto.setInterested(booking.getInterested().getId());
            bookingDetailsDto.setBeginningDate(booking.getBeginningDate());
            bookingDetailsDto.setEndDate(booking.getEndDate());
            bookingDetailsDto.setStatusEnum(booking.getStatus());

            HousingFromUser housingDto = new HousingFromUser();
            housingDto.setHousing_id(Math.toIntExact(booking.getHousing().getHousing_id()));
            housingDto.setTitle(booking.getHousing().getTitle());
            housingDto.setAddress(booking.getHousing().getAddress());
            housingDto.setCity(booking.getHousing().getCity());
            housingDto.setZipcode(booking.getHousing().getZipcode());
            housingDto.setPrice(booking.getHousing().getPrice());

            List<FileDto> fileDtos = booking.getHousing().getFiles().stream()
                    .map(file -> modelMapper.map(file, FileDto.class))
                    .collect(Collectors.toList());
            housingDto.setFiles(fileDtos);

            bookingDetailsDto.setHousingDto(housingDto);
            bookingDetailsDtos.add(bookingDetailsDto);
        }

        return bookingDetailsDtos;
    }


}
