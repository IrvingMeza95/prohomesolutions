package com.EF2.prohomesolutions.repositories;

import com.EF2.prohomesolutions.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepo extends JpaRepository<Job, String> {
}
