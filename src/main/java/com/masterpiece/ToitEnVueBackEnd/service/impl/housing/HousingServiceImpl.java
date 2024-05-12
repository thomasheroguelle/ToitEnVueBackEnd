package com.masterpiece.ToitEnVueBackEnd.service.impl.housing;

import com.masterpiece.ToitEnVueBackEnd.dto.file.FileDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.CreateHousingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingFromUser;
import com.masterpiece.ToitEnVueBackEnd.model.booking.Booking;
import com.masterpiece.ToitEnVueBackEnd.model.booking.BookingDate;
import com.masterpiece.ToitEnVueBackEnd.model.file.File;
import com.masterpiece.ToitEnVueBackEnd.model.housing.Housing;
import com.masterpiece.ToitEnVueBackEnd.model.user.User;
import com.masterpiece.ToitEnVueBackEnd.repository.file.FileRepository;
import com.masterpiece.ToitEnVueBackEnd.repository.housing.HousingRepository;
import com.masterpiece.ToitEnVueBackEnd.repository.user.UserRepository;
import com.masterpiece.ToitEnVueBackEnd.service.file.FileStorageService;
import com.masterpiece.ToitEnVueBackEnd.service.housing.HousingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HousingServiceImpl implements HousingService {
    @Autowired
    private HousingRepository housingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public HousingDto saveHousingWithFiles(CreateHousingDto createHousingDto, List<MultipartFile> files) {

        User user = userRepository.findUserById(createHousingDto.getUser_id());

        Housing housing = modelMapper.map(createHousingDto, Housing.class);
        housing.setUser(user);
        Housing createdHousing = housingRepository.save(housing);

        if (createdHousing.getFiles() == null) {
            createdHousing.setFiles(new ArrayList<>());
        }

        for (MultipartFile file : files) {
            File savedFile = new File();
            savedFile.setFile_name(file.getOriginalFilename());
            savedFile.setContent_type(file.getContentType());
            savedFile.setHousing(createdHousing);
            createdHousing.getFiles().add(fileRepository.save(savedFile));
            fileStorageService.save(file);
        }

        return modelMapper.map(createdHousing, HousingDto.class);
    }

    @Override
    public List<HousingDto> getAllHousing() {
        List<Housing> housingList = housingRepository.findAll();
        return housingList.stream()
                .map(housing -> modelMapper.map(housing, HousingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public HousingDto getHousing(Long id) {
        Housing housing = housingRepository.findById(id).orElse(null);
        if (housing != null) {
            return modelMapper.map(housing, HousingDto.class);
        }
        return null;
    }

    @Override
    public List<HousingFromUser> findHousingByUserId(Long userId) {
        List<Housing> housingList = housingRepository.findHousingsByUserId(userId);
        List<HousingFromUser> housingFromUserList = new ArrayList<>();


        for (Housing housing : housingList) {
            HousingFromUser housingFromUser = new HousingFromUser();

            List<FileDto> files = housing.getFiles().stream()
                    .map(file -> modelMapper.map(file, FileDto.class))
                    .collect(Collectors.toList());

            userRepository.findUserById(housing.getUser().getId());
            housingFromUser.setHousing_id(housing.getHousing_id());
            housingFromUser.setTitle(housing.getTitle());
            housingFromUser.setAddress(housing.getAddress());
            housingFromUser.setCity(housing.getCity());
            housingFromUser.setZipcode(housing.getZipcode());
            housingFromUser.setDescription(housing.getDescription());
            housingFromUser.setPrice(housing.getPrice());
            housingFromUser.setCategory(housing.getCategory());
            housingFromUser.setRooms(housing.getRooms());
            housingFromUser.setBedrooms(housing.getBedrooms());
            housingFromUser.setBathrooms(housing.getBathrooms());
            housingFromUser.setLiving_space(housing.getLiving_space());
            housingFromUser.setHighlights(housing.getHighlights());
            housingFromUser.setYear_of_construction(housing.getYear_of_construction());
            housingFromUser.setHousingCondition(housing.getHousingCondition());
            housingFromUser.setUser_id(housing.getUser().getId());
            housingFromUser.setUsername(housing.getUser().getUsername());
            housingFromUser.setFiles(files);

            List<Booking> bookings = housing.getBookings();
            List<BookingDate> bookingDates = new ArrayList<>();

            for (Booking booking : bookings) {
                BookingDate bookingDate = new BookingDate();
                bookingDate.setBeginningDate(booking.getBeginningDate());
                bookingDate.setEndDate(booking.getEndDate());
                bookingDates.add(bookingDate);
            }

            housingFromUser.setBookingDate(bookingDates);
            housingFromUserList.add(housingFromUser);
        }

        return housingFromUserList;
    }
}
