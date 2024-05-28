package com.masterpiece.ToitEnVueBackEnd.dto.booking;

import com.masterpiece.ToitEnVueBackEnd.model.booking.StatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerChoiceDto {
    private Long bookingId;
    private StatusEnum bookingStatus;
}

