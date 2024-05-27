package com.masterpiece.ToitEnVueBackEnd.controller.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterpiece.ToitEnVueBackEnd.dto.booking.BookingDetailsDto;
import com.masterpiece.ToitEnVueBackEnd.dto.booking.MakeBookingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.booking.OwnerChoiceDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingDto;
import com.masterpiece.ToitEnVueBackEnd.exceptions.booking.BookingException;
import com.masterpiece.ToitEnVueBackEnd.model.booking.Booking;
import com.masterpiece.ToitEnVueBackEnd.model.booking.StatusEnum;
import com.masterpiece.ToitEnVueBackEnd.security.jwt.UserDetailsUtils;
import com.masterpiece.ToitEnVueBackEnd.service.booking.BookingService;
import com.masterpiece.ToitEnVueBackEnd.service.housing.HousingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private HousingService housingService;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<?> makeBooking(
            @RequestParam Long housingId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date beginningDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        MakeBookingDto makeBookingDto = new MakeBookingDto();
        makeBookingDto.setHousing_id(housingId);
        makeBookingDto.setBeginningDate(beginningDate);
        makeBookingDto.setEndDate(endDate);
        try {
            HousingDto housingDto = housingService.getHousing(housingId);
            if (Objects.equals(UserDetailsUtils.getUserDetails().getId(), housingDto.getUser_id())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous ne pouvez pas réserver votre propre logement :D");
            }

            boolean success = bookingService.makeBooking(makeBookingDto);
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Veuillez sélectionner des dates valides");
            }
        } catch (BookingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/details/{housingId}")
    public ResponseEntity<List<BookingDetailsDto>> getBookingDetailsByHousingId(@PathVariable Long housingId) {
        List<BookingDetailsDto> bookingDetails = bookingService.getBookingDetailsByHousingId(housingId);
        return new ResponseEntity<>(bookingDetails, HttpStatus.OK);
    }

    @PatchMapping("/owner-choice")
    public ResponseEntity<OwnerChoiceDto> ownerChoice(@RequestBody OwnerChoiceDto ownerChoiceDto) {
        try {
           OwnerChoiceDto updatedBooking = bookingService.ownerChoice(ownerChoiceDto);
           return ResponseEntity.ok(updatedBooking);
        } catch (BookingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}