package com.masterpiece.ToitEnVueBackEnd.dto.housing;

import com.masterpiece.ToitEnVueBackEnd.dto.file.FileDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HousingDto extends CreateHousingDto {
    private int housing_id;
    private List<FileDto> files;
}
