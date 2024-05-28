package com.masterpiece.ToitEnVueBackEnd.repository.booking;

import com.masterpiece.ToitEnVueBackEnd.model.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b " +
            "WHERE b.housing.id = :housingId " +
            "AND b.beginningDate < :endDate " +
            "AND b.endDate > :beginningDate"
    )
    List<Booking> findByHousingIdAndDates(Long housingId, Date beginningDate, Date endDate);

    @Query("SELECT b FROM Booking b WHERE b.housing.id = :housingId")
    List<Booking> findByHousingId(Long housingId);

    @Query("SELECT b FROM Booking b WHERE b.interested.id = :userId")
    List<Booking> findByUserInterestedId(Long userId);

}
