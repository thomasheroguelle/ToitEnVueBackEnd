package com.masterpiece.ToitEnVueBackEnd.service.housing;

import com.masterpiece.ToitEnVueBackEnd.dto.housing.CreateHousingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HousingService {
    HousingDto createHousing(CreateHousingDto createHousingDto);
    HousingDto saveHousingWithFiles(CreateHousingDto createHousingDto, List<MultipartFile> files);

}
