package com.masterpiece.ToitEnVueBackEnd.dto.booking;

import com.masterpiece.ToitEnVueBackEnd.model.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class MakeBookingDto {
    public Long housing_id;
    public Date beginningDate;
    public Date endDate;
    public User user_id;
    public StatusEnumDto statusEnumDto;

}
