package com.EF2.prohomesolutions.repositories;

import com.EF2.prohomesolutions.models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepo extends JpaRepository<File, String> {
}
