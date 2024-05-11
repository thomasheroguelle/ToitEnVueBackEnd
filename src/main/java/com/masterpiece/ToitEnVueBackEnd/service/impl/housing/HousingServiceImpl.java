package com.masterpiece.ToitEnVueBackEnd.service.impl.housing;

import com.masterpiece.ToitEnVueBackEnd.dto.housing.CreateHousingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingDto;
import com.masterpiece.ToitEnVueBackEnd.model.housing.Housing;
import com.masterpiece.ToitEnVueBackEnd.model.user.User;
import com.masterpiece.ToitEnVueBackEnd.repository.housing.HousingRepository;
import com.masterpiece.ToitEnVueBackEnd.repository.user.UserRepository;
import com.masterpiece.ToitEnVueBackEnd.service.housing.HousingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HousingServiceImpl implements HousingService {
    @Autowired
    private HousingRepository housingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public HousingDto createHousing(CreateHousingDto createHousingDto) {

        User user = userRepository.findUserById(createHousingDto.getUser_id());

        Housing housing = modelMapper.map(createHousingDto, Housing.class);
        housing.setUser(user);
        Housing createdHousing = housingRepository.save(housing);


        return modelMapper.map(createdHousing, HousingDto.class);
    }
}
