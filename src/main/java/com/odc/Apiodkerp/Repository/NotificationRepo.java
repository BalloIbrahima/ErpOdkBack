package com.odc.Apiodkerp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.odc.Apiodkerp.Models.Notification;

public interface NotificationRepo extends JpaRepository<Notification, Long> {

    Notification findByTitre(String titre);

}
