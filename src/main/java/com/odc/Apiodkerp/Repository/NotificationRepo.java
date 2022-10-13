package com.odc.Apiodkerp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.odc.Apiodkerp.Models.Notification;

public interface NotificationRepo extends JpaRepository<Notification, Long> {

    Notification findByTitre(String titre);

    List<Notification> findAllByOrderByIdDesc();


}
