package com.odc.Apiodkerp.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.odc.Apiodkerp.Models.*;

import java.io.Console;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Configuration.SaveImage;
import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.Role;
import com.odc.Apiodkerp.Models.Utilisateur;

import org.springframework.web.bind.annotation.*;

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

@RestController
@RequestMapping("/utilisateur")
@Api(value = "utilisateur", description = "Les fonctionnalités liées à un utilisateur simple")
@CrossOrigin
public class UtilisateurController {

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
    private HistoriqueService historiqueService;

    @Autowired
    private TacheService tacheService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private IntervenantExterneService intervenantExterneService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private DroitService droitService;

    @Autowired
    private AouPService aPService;

    // Pour le login d'un utilisateur
    @ApiOperation(value = "Pour le login d'un utilisateur.")
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Utilisateur utilisateur) {

        System.out.println(utilisateur.getLogin());
        System.out.println(utilisateur.getPassword());

        // try {
        Utilisateur Simpleutilisateur = utilisateurService.login(utilisateur.getLogin(), utilisateur.getPassword());

        System.out.println(Simpleutilisateur);
        if (Simpleutilisateur != null) {
            if (Simpleutilisateur.getActive() == true) {

                Historique historique = new Historique();
                historique.setDatehistorique(new Date());
                historique.setDescription(Simpleutilisateur.getPrenom() + " " + Simpleutilisateur.getNom()
                        + " vient de se connecter.");

                historiqueService.Create(historique);
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, Simpleutilisateur);
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK,
                        "vous n'avez pas les droits d'acces  !");
            }

        } else {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, "Login ou mot de passe incorrect !");
        }

        // } catch (Exception e) {
        // // TODO: handle exception
        // return ResponseMessage.generateResponse("error", HttpStatus.OK,
        // e.getMessage());
        // }
    }

    // Modification de l'activite
    @ApiOperation(value = "Modification de l'activite en fonction de l'id")
    @PostMapping("/update/activity/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestParam(value = "activte") String activi,
            @RequestParam(value = "user") String userVenant,

            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Activite act = new JsonMapper().readValue(activi, Activite.class);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Activite activite1 = activiteService.GetById(id);

            if (file != null) {
                act.setImage(SaveImage.save("activite", file, act.getNom()));
            }

            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Droit updateActivite = droitService.GetLibelle("Update Activite");

            if (user != null) {
                if (user.getRole().getDroits().contains(updateActivite)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a modifié l'activite "
                                        + activite1.getNom());
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("error", HttpStatus.OK,
                            activiteService.Update(id, act));
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

    // affichage d'activite en fonction de l'id
    @ApiOperation(value = "Affichage de l'activite en fonction de l'id")
    @PostMapping("/afficherActivit/{id}")

    public ResponseEntity<Object> AfficherActivit(@PathVariable long id,
            @RequestParam(value = "user") String userVenant) {
        try {
            Activite activite1 = activiteService.GetById(id);
            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());

            Droit readActivite = droitService.GetLibelle("Read Activite");

            if (user != null) {
                if (user.getRole().getDroits().contains(readActivite)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a affiché l'activite "
                                        + activite1.getNom());
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, activiteService.GetById(id));
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

    // Supprimer d'activite en fonction de l'id
    @ApiOperation(value = "Supprimer une activite en fonction de l'id")
    @PostMapping("/supprimeractivite/{idactivite}")
    public ResponseEntity<Object> supprimer(@PathVariable("idactivite") long idactivite,

            @RequestParam(value = "user") String userVenant) {

        try {
            Activite activite = activiteService.GetById(idactivite);

            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());

            Droit deleteActivite = droitService.GetLibelle("Delete Activite");

            if (user != null) {
                if (user.getRole().getDroits().contains(deleteActivite)) {
                    Role admin = RoleService.GetByLibelle("ADMIN");

                    if (activite.getCreateur() == user || user.getRole() == admin) {

                        try {
                            Historique historique = new Historique();
                            Date datehisto = new Date();
                            historique.setDatehistorique(datehisto);
                            historique
                                    .setDescription(
                                            "" + user.getPrenom() + " " + user.getNom()
                                                    + " a supprime l activite "
                                                    + activite.getNom());
                            historiqueService.Create(historique);
                            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                                    activiteService.Delete(idactivite));
                        } catch (Exception e) {
                            // TODO: handle exception
                            return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                        }

                    } else {
                        return ResponseMessage.generateResponse("error", HttpStatus.OK, "vous n'etes pas autorisé");
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

    // Afficher activite en fonction de l'etat
    @ApiOperation(value = "Affichage de l'activite en fonction de son etat")
    @PostMapping("/afficherActiviteEtat/{idetat}")

    public ResponseEntity<Object> AfficherActivite(@PathVariable Long idetat,
            @RequestParam(value = "user") String userVenant) {

        Etat etat = etatService.GetById(idetat);
        try {
            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());

            Droit readActivite = droitService.GetLibelle("Read Activite");

            if (user != null) {
                if (user.getRole().getDroits().contains(readActivite)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a affiché des activites en fontion de l'etat " + etat.getStatut());
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, activiteService.GetByEtat(etat));
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

    // afficher toutes les activites
    @ApiOperation(value = "Afficher toutes les  activite  ")
    @PostMapping("/allactivite")

    public ResponseEntity<Object> ToutesActivite(@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());

            Droit readActivite = droitService.GetLibelle("Read Activite");

            if (user != null) {
                if (user.getRole().getDroits().contains(readActivite)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a affiche toutes les activites ");
                        historiqueService.Create(historique);

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, activiteService.FindAllAct());

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

    // Filtre de l'ensemble des activités
    @ApiOperation(value = "Controller qui filtre les activités par nom, types, entite, date début et date fin")
    @PostMapping("/activite/{nomactivite}/{typeactivite}/{entite}/{dtdebut}/{dtfin}")
    public ResponseEntity<Object> filtrerActivite(
            @RequestParam(value = "user") String usercourant,
            @PathVariable String nomactivite,
            @PathVariable String typeactivite,
            @PathVariable String entite,
            @PathVariable String dtdebut,
            @PathVariable String dtfin) {
        try {
            Utilisateur monutilisateur = new JsonMapper().readValue(usercourant, Utilisateur.class);
            Utilisateur thisuser = utilisateurService.trouverParLoginAndPass(monutilisateur.getLogin(),
                    monutilisateur.getPassword());
            Droit readActivite = droitService.GetLibelle("Read Activite");
            if (thisuser != null) {
                if (thisuser.getRole().getDroits().contains(readActivite)) {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            thisuser.getPrenom() + " " + thisuser.getNom() + " a filtre les activites");
                    historiqueService.Create(historique);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            activiteService.findFiltre(nomactivite, typeactivite, entite, dtdebut, dtfin));
                } else
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Vous n'êtes pas autorisé !");

            } else
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");
        } catch (JsonMappingException e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        } catch (JsonProcessingException e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // Afficher une activite
    @ApiOperation(value = "Afficher une activite en fonction de l'id ")

    @PostMapping("/activite/{idactivite}")
    public ResponseEntity<Object> Afficheractivite(@PathVariable long idactivite,
            @RequestParam(value = "user") String userVenant) {
        try {
            Activite activite = activiteService.GetById(idactivite);
            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());

            Droit readActivite = droitService.GetLibelle("Read Activite");

            if (user != null) {
                if (user.getRole().getDroits().contains(readActivite)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a affiche  " + activite.getNom());
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, activite);
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

    // Ajouter des participants ou apprenants à la liste de presence
    @ApiOperation(value = "Add un apprenant à la liste de presence ")
    @PostMapping("/lapresence/{idpresence}")
    public ResponseEntity<Object> presence(
            @PathVariable("idpresence") long idpresence, @RequestParam(value = "user") String userVenant) {
        try {
            // Presence presence = new Presence();
            // Activite activite = activiteService.GetById(idactivite);
            // presence.setActivite(activite);
            // presence.setDate(new Date());
            // AouP aouP = aPService.GetById(idaoup);
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            // presence.setPostulantTire(postulantTire);

            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Droit createpresence = droitService.GetLibelle("Create Presence");

            Presence presence = presenceService.getById(idpresence);
            if (user != null) {
                if (user.getRole().getDroits().contains(createpresence)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a ajouter " + presence.getAouP().getPostulant().getEmail()
                                + " a la liste de presence de l'activité " + presence.getActivite().getNom());
                        historiqueService.Create(historique);

                        presence.setEtat(true);

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, presenceService.creer(presence));

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

    @ApiOperation(value = " afficher la liste de presence en fonction de l'id de l'activite")

    @PostMapping("/lapresence/{idactivite}")
    public ResponseEntity<Object> Listepresence(@PathVariable long idactivite,
            @RequestParam(value = "user") String userVenant) {

        try {
            Activite act = activiteService.GetById(idactivite);
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());

            Droit readpresence = droitService.GetLibelle("Read Presence");

            if (user != null) {
                if (user.getRole().getDroits().contains(readpresence)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a affiché la liste de  presence de l'activté " + act.getNom());
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, act.getPresences());
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

    //

    @ApiOperation(value = "Modification de l'entite en fonction de l'id")

    @PostMapping("/updateentite/{id}")
    public ResponseEntity<Object> updateEntite(@RequestParam(value = "user") String userVenant, @PathVariable Long id,
            @RequestBody Entite entite) {
        try {

            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());

            Droit updateentite = droitService.GetLibelle("Update Entite");

            if (user != null) {
                if (user.getRole().getDroits().contains(updateentite)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom() + " a modifié l'entité "
                                + entite.getLibelleentite());
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, entiteService.Update(id, entite));
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
    ////
    // ::::::::::::::::::::::::::::::::::::::::ACTIVITE
    //// ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    // methode pour la création d'une activité
    @ApiOperation(value = "methode pour la création d'une activité. ::::::::::::::::::::::::::::")
    @PostMapping("/activite/new")
    public ResponseEntity<Object> Createactivite(@RequestParam(value = "data") String acti,

            @RequestParam(value = "user") String userVenant,
            @RequestParam(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        Activite activite = null;
        Utilisateur utilisateurs=null;

        try {
            activite = new JsonMapper().readValue(acti, Activite.class);
            System.out.println(activite);
            utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);
        } catch (Exception e) {

            System.out.println(activite);

            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
            // Salle salle = salleService.read(idsalle);

            TypeActivite talk=typeActiviteService.getByLibelle("Talk");
            TypeActivite eve=typeActiviteService.getByLibelle("Evenement");
            TypeActivite formation=typeActiviteService.getByLibelle("Formations");
            System.out.println(talk);
            System.out.println(eve);
            System.out.println(formation);

            long diffInMillies = Math.abs(activite.getDateFin().getTime() - activite.getDateDebut().getTime());

            long difInDay=diffInMillies/86400000;
            System.out.println(difInDay);
            System.out.println(activite.getTypeActivite().getLibelle());


            Activite find=activiteService.RecupererParNom(activite.getNom());
            if(find==null){
                if (file != null) {
                    if(activite.getDateDebut().before(new Date())){
                        return ResponseMessage.generateResponse("error", HttpStatus.OK, "Veuillez renseigner une date ultérieure à aujourd'hui ! ");
                    }else 
                    if(activite.getDateFin().before(activite.getDateDebut())){
                        return ResponseMessage.generateResponse("error", HttpStatus.OK, "La date de fin de l'activite doit être ultérieure à la date de debut !");
                    }else{
                        if((activite.getTypeActivite().getLibelle().equals("Talk") && difInDay>3) || (activite.getTypeActivite().getLibelle().equals("Evenement") && difInDay>3)){

                            return ResponseMessage.generateResponse("error", HttpStatus.OK, "Le type d'activité ne correspond pas au nombre de jours ! "+activite.getTypeActivite().getLibelle()+" "+ difInDay+"Jours");

                        }else if(activite.getTypeActivite().getLibelle().equals("Formations") && (difInDay<=7 || difInDay>190)){
                            if(difInDay>190){
                                difInDay=difInDay/30;
                                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Le type d'activité ne correspond pas au nombre de jours ! "+activite.getTypeActivite().getLibelle()+" "+ difInDay+"Mois");
                            }
                            return ResponseMessage.generateResponse("error", HttpStatus.OK, "Le type d'activité ne correspond pas au nombre de jours ! "+activite.getTypeActivite().getLibelle()+" "+ difInDay+"Jours");
                        }else{
                            try {
                                Etat etat = etatService.recupereParStatut("A VENIR");
            
                                Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                                        utilisateurs.getPassword());
                                Droit createActivite = droitService.GetLibelle("Create Activite");
            
                                // TypeActivite type = typeActiviteService.getById(idtype);
            
                                // activite.setTypeActivite(type);
                                // activite.setSalle(salle);
                                activite.setCreateur(user);
                                activite.setEtat(etat);
                                activite.setLeader(user);
                                activite.setDateCreation(new Date());
                                System.out.println(user);
                                // activite.setLeader(user);
                                try {
                                    activite.setImage(SaveImage.save("activite", file, activite.getNom()));
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());
            
                                }
            
                                // ::::::::::::::::::::::::::::Historique ::::::::::::::::
                                // Utilisateur user = utilisateurService.getById(iduser);
                                if (user != null) {
                                    if (user.getRole().getDroits().contains(createActivite)) {
                                        try {
                                            Activite act = activiteService.Create(activite);
                                            Notification notif = new Notification();
                                            notif.setActivite(act);
                                            notif.setDatenotif(new Date());
                                            // notif.setDescription(description);
                                            notificationService.creer(notif);
            
                                            Historique historique = new Historique();
                                            Date datehisto = new Date();
                                            historique.setDatehistorique(datehisto);
                                            historique
                                                    .setDescription(
                                                            "" + user.getPrenom() + " " + user.getNom() + " a cree l activite "
                                                                    + activite.getNom());
                                            historiqueService.Create(historique);
            
                                            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                                                    act);
                                        } catch (Exception e) {
                                            // TODO: handle exception
                                            return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());
            
                                        }
            
                                    } else {
                                        return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
            
                                    }
            
                                } else {
                                    return ResponseMessage.generateResponse("error", HttpStatus.OK,
                                            "Cet utilisateur n'existe pas !");
            
                                }
                            } catch (Exception e) {
            
                                return ResponseMessage.generateResponse("errorpp", HttpStatus.OK, e.getMessage());
                            }
            
                        }
                    }
                    
    
                } else {
    
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Fichier vide");
                }
            }else{
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Une activite existe déja avec le même nom !");

            }
            

       

        // application/json

    }
    // ::::::::::::::::::::::::::::::::TYPE ACTIVITE
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "methode pour la création d'une type d' activité.")
    @PostMapping("/TypeactiviteCreer")
    public ResponseEntity<Object> CreateTypeActivite(@RequestParam(value = "type") String type,
            @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit createType = droitService.GetLibelle("Create TypeActivite");

            TypeActivite typeActivite = new JsonMapper().readValue(type, TypeActivite.class);

            if (user != null) {
                if (user.getRole().getDroits().contains(createType)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a crée le type d'activte "
                                        + typeActivite.getLibelle());
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            typeActiviteService.creer(typeActivite));

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

    @ApiOperation(value = "methode pour la Suppression d'une type d' activité.")
    @PostMapping("/TypeactiviteSupprimer/{id}")
    public ResponseEntity<Object> SupprimerTypeActivite(@PathVariable long id,
            @RequestParam(value = "user") String userVenant,
            @RequestParam(value = "type") String type) {

        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit deleteType = droitService.GetLibelle("Delete TypeActivite");

            TypeActivite typeActivite = new JsonMapper().readValue(type, TypeActivite.class);

            if (user != null) {
                if (user.getRole().getDroits().contains(deleteType)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a crée le type d'activte "
                                        + typeActivite.getLibelle());
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, typeActiviteService.delete(id));
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

    @ApiOperation(value = "methode pour la modification d'une type d' activité.")
    @PostMapping("/TypeactiviteModif")
    public ResponseEntity<Object> ModifTypeActivite(@RequestParam(value = "user") String userVenant,
            @RequestParam(value = "type") String type) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit updateType = droitService.GetLibelle("Update TypeActivite");

            TypeActivite typeActivite = new JsonMapper().readValue(type, TypeActivite.class);

            if (user != null) {
                if (user.getRole().getDroits().contains(updateType)) {
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            typeActiviteService.update(typeActivite));

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

    //////// :::::::::::::::::::::::historique
    // methode pour la création d'une historique
    @ApiOperation(value = "methode pour la création d'une historique.")
    @PostMapping("/historique/new")
    public ResponseEntity<Object> CreateHistorique(@RequestParam(value = "historique") String histoi,
            @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Historique historique = new JsonMapper().readValue(histoi, Historique.class);

            if (user != null) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, historiqueService.Create(historique));

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // ::::::::::::::::::::::::::::::::::Modifier Utilisateur
    // :::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Modification utilisateur en fournisssant id")
    @PostMapping("/updateUser/{id}")
    public ResponseEntity<Object> updateUtilisateur(@PathVariable Long id,
            @RequestParam(value = "user") String userVenant, @RequestParam(value = "data") String data) {

        Utilisateur utilisateur1 = utilisateurService.getById(id);

        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit updateuser = droitService.GetLibelle("Update Utilisateur");

            if (user != null) {
                if (user.getRole().getDroits().contains(updateuser)) {

                    if (utilisateur1 != null && utilisateur1.getId() == utilisateur.getId()) {

                        try {
                            Historique historique = new Historique();
                            Date datehisto = new Date();
                            historique.setDatehistorique(datehisto);
                            historique.setDescription("" + utilisateur1.getPrenom() + " " + utilisateur1.getNom()
                                    + " a modifié l'utilisateur " + utilisateur1.getPrenom());
                            historiqueService.Create(historique);
                        } catch (Exception e) {
                            // TODO: handle exception
                            return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                        }
                        return ResponseMessage.generateResponse("error", HttpStatus.OK,
                                utilisateurService.update(utilisateur));
                    } else {
                        return ResponseMessage.generateResponse("error", HttpStatus.OK,
                                "Vous n'êtes pas autorisé à supprimer");
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

    // ::::::::::::::::::::::Total nombre activite ::::::::::::::::::::::::
    @ApiOperation(value = "Nombre total d'activite")

    @PostMapping("/totalactivite")
    public ResponseEntity<Object> TotalActivite(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit readActivite = droitService.GetLibelle("Read Activite");

            if (user != null) {
                if (user.getRole().getDroits().contains(readActivite)) {
                    try {

                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a affiche toutes les activites ");
                        historiqueService.Create(historique);

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, activiteService.TotalActivite());

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK,
                                e.getMessage());

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK,
                        "Vous n'êtes pas autorisé à supprimer");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("errortt", HttpStatus.OK,
                    e.getMessage());
        }

    }

    // ::::::::::::::::::::::Afficher toutes les liste Postulant
    // ::::::::::::::::::::::::
    @ApiOperation(value = "Afficher toutes les listes Postulant ")
    @PostMapping("/AllListePost")
    public ResponseEntity<Object> AllListePost(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit readListe = droitService.GetLibelle("Read ListePostulant");

            if (user != null) {
                if (user.getRole().getDroits().contains(readListe)) {
                    try {

                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a affiche toutes les listes ");
                        historiqueService.Create(historique);

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, listePostulantService.getAll());

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK,
                                e.getMessage());

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK,
                        "Vous n'êtes pas autorisé à afficher tous les liste");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("errortt", HttpStatus.OK,
                    e.getMessage());
        }

    }

    @ApiOperation(value = "Afficher tirage par id")
    @PostMapping("/tirageById/{id}")
    public ResponseEntity<Object> getTirageById(@RequestParam(value = "user") String userVenant,
            @PathVariable long id) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit readTirage = droitService.GetLibelle("Read Tirage");

            if (user != null) {
                if (user.getRole().getDroits().contains(readTirage)) {
                    try {

                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a affiche un tirage");
                        historiqueService.Create(historique);

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, tirageService.getById(id));

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK,
                                e.getMessage());

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK,
                        "Vous n'êtes pas autorisé à afficher tous les liste");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("errortt", HttpStatus.OK,
                    e.getMessage());
        }

    }

    @ApiOperation(value = "Tout les participants")
    @PostMapping("/participants/all")
    public ResponseEntity<Object> Allparticipant(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit readAoup = droitService.GetLibelle("Read AouP");

            if (user != null) {
                if (user.getRole().getDroits().contains(readAoup)) {
                    try {

                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a affiche tout les participant");
                        historiqueService.Create(historique);

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, aPService.GetAll());

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK,
                                e.getMessage());

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK,
                        "Vous n'êtes pas autorisé à afficher tous les liste");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("errortt", HttpStatus.OK,
                    e.getMessage());
        }

    }

    @ApiOperation(value = "Tout les participants")
    @PostMapping("/postulantstires/{idTirage}")
    public ResponseEntity<Object> Allparticipant(@RequestParam(value = "user") String userVenant,
            @PathVariable("idTirage") Long idTirage) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit readAoup = droitService.GetLibelle("Read AouP");

            if (user != null) {
                if (user.getRole().getDroits().contains(readAoup)) {

                    try {
                        Tirage tirage = tirageService.getById(idTirage);

                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom()
                                        + " a affiche tout les participant du tirage" + idTirage);
                        historiqueService.Create(historique);

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, tirage.getPostulanttires());

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK,
                                e.getMessage());

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK,
                        "Vous n'êtes pas autorisé à afficher tous les liste");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("errortt", HttpStatus.OK,
                    e.getMessage());
        }

    }


    // ::::::::::::Supprimer apprenant ounpartcipant
    @ApiOperation(value = "Supprimer apprenant ou partcipant")
    @PostMapping("/DeleteApprenant/{idap}")
    public ResponseEntity<Object> DeleteApprenantOuPostulant(@RequestParam(value = "user") String userVenant,
                                                @PathVariable("idap") Long idap) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit readAoup = droitService.GetLibelle("Read AouP");

            if (user != null) {
                if (user.getRole().getDroits().contains(readAoup)) {

                    try {

                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom()
                                        + " a supprime l apprenant "+aPService.GetById(idap).getPostulant().getPrenom() );
                        historiqueService.Create(historique);
                        aPService.Delete(idap);
                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, "ok");

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK,
                                e.getMessage());

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK,
                        "Vous n'êtes pas autorisé à afficher tous les liste");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("errortt", HttpStatus.OK,
                    e.getMessage());
        }

    }

}

//
