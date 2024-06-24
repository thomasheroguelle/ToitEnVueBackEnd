package com.masterpiece.ToitEnVueBackEnd.repository.housing;

import com.masterpiece.ToitEnVueBackEnd.model.housing.Housing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface HousingRepository extends JpaRepository<Housing, Long> {
    @Query("SELECT h FROM Housing h WHERE h.user.id = :userId")
    List<Housing>findHousingsByUserId(@Param("userId") Long userId);
}
