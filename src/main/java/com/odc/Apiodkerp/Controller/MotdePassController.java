package com.odc.Apiodkerp.Controller;
import com.odc.Apiodkerp.Models.EmailDetails;
import com.odc.Apiodkerp.Models.Utilisateur;
import com.odc.Apiodkerp.ServiceImplementation.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.odc.Apiodkerp.Configuration.ResponseMessage.generateResponse;
@RestController
@RequestMapping("/motdepass")
@Api(value = "formatemail", description = "Les fonctionnalités liées à une formatemail")
@CrossOrigin
public class MotdePassController {
    @Autowired
    JavaMailSender javaMailSender;

    @ApiOperation(value = "Envoyer email")
    @PostMapping("/sendMail1")
    public ResponseEntity<Object> sendEmail(@PathVariable String mail){

        SimpleMailMessage sm = new SimpleMailMessage();
        sm.setFrom("abassemaiga403@gmail.com");//input the sender email Id or read it from properties file
        sm.setTo(mail);
        sm.setSubject("Welcome to Java SpringBoot Application");
        sm.setText("Hello \n Welcome to the Java Springboot Mail Example.");
        javaMailSender.send(sm);

        return generateResponse("Email Sent to the mail ", HttpStatus.OK, mail);
    }

    public ResponseEntity<Object> generateResponse(String msg, HttpStatus st , Object response){
        Map<String, Object> mp = new HashMap<String, Object>();

        mp.put("message", msg);
        mp.put("status", st.value());
        mp.put("data",response);

        return  new ResponseEntity<Object>(mp,st);
    }



    @Autowired private EmailService emailService;

    // Sending a simple Email
    @PostMapping("/sendMailsansAt")
    public String
    sendMail(@RequestBody EmailDetails details)
    {
        String status
                = emailService.sendSimpleMail(details);

        return status;
    }

    // Sending email with attachment
    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(
            @RequestBody EmailDetails details)
    {
        String status
                = emailService.sendMailWithAttachment(details);

        return status;
    }













}
