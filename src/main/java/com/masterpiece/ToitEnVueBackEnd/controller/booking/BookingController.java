package com.masterpiece.ToitEnVueBackEnd.controller.booking;

import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingDto;
import com.masterpiece.ToitEnVueBackEnd.exceptions.booking.BookingException;
import com.masterpiece.ToitEnVueBackEnd.security.jwt.UserDetailsUtils;
import com.masterpiece.ToitEnVueBackEnd.service.booking.BookingService;
import com.masterpiece.ToitEnVueBackEnd.service.housing.HousingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private HousingService housingService;

    @PostMapping
    public ResponseEntity<?> makeBooking(
            @RequestParam Long housingId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date beginningDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        try {
            boolean success = bookingService.makeBooking(housingId, beginningDate, endDate);

            HousingDto housingDto = housingService.getHousing(housingId);

            if (Objects.equals(UserDetailsUtils.getUserDetails().getId(), housingDto.getUser_id())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous ne pouvez pas réserver votre propre logement :D");
            }
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Veuillez séléctionner des dates valides");
            }
        } catch (BookingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
