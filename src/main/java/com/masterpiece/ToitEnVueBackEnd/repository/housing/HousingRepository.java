package com.masterpiece.ToitEnVueBackEnd.repository.housing;

import com.masterpiece.ToitEnVueBackEnd.model.housing.Housing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HousingRepository extends JpaRepository<Housing, Long> {
}
