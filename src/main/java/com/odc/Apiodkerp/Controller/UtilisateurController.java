package com.odc.Apiodkerp.Controller;

import com.odc.Apiodkerp.Models.*;

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

import ch.qos.logback.classic.pattern.Util;
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
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise !");
            }

        } else {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, "Login ou mots de passe incorrect !");
        }

        // } catch (Exception e) {
        // // TODO: handle exception
        // return ResponseMessage.generateResponse("error", HttpStatus.OK,
        // e.getMessage());
        // }
    }

    // Modification de l'activite
    @ApiOperation(value = "Modification de l'activite en fonction de l'id")
    @PutMapping("/update/activity/{id}/{login}/{password}")
    public ResponseEntity<Object> update(@PathVariable Long id, @PathVariable("login") String login,
            @PathVariable("password") String password,
            @RequestBody Activite activite,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Activite activite1 = activiteService.GetById(id);
            if (file != null) {
                activite.setImage(SaveImage.save("activite", file, activite.getNom()));
            }
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit updateActivite = droitService.GetLibelle("Update Actvite");

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
                            activiteService.Update(id, activite));
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
    @GetMapping("/afficherActivit/{id}/{login}/{password}")
    public ResponseEntity<Object> AfficherActivit(@PathVariable long id, @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            Activite activite1 = activiteService.GetById(id);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit readActivite = droitService.GetLibelle("Read Actvite");

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
    @DeleteMapping("/supprimeractivite/{idactivite}/{login}/{password}")
    public ResponseEntity<Object> supprimer(@PathVariable("idactivite") long idactivite,
            @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            Activite activite = activiteService.GetById(idactivite);

            Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(login, password);
            Droit deleteActivite = droitService.GetLibelle("Delete Actvite");

            if (utilisateur != null) {
                if (utilisateur.getRole().getDroits().contains(deleteActivite)) {
                    Role admin = RoleService.GetByLibelle("ADMIN");

                    if (activite.getCreateur() == utilisateur || utilisateur.getRole() == admin) {
                        Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
                        try {
                            Historique historique = new Historique();
                            Date datehisto = new Date();
                            historique.setDatehistorique(datehisto);
                            historique
                                    .setDescription(
                                            "" + user.getPrenom() + " " + user.getNom() + " a supprimé l'activite "
                                                    + activite.getNom());
                            historiqueService.Create(historique);
                        } catch (Exception e) {
                            // TODO: handle exception
                            return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                        }

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                                activiteService.Delete(idactivite));
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
    @GetMapping("/afficherActiviteEtat/{idetat}/{login}/{password}")
    public ResponseEntity<Object> AfficherActivite(@PathVariable Long idetat, @PathVariable("login") String login,
            @PathVariable("password") String password) {

        Etat etat = etatService.GetById(idetat);
        try {
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit readActivite = droitService.GetLibelle("Read Actvite");

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
    @GetMapping("/lapresence/{login}/{password}")
    public ResponseEntity<Object> ToutesActivite(@PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit readActivite = droitService.GetLibelle("Read Actvite");

            if (user != null) {
                if (user.getRole().getDroits().contains(readActivite)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a affiché toutes les activités ");
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, activiteService.FindAllAct());
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

    // Afficher une activite
    @ApiOperation(value = "Afficher une activite en fonction de l'id ")
    @GetMapping("/activite/{idactivite}/{login}/{password}")
    public ResponseEntity<Object> Afficheractivite(@PathVariable long idactivite, @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            Activite activite = activiteService.GetById(idactivite);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit readActivite = droitService.GetLibelle("Read Actvite");

            if (user != null) {
                if (user.getRole().getDroits().contains(readActivite)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a affiché  " + activite.getNom());
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
    @ApiOperation(value = "Creer la liste de presence ")
    @PostMapping("/lapresence/{idactivite}/{idpostulanttire}/{login}/{password}")
    public ResponseEntity<Object> presence(@PathVariable("idactivite") long idactivite,
            @PathVariable("idpostulanttire") long idpostulanttire, @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            Presence presence = new Presence();
            Activite activite = activiteService.GetById(idactivite);
            presence.setActivite(activite);
            presence.setDate(new Date());
            PostulantTire postulantTire = postulantTrieService.read(idpostulanttire);

            // presence.setPostulantTire(postulantTire);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit createpresence = droitService.GetLibelle("Create Presence");

            if (user != null) {
                if (user.getRole().getDroits().contains(createpresence)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a géré la presence de l'activité " + activite.getNom());
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, presenceService.creer(presence));
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
    @GetMapping("/lapresence/{idactivite}/{login}/{password}")
    public ResponseEntity<Object> Listepresence(@PathVariable long idactivite, @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            Activite act = activiteService.GetById(idactivite);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
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
    @PutMapping("/updateentite/{id}/{login}/{password}")
    public ResponseEntity<Object> updateEntite(@PathVariable("login") String login,
            @PathVariable("password") String password, @PathVariable Long id,
            @RequestBody Entite entite) {
        try {

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
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
    @PostMapping("/activite/new/{idsalle}/{idtype}/{login}/{password}")
    public ResponseEntity<Object> Createactivite(@RequestParam(value = "data") String acti,
            @PathVariable("login") String login,
            @PathVariable("password") String password, @PathVariable("idsalle") Long idsalle,
            @PathVariable("idtype") Long idtype,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        Activite activite = null;

        try {
            activite = new JsonMapper().readValue(acti, Activite.class);
            System.out.println(activite);

            if (file != null) {
                try {
                    Etat etat = etatService.recupereParStatut("A VENIR");
                    Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
                    Droit createType = droitService.GetLibelle("Create TypeActivite");

                    Salle salle = salleService.read(idsalle);
                    TypeActivite type = typeActiviteService.getById(idtype);

                    activite.setTypeActivite(type);
                    activite.setSalle(salle);
                    activite.setCreateur(user);
                    activite.setEtat(etat);
                    activite.setLeader(user);
                    activite.setDateCreation(new Date());
                    System.out.println(user);
                    // activite.setLeader(user);

                    activite.setImage(SaveImage.save("activite", file, activite.getNom()));

                    // ::::::::::::::::::::::::::::Historique ::::::::::::::::
                    // Utilisateur user = utilisateurService.getById(iduser);
                    if (user != null) {
                        if (user.getRole().getDroits().contains(createType)) {
                            try {
                                Historique historique = new Historique();
                                Date datehisto = new Date();
                                historique.setDatehistorique(datehisto);
                                historique
                                        .setDescription(
                                                "" + user.getPrenom() + " " + user.getNom() + " a crée l'activité "
                                                        + activite.getNom());
                                historiqueService.Create(historique);
                            } catch (Exception e) {
                                // TODO: handle exception
                                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                            }
                            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                                    activiteService.Create(activite));

                        } else {
                            return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

                        }

                    } else {
                        return ResponseMessage.generateResponse("error", HttpStatus.OK,
                                "Cet utilisateur n'existe pas !");

                    }

                } catch (Exception e) {

                    // TODO: handle exception
                    return ResponseMessage.generateResponse("errorTTTTTT", HttpStatus.OK, e.getMessage());
                }
            } else {

                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Fichier vide");
            }
        } catch (Exception e) {

            System.out.println(activite);

            return ResponseMessage.generateResponse("errorVVVVVVVVVV", HttpStatus.OK, e.getMessage());
        }

        // application/json

    }
    // ::::::::::::::::::::::::::::::::TYPE ACTIVITE
    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "methode pour la création d'une type d' activité.")
    @PostMapping("/TypeactiviteCreer/{login}/{password}")
    public ResponseEntity<Object> CreateTypeActivite(@RequestBody TypeActivite typeActivite,
            @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit createType = droitService.GetLibelle("Create TypeActivite");

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
    @PostMapping("/TypeactiviteSupprimer/{id}/{login}/{password}")
    public ResponseEntity<Object> SupprimerTypeActivite(@PathVariable long id, @PathVariable("login") String login,
            @PathVariable("password") String password,
            @RequestBody TypeActivite typeActivite) {

        try {

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit deleteType = droitService.GetLibelle("Delete TypeActivite");

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
    @PutMapping("/TypeactiviteModif/{login}/{password}")
    public ResponseEntity<Object> ModifTypeActivite(@RequestBody TypeActivite typeActivite,
            @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit updateType = droitService.GetLibelle("Update TypeActivite");

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
    @PostMapping("/historique/new/{login}/{password}")
    public ResponseEntity<Object> CreateHistorique(@RequestBody Historique historique,
            @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

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
    @PutMapping("/updateUser/{id}/{login}/{password}")
    public ResponseEntity<Object> updateUtilisateur(@PathVariable Long id, @PathVariable("login") String login,
            @PathVariable("password") String password, @RequestParam(value = "data") String data) {

        Utilisateur utilisateur1 = utilisateurService.getById(id);

        try {

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit updateuser = droitService.GetLibelle("Update Utilisateur");

            if (user != null) {
                if (user.getRole().getDroits().contains(updateuser)) {
                    Utilisateur utilisateur = new JsonMapper().readValue(data, Utilisateur.class);
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

    // ::::::::::::::::::::::Total activite ::::::::::::::::::::::::

    @ApiOperation(value = "Total activite")
    @GetMapping("/totalactivite/{login}/{password}")
    public ResponseEntity<Object> TotalActivite(@PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit readActivite = droitService.GetLibelle("Read Actvite");

            if (user != null) {
                if (user.getRole().getDroits().contains(readActivite)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a affiché toutes les activites ");
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, activiteService.TotalActivite());

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK,
                        "Vous n'êtes pas autorisé à supprimer");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

}

//
