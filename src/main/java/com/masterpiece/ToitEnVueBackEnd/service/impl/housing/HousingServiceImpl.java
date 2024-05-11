package com.masterpiece.ToitEnVueBackEnd.service.impl.housing;

import com.masterpiece.ToitEnVueBackEnd.dto.housing.CreateHousingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingDto;
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
}
