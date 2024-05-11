package com.masterpiece.ToitEnVueBackEnd.dto.file;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDto {
    private int file_id;
    @NotBlank
    private String file_name;
    private String content_type;
}
