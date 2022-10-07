package com.odc.Apiodkerp.Service;

import java.util.List;

import com.odc.Apiodkerp.Models.Notification;

public interface NotificationService {

    Notification creer(Notification notification);

    Notification update(Notification notification);

    void delete(Long id);

    Notification getById(Long id);

    Notification getByTitre(String titre);

    List<Notification> getAll();
}
