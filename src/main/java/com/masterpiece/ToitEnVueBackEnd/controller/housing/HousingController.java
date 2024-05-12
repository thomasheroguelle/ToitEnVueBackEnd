package com.masterpiece.ToitEnVueBackEnd.controller.housing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterpiece.ToitEnVueBackEnd.dto.booking.BookingDetailsDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.CreateHousingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingFromUser;
import com.masterpiece.ToitEnVueBackEnd.exceptions.file.EmptyFileException;
import com.masterpiece.ToitEnVueBackEnd.repository.housing.HousingRepository;
import com.masterpiece.ToitEnVueBackEnd.security.jwt.UserDetailsUtils;
import com.masterpiece.ToitEnVueBackEnd.service.booking.BookingService;
import com.masterpiece.ToitEnVueBackEnd.service.housing.HousingService;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/housing")
public class HousingController {
    @Autowired
    private HousingService housingService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private HousingRepository housingRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createHousing(@Valid @RequestPart("housing") String createHousingJson,
                                           @Valid @RequestPart("files") List<MultipartFile> files
    ) throws JsonProcessingException {

        CreateHousingDto createHousingDto = objectMapper.readValue(createHousingJson, CreateHousingDto.class);

        if (!Objects.equals(UserDetailsUtils.getUserDetails().getId(), createHousingDto.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Incohérence au niveau des ID");
        }
        if (!Objects.equals(UserDetailsUtils.getUserDetails().getUsername(), createHousingDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Accès non autorisé : vous n'êtes pas autorisé à effectuer cette action");
        }
        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body("Aucun fichier n'a été fourni.");
        }
        try {
            HousingDto savedHousing = housingService.saveHousingWithFiles(createHousingDto, files);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedHousing);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT invalide.");
        } catch (EmptyFileException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<HousingDto>> getHousingList() {
        List<HousingDto> housingList = housingService.getAllHousing();
        return ResponseEntity.ok(housingList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HousingDto> getHousingById(@PathVariable Long id) {
        HousingDto housingById = housingService.getHousing(id);
        if (housingById == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(housingById, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<HousingFromUser>> getHousingByUserId(@PathVariable Long userId) {
        Long authenticatedUserId = UserDetailsUtils.getUserDetails().getId();

        if (!Objects.equals(authenticatedUserId, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        List<HousingFromUser> housingList = housingService.findHousingByUserId(userId);
        return ResponseEntity.ok(housingList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHousingFromUser(@PathVariable Long id) {
        HousingDto housingDto = housingService.getHousing(id);
        if (housingDto == null) {
            return ResponseEntity.notFound().build();
        }

        Long authenticatedUserId = UserDetailsUtils.getUserDetails().getId();
        if (!Objects.equals(authenticatedUserId, housingDto.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'êtes pas autorisé à supprimer cette annonce.");
        }

        List<BookingDetailsDto> bookingDetails = bookingService.getBookingDetailsByHousingId(id);
        if (!bookingDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossible de supprimer cette annonce car elle a des réservations associées.");
        }

        housingRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }


}
