package com.odc.Apiodkerp.Controller;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Models.EmailDetails;
import com.odc.Apiodkerp.Models.Utilisateur;
import com.odc.Apiodkerp.Service.ActiviteService;
import com.odc.Apiodkerp.Service.AouPService;
import com.odc.Apiodkerp.Service.DesignationService;
import com.odc.Apiodkerp.Service.DroitService;
import com.odc.Apiodkerp.Service.EntiteService;
import com.odc.Apiodkerp.Service.EtatService;
import com.odc.Apiodkerp.Service.FormatEmailService;
import com.odc.Apiodkerp.Service.HistoriqueService;
import com.odc.Apiodkerp.Service.ListePostulantService;
import com.odc.Apiodkerp.Service.PostulantService;
import com.odc.Apiodkerp.Service.PostulantTrieService;
import com.odc.Apiodkerp.Service.PresenceService;
import com.odc.Apiodkerp.Service.RoleService;
import com.odc.Apiodkerp.Service.SalleService;
import com.odc.Apiodkerp.Service.TirageService;
import com.odc.Apiodkerp.Service.TypeActiviteService;
import com.odc.Apiodkerp.Service.UtilisateurService;
import com.odc.Apiodkerp.ServiceImplementation.EmailDetailsInterf;
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
import java.util.Random;

import static com.odc.Apiodkerp.Configuration.ResponseMessage.generateResponse;
@RestController
@RequestMapping("/motdepass")
@Api(value = "formatemail", description = "Les fonctionnalités liées à une formatemail")
@CrossOrigin
public class MotdePassController {
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private ActiviteService activiteService;

    @Autowired
    private EntiteService entiteService;

    @Autowired
    private DroitService droitService;
    
    @Autowired
    private RoleService roleService;
    @Autowired
    private EtatService etatService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private FormatEmailService formatEmailService;

    @Autowired
    private ListePostulantService listePostulantService;

    @Autowired
    private PostulantService postulantService;

    @Autowired
    private AouPService aouPService;

    @Autowired
    private PostulantTrieService postulantTrieService;

    @Autowired
    private PresenceService presenceService;

    @Autowired
    private HistoriqueService historiqueService;

    @Autowired
    private RoleService RoleService;

    @Autowired
    private SalleService salleService;

    @Autowired
    private TirageService tirageService;

    @Autowired
    private TypeActiviteService typeActiviteService;

    @Autowired
    private EmailDetailsInterf email;

    @Autowired
    JavaMailSender javaMailSender;


     /// ::::::::::::::::::::::::::Liste par id
     @ApiOperation(value = "recupere une liste par id")
     @PostMapping("/forgetpassword")
     public ResponseEntity<Object> SendEmail(@RequestParam(value = "user") String userVenant) {
         try {
 
             Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
 
             Utilisateur user = utilisateurService.getByEmail(utilisateur.getEmail());
 
             if(user !=null){
                 String lien="";
                 for(int i=0; i<20;i++){
                     Random random = new Random();
 
                     char randomizedCharacter = (char) (random.nextInt(26) + 'a');
                     lien=lien+""+randomizedCharacter;
                 }
 
                 EmailDetails detail=new EmailDetails();
                 detail.setRecipient(user.getEmail());
                 detail.setMsgBody("Vous avez demandez une reinitialisation de mot de passe ! \n Veuillez clicquez sur le lien suivant :\nhttp://localhost:8100/forgotpassword/"+lien);
                 email.sendSimpleMail(detail);

                 return ResponseMessage.generateResponse("ok", HttpStatus.OK, "Email envoye !");

             }else{
                 return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");
 
             }
             
         } catch (Exception e) {
             // TODO: handle exception
             return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
 
         }
     }



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
