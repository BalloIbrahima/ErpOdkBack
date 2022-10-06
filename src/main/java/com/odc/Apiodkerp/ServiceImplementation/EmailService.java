package com.odc.Apiodkerp.ServiceImplementation;

import com.odc.Apiodkerp.Models.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}
