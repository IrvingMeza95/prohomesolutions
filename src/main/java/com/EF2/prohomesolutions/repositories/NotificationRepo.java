package com.EF2.prohomesolutions.repositories;

import com.EF2.prohomesolutions.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, String> {
}
