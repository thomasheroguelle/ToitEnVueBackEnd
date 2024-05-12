package com.masterpiece.ToitEnVueBackEnd.service.housing;

import com.masterpiece.ToitEnVueBackEnd.dto.housing.CreateHousingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingFromUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HousingService {
    HousingDto saveHousingWithFiles(CreateHousingDto createHousingDto, List<MultipartFile> files);

    List<HousingDto> getAllHousing();

    HousingDto getHousing(Long id);
    List<HousingFromUser> findHousingByUserId(Long userId);


}
