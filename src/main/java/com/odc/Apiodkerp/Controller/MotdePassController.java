package com.odc.Apiodkerp.Controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Models.EmailDetails;
import com.odc.Apiodkerp.Models.ForgetPass;
import com.odc.Apiodkerp.Models.Historique;
import com.odc.Apiodkerp.Models.Utilisateur;
import com.odc.Apiodkerp.Service.ActiviteService;
import com.odc.Apiodkerp.Service.AouPService;
import com.odc.Apiodkerp.Service.DesignationService;
import com.odc.Apiodkerp.Service.DroitService;
import com.odc.Apiodkerp.Service.EntiteService;
import com.odc.Apiodkerp.Service.EtatService;
import com.odc.Apiodkerp.Service.ForgetPassService;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    private ForgetPassService forgetpass;

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

    @Autowired
    private EmailService emailService;

    /// ::::::::::::::::::::::::::Liste par id
    @ApiOperation(value = "Send Mail")
    @PostMapping("/forgetpassword")
    public ResponseEntity<Object> SendEmail(@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.getByEmail(utilisateur.getEmail());

            if (user != null) {
                String lien = "";
                for (int i = 0; i < 20; i++) {
                    Random random = new Random();

                    char randomizedCharacter = (char) (random.nextInt(26) + 'a');
                    lien = lien + "" + randomizedCharacter;
                }

                try {
                    ForgetPass forget = new ForgetPass();

                    System.out.println(utilisateur.getEmail());
                    System.out.println(user.getEmail());
                    EmailDetails detail = new EmailDetails();
                    detail.setRecipient(user.getEmail());
                    detail.setMsgBody(
                            "Vous avez demandez une reinitialisation de mot de passe ! \n Veuillez clicquez sur le lien suivant :\nhttp://localhost:8100/forgotpassword/"
                                    + lien);
                    System.out.println(emailService.sendSimpleMail(detail));

                    Date date = new Date();
                    forget.setCode(lien);
                    forget.setUser(user);
                    forget.setDate(date);
                    forgetpass.Create(forget);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, "Email envoye !");
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

                }

            } else {
                return ResponseMessage.generateResponse("errorUser", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    /// ::::::::::::::::::::::::::Liste par id
    @ApiOperation(value = "Send Mail")
    @PostMapping("/change/password/{code}")
    public ResponseEntity<Object> ChangePassword(@RequestParam(value = "user") String userVenant,
            @PathVariable("code") String code) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.getByEmail(utilisateur.getEmail());

            if (user != null) {
                ForgetPass forget = forgetpass.Recuperer(code);
                if (forget != null) {
                    if (forget.getUser() == user) {

                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                        Date today = new Date();

                        Date todayWithZeroTime = formatter.parse(formatter.format(today));

                        Date forgetDate = formatter.parse(formatter.format(forget.getDate()));

                        System.out.println("date auj" + todayWithZeroTime);
                        System.out.println("date oub" + forgetDate);

                        if (forgetDate.equals(todayWithZeroTime)) {

                            ///
                            Historique historique = new Historique();
                            historique.setDatehistorique(new Date());
                            historique.setDescription(user.getPrenom() + " " + user.getNom()
                                    + " a modifie son mot de passe.");
                            historiqueService.Create(historique);

                            user.setPassword(utilisateur.getPassword());
                            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                                    utilisateurService.update(user));

                        } else {
                            return ResponseMessage.generateResponse("error", HttpStatus.OK, "Code expiré !");

                        }

                    } else {
                        return ResponseMessage.generateResponse("error", HttpStatus.OK, "Tentative d'usurpation !!");

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Erreur de code!");

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

}
