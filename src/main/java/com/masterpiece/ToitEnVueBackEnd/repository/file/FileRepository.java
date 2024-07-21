package com.masterpiece.ToitEnVueBackEnd.repository.file;

import com.masterpiece.ToitEnVueBackEnd.model.file.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}
