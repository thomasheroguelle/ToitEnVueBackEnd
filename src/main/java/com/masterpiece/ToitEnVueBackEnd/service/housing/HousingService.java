package com.masterpiece.ToitEnVueBackEnd.service.housing;

import com.masterpiece.ToitEnVueBackEnd.dto.housing.CreateHousingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingDto;

public interface HousingService {
    HousingDto createHousing(CreateHousingDto createHousingDto);
}
