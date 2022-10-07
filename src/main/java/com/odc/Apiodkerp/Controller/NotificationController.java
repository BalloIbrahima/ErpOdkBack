package com.odc.Apiodkerp.Controller;

import com.odc.Apiodkerp.Models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Service.ActiviteService;
import com.odc.Apiodkerp.Service.AouPService;
import com.odc.Apiodkerp.Service.DroitService;
import com.odc.Apiodkerp.Service.EntiteService;
import com.odc.Apiodkerp.Service.EtatService;
import com.odc.Apiodkerp.Service.HistoriqueService;
import com.odc.Apiodkerp.Service.IntervenantExterneService;
import com.odc.Apiodkerp.Service.ListePostulantService;
import com.odc.Apiodkerp.Service.NotificationService;
import com.odc.Apiodkerp.Service.PostulantService;
import com.odc.Apiodkerp.Service.PostulantTrieService;
import com.odc.Apiodkerp.Service.PresenceService;
import com.odc.Apiodkerp.Service.RoleService;
import com.odc.Apiodkerp.Service.SalleService;
import com.odc.Apiodkerp.Service.StatusService;
import com.odc.Apiodkerp.Service.TacheService;
import com.odc.Apiodkerp.Service.TirageService;
import com.odc.Apiodkerp.Service.TypeActiviteService;
import com.odc.Apiodkerp.Service.UtilisateurService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;

@RestController
@RequestMapping("/notification")
@Api(value = "notification", description = "Les fonctionnalités liées à un notification")
@CrossOrigin
public class NotificationController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private ActiviteService activiteService;

    @Autowired
    private EntiteService entiteService;

    @Autowired
    private EtatService etatService;

    @Autowired
    private ListePostulantService listePostulantService;

    @Autowired
    private PostulantService postulantService;

    @Autowired
    private PostulantTrieService postulantTrieService;

    @Autowired
    private PresenceService presenceService;

    @Autowired
    private RoleService RoleService;

    @Autowired
    private SalleService salleService;

    @Autowired
    private TirageService tirageService;

    @Autowired
    private TypeActiviteService typeActiviteService;

    @Autowired
    private TacheService tacheService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private IntervenantExterneService intervenantExterneService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AouPService aouPService;

    @Autowired
    private HistoriqueService historiqueService;

    @Autowired
    private DroitService droitService;


    // la methode pour afficher toutes les notifications
    @ApiOperation(value = "la methode pour afficher toutes les notifications.")
    @PostMapping("/allnotification")
    public ResponseEntity<Object> ImportListePostulant(@RequestParam(value = "user") String userVenant) {

        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur Simpleutilisateur = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());


            if (Simpleutilisateur != null) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        notificationService.getAll());
                
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK,
                        "Cet utilisateur n'existe pas !");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

   /* @ApiOperation(value = "Affichager une notification")
    @PostMapping("/get/notification/{id}")
    public ResponseEntity<Object> GetIdNotif(@PathVariable("id") Long id,
                                                   @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Notification notification = new Notification();
            Droit Rnotification = droitService.GetLibelle("Read notification");

            if (user != null) {
                if (user.getRole().getDroits().contains(Rnotification)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a regarder le notification " + notificationService.getById(id));
                        historiqueService.Create(historique);
                        Notification idNotif = notificationService.getById(id);
                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, idNotif);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }
*/


    // ::::::::::Recuperer salle par id
   @ApiOperation(value = "Recuperer salle par id")
    @PostMapping("/get/notification/{id}")
    public ResponseEntity<Object> getNotification(@PathVariable("id") Long id,
                                           @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Notification notification = new Notification();
            Droit Rsalle = droitService.GetLibelle("Read notification");

            if (user != null) {
                if (user.getRole().getDroits().contains(Rsalle)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a regarder le notification " + notification.getId());
                        historiqueService.Create(historique);
                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, notificationService.getById(id));

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

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
