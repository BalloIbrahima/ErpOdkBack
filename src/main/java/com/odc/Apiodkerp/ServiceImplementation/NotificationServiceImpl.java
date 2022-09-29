package com.odc.Apiodkerp.ServiceImplementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.odc.Apiodkerp.Models.Notification;
import com.odc.Apiodkerp.Repository.NotificationRepo;
import com.odc.Apiodkerp.Service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    NotificationRepo notificationRepo;

    @Override
    public Notification creer(Notification notification) {
        // TODO Auto-generated method stub
        return notificationRepo.save(notification);
    }

    @Override
    public Notification update(Notification notification) {
        // TODO Auto-generated method stub
        return notificationRepo.save(notification);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        notificationRepo.deleteById(id);

    }

    @Override
    public Notification getById(Long id) {
        // TODO Auto-generated method stub
        return notificationRepo.findById(id).get();
    }

    @Override
    public Notification getByTitre(String titre) {
        // TODO Auto-generated method stub
        return notificationRepo.findByTitre(titre);
    }

    @Override
    public List<Notification> getAll() {
        // TODO Auto-generated method stub
        return notificationRepo.findAll();
    }

}
