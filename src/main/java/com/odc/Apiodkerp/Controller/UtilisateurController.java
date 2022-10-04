package com.odc.Apiodkerp.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    @PostMapping("/update/activity/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestParam(value = "activte") String activite, @RequestParam(value = "user") String userVenant,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Activite act = new JsonMapper().readValue(activite, Activite.class);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Activite activite1 = activiteService.GetById(id);
            if (file != null) {
                act.setImage(SaveImage.save("activite", file, act.getNom()));
            }

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
    public ResponseEntity<Object> AfficherActivit(@PathVariable long id, @RequestParam(value = "user") String userVenant) {
        try {
            Activite activite1 = activiteService.GetById(id);
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
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
    @PostMapping("/supprimeractivite/{idactivite}")
    public ResponseEntity<Object> supprimer(@PathVariable("idactivite") long idactivite,
                                       @RequestParam(value = "user") String userVenant) {
        try {
            Activite activite = activiteService.GetById(idactivite);
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);




            Droit deleteActivite = droitService.GetLibelle("Delete Actvite");

            if (utilisateur != null) {
                if (utilisateur.getRole().getDroits().contains(deleteActivite)) {
                    Role admin = RoleService.GetByLibelle("ADMIN");

                    if (activite.getCreateur() == utilisateur || utilisateur.getRole() == admin) {
                        Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),utilisateur.getPassword());
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
    @PostMapping("/afficherActiviteEtat/{idetat}")
    public ResponseEntity<Object> AfficherActivite(@PathVariable Long idetat,@RequestParam(value = "user") String userVenant) {

        Etat etat = etatService.GetById(idetat);
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
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
    @PostMapping("/allactivite")
    public ResponseEntity<Object> ToutesActivite( @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);


            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
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

    // Afficher une activite
    @ApiOperation(value = "Afficher une activite en fonction de l'id ")
    @GetMapping("/activite/{idactivite}")
    public ResponseEntity<Object> Afficheractivite(@PathVariable long idactivite,@RequestParam(value = "user") String userVenant) {
        try {
            Activite activite = activiteService.GetById(idactivite);
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
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
    @PostMapping("/lapresence/{idactivite}/{idpostulanttire}")
    public ResponseEntity<Object> presence(@PathVariable("idactivite") long idactivite,
            @PathVariable("idpostulanttire") long idpostulanttire, @RequestParam(value = "user") String userVenant) {
        try {
            Presence presence = new Presence();
            Activite activite = activiteService.GetById(idactivite);
            presence.setActivite(activite);
            presence.setDate(new Date());
            PostulantTire postulantTire = postulantTrieService.read(idpostulanttire);
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            // presence.setPostulantTire(postulantTire);

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
    @GetMapping("/lapresence/{idactivite}")
    public ResponseEntity<Object> Listepresence(@PathVariable long idactivite, @RequestParam(value = "user") String userVenant) {
        try {
            Activite act = activiteService.GetById(idactivite);
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
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
    @PutMapping("/updateentite/{id}")
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
    @PostMapping("/activite/new/{idsalle}/{idtype}")
    public ResponseEntity<Object> Createactivite(@RequestParam(value = "data") String acti,
                                          @RequestParam(value = "user") String userVenant, @PathVariable("idsalle") Long idsalle,
            @PathVariable("idtype") Long idtype,
            @RequestParam(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        Activite activite = null;
        Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

        try {
            activite = new JsonMapper().readValue(acti, Activite.class);
            System.out.println(activite);
        } catch (Exception e) {

            System.out.println(activite);

            return ResponseMessage.generateResponse("errorVVVVVVVVVV", HttpStatus.OK, e.getMessage());
        }
        if (file != null) {
            // try {
            Etat etat = etatService.recupereParStatut("A VENIR");
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
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

        } else {

            return ResponseMessage.generateResponse("error", HttpStatus.OK, "Fichier vide");
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
    @DeleteMapping("/TypeactiviteSupprimer/{id}/{login}/{password}")
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

    // ::::::::::::::::::::::Total nombre activite ::::::::::::::::::::::::
    @ApiOperation(value = "Modification utilisateur en fournisssant id")
    @PostMapping("/totalactivite")
    public ResponseEntity<Object> TotalActivite(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit readActivite = droitService.GetLibelle("Read Actvite");

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

}

//
