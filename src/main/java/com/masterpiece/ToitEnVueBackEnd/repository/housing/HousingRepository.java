package com.masterpiece.ToitEnVueBackEnd.repository.housing;

import com.masterpiece.ToitEnVueBackEnd.model.housing.Housing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HousingRepository extends JpaRepository<Housing, Long> {
    @Query("SELECT h FROM Housing h WHERE h.user.id = :userId")
    List<Housing> findHousingsByUserId(@Param("userId") Long userId);
}
