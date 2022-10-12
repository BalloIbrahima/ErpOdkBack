package com.odc.Apiodkerp.Controller;

import com.odc.Apiodkerp.Models.*;
import com.odc.Apiodkerp.Service.*;
import com.odc.Apiodkerp.ServiceImplementation.EmailDetailsInterf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Configuration.SaveImage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/admin")
@Api(value = "admin", description = "Les fonctionnalités liées à un super administrateur, le grand chef.")
@CrossOrigin
public class SuperAdminController {
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
    private IntervenantExterneService intervenantExterneService;

    // ---------------------------CRUD
    // USER-------------------------------------------------------------->
    @ApiOperation(value = "Creer un utilisateur.")
    @PostMapping("/create/user")
    public ResponseEntity<Object> createUser(@RequestParam(value = "data") String data,
            @RequestParam(value = "user") String userVenant,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {

            Utilisateur utilisateu = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur utilisateur = new JsonMapper().readValue(data, Utilisateur.class);

            // Role role = RoleService.GetByLibelle("USER");

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateu.getLogin(),
                    utilisateu.getPassword());
            Droit CUser = droitService.GetLibelle("Create Utilisateur");

            if (users != null) {
                if (users.getRole().getDroits().contains(CUser)) {

                    if (utilisateurService.getByEmail(utilisateur.getEmail()) == null) {
                        // utilisateur.setRole(role);

                        String pass = utilisateur.getNom().substring(0, 1) + utilisateur.getPrenom().substring(0, 1)
                                + "@ODC2022";

                        String login = utilisateur.getNom().substring(0, 1) + utilisateur.getPrenom().substring(0, 1)
                                + utilisateur.getNom();

                        System.out.println(pass);

                        utilisateur.setPassword(pass);
                        utilisateur.setLogin(login);

                        if (file != null) {
                            utilisateur.setImage(SaveImage.save("user", file, utilisateur.getEmail()));
                        }

                        // Historique
                        try {
                            Historique historique = new Historique();
                            Date datehisto = new Date();
                            historique.setDatehistorique(datehisto);
                            historique.setDescription("" + users.getPrenom() + " " + users.getNom()
                                    + " a cree un utilisateur du nom de " + utilisateur.getNom());
                            historiqueService.Create(historique);

                            Utilisateur NewUser = utilisateurService.creer(utilisateur);
                            System.out.println(NewUser.getLogin());
                            return ResponseMessage.generateResponse("ok", HttpStatus.OK, NewUser);
                        } catch (Exception e) {
                            // TODO: handle exception
                            return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                        }

                    } else {
                        return ResponseMessage.generateResponse("error", HttpStatus.OK, "Adresse mail existante");

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

    @ApiOperation(value = "Modifier un utilisateur.")
    @PostMapping("/update/user/{idadmin}/{id}")
    public ResponseEntity<Object> updateUser(@RequestParam(value = "user") String userVenant,
            @PathVariable long idadmin,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            if (file != null) {
                SaveImage.save("user", file, utilisateur.getEmail());
            }

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit UUser = droitService.GetLibelle("Update Utilisateur");

            if (users != null) {
                if (users.getRole().getDroits().contains(UUser)) {

                    // Historique
                    // Utilisateur user = utilisateurService.trouverParLoginAndPass(login,
                    // password);
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + users.getPrenom() + " " + users.getNom()
                                        + " a modifie un utilisateur du nom de ");
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

                    long userid = utilisateur.getId();
                    Utilisateur UpdateUtilisateur = utilisateurService.update(utilisateur);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, UpdateUtilisateur);

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

    @ApiOperation(value = "Supprimer un utilisateur")
    @PostMapping("/delete/user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id,
            @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            // Historique
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Du = droitService.GetLibelle("Delete Utilisateur");

            if (user != null) {
                if (user.getRole().getDroits().contains(Du)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a supprime un utilisateur du nom de " + utilisateurService.getById(id));
                        historiqueService.Create(historique);
                        utilisateurService.delete(id);

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);
                }

                else {
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

    @ApiOperation(value = "Affichager tout les utilisateurs")
    @PostMapping("/getAll/user")
    public ResponseEntity<Object> GetAllUser(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Ruser = droitService.GetLibelle("Read Utilisateur");

            if (user != null) {
                if (user.getRole().getDroits().contains(Ruser)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + "a affiche tous les utilisateurs");
                        historiqueService.Create(historique);

                        System.out.println(historique.getDescription());

                        Role uRole = RoleService.GetByLibelle("USER");
                        List<Utilisateur> getAllUtilisateur = utilisateurService.RetrouverParRole(uRole);
                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, getAllUtilisateur);

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

    @ApiOperation(value = "Affichager un utilisateur")
    @PostMapping("/get/user/{id}")
    public ResponseEntity<Object> GetIdUtilisateur(@PathVariable("id") Long id,
            @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit Ruser = droitService.GetLibelle("Read Utilisateur");

            if (user.getRole().getDroits().contains(Ruser)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                            + " a afficher  un utilisateur du nom de " + utilisateurService.getById(id));
                    historiqueService.Create(historique);
                    Utilisateur idUser = utilisateurService.getById(id);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, idUser);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // ---------------------------CRUD
    // Responsable-------------------------------------------------------------->
    @ApiOperation(value = "Creer un responsable.")
    @PostMapping("/create/responsable")
    public ResponseEntity<Object> createResponsable(@RequestParam(value = "data") String data,
            @RequestParam(value = "user") String userVenant,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            Utilisateur admin = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit Ruser = droitService.GetLibelle("Create Utilisateur");

            if (admin.getRole().getDroits().contains(Ruser)) {

                utilisateur = new JsonMapper().readValue(data, Utilisateur.class);

                Utilisateur verif = utilisateurService.getByEmail(utilisateur.getEmail());

                if (verif == null) {
                    Role role = RoleService.GetByLibelle("RESPONSABLE");
                    utilisateur.setRole(role);
                    String pass = utilisateur.getNom().substring(0, 1) + utilisateur.getPrenom().substring(0, 1)
                            + "@ODC2022";

                    System.out.println(pass);

                    utilisateur.setPassword(pass);
                    System.out.println(file);
                    if (file != null) {
                        utilisateur.setImage(SaveImage.save("user", file, utilisateur.getEmail()));
                        // System.out.println(utilisateur.getImage());

                    }

                    // Histoirque
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + admin.getPrenom() + " " + admin.getNom() + " a cree un responsable du nom de "
                                        + utilisateur.getNom() + " " + utilisateur.getPrenom());
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

                    Utilisateur NewResponsable = utilisateurService.creer(utilisateur);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, NewResponsable);
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Utilisateur existe deja");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "Modifier un responsable.")
    @PostMapping("/update/responsable/{idResponsable}")
    public ResponseEntity<Object> updateResponsable(@RequestParam(value = "data") String data,
            @PathVariable("idResponsable") Long idResponsable, @RequestParam(value = "user") String userVenant,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Utilisateur utilisat = new JsonMapper().readValue(userVenant, Utilisateur.class);
            Utilisateur utilisateur = new JsonMapper().readValue(data, Utilisateur.class);
            Utilisateur responsable = utilisateurService.getById(idResponsable);

            Utilisateur admin = utilisateurService.trouverParLoginAndPass(utilisat.getLogin(), utilisat.getPassword());

            Droit updateResponsable = droitService.GetLibelle("Update Utilisateur");

            if (admin.getRole().getDroits().contains(updateResponsable) || admin == responsable) {
                if (responsable != null && responsable.getId() == utilisateur.getId()) {
                    if (file != null) {
                        utilisateur.setImage(SaveImage.save("user", file, utilisateur.getEmail()));
                    }
                    System.out.println(utilisateur.getPassword());
                    Utilisateur UpdateResponsable = utilisateurService.update(utilisateur);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, UpdateResponsable);
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "Supprimer un responsable")
    @PostMapping("/delete/responsable/{idResponsable}")
    public ResponseEntity<Object> deleteResponsable(@RequestParam(value = "user") String userVenant,
            @PathVariable("idResponsable") Long idResponsable) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur admin = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            if (admin.getRole() == RoleService.GetByLibelle("ADMIN")) {

                Utilisateur respon = utilisateurService.getById(idResponsable);

                Droit deleteResponsable = droitService.GetLibelle("Delete Utilisateur");

                if (admin.getRole().getDroits().contains(deleteResponsable)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + admin.getPrenom() + " " + admin.getNom()
                                + " a supprime un responsable du nom de " + respon.getNom() + " " + respon.getPrenom());
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    utilisateurService.delete(idResponsable);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    @ApiOperation(value = "Affichager toute les responsables")
    @PostMapping("/getAll/responsable")
    public ResponseEntity<Object> GetAllResponsable(@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit readUser = droitService.GetLibelle("Read Utilisateur");

            if (user.getRole().getDroits().contains(readUser)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            "" + user.getPrenom() + " " + user.getNom() + " a affiche tous les responsables");
                    historiqueService.Create(historique);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }
                Role reponsable = RoleService.GetByLibelle("RESPONSABLE");
                List<Utilisateur> getAllResponsable = utilisateurService.RetrouverParRole(reponsable);
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, getAllResponsable);
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    @ApiOperation(value = "Affichager un responsable")
    @PostMapping("/get/responsable/{id}")
    public ResponseEntity<Object> GetIdResponsable(@PathVariable("id") Long id,
            @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur idResponsable = utilisateurService.getById(id);
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur admin = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit readUser = droitService.GetLibelle("Read Utilisateur");

            if (admin.getRole().getDroits().contains(readUser)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            "" + admin.getPrenom() + " " + admin.getNom() + " a affiche le responsable du nom de "
                                    + idResponsable.getNom() + " " + idResponsable.getPrenom());
                    historiqueService.Create(historique);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, idResponsable);
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // ---------------------------CRUD

    /// active un utilisateur
    @ApiOperation(value = "Active un utilisateur")
    @PostMapping("/active/{idUser}")
    ResponseEntity<Object> activeUser(@RequestParam(value = "user") String userVenant,
            @PathVariable("idUser") Long idUser) {

        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            Utilisateur admin = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit updateuser = droitService.GetLibelle("Update Utilisateur");

            if (admin.getRole().getDroits().contains(updateuser)) {

                Utilisateur user = utilisateurService.getById(idUser);
                user.setActive(true);

                // Historique
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription("" + admin.getPrenom() + " " + admin.getNom()
                            + " a activé un utilisateur du nom de " + user.getNom() + " " + user.getPrenom());
                    historiqueService.Create(historique);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }
                // System.out.println(user.getPassword());
                // user.setPassword("");

                return ResponseMessage.generateResponse("ok", HttpStatus.OK, utilisateurService.modifierRole(user));

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    /// desactive un utilisateur
    @ApiOperation(value = "Desactive un utilisateur")
    @PostMapping("/desactive/{idUser}")
    ResponseEntity<Object> desactiveUser(@RequestParam(value = "user") String userVenant,
            @PathVariable("idUser") Long idUser) {

        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur admin = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit updateuser = droitService.GetLibelle("Update Utilisateur");

            if (admin.getRole().getDroits().contains(updateuser)) {
                Utilisateur user = utilisateurService.getById(idUser);
                user.setActive(false);

                // Historique
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription("" + admin.getPrenom() + " " + admin.getNom()
                            + " a desactivé un utilisateur du nom de " + user.getNom() + " " + user.getPrenom());
                    historiqueService.Create(historique);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }
                // System.out.println(user.getPassword());
                // user.setPassword("");
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, utilisateurService.modifierRole(user));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // :::::::::::::::total postulant ::::::::::::::::::::

    @ApiOperation(value = "Total postulant")
    @PostMapping("/totalpostulant")
    public ResponseEntity<Object> TotalPostulant(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur users = utilisateurService.getById(utilisateur.getId());

            Droit readuser = droitService.GetLibelle("Read Utilisateur");

            if (users.getRole().getDroits().contains(readuser)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            "" + users.getPrenom() + " " + users.getNom() + " a affiche tous les postulants ");
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, utilisateurService.TotalPersonnel());

                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    // tOTAL PERSONNEL ACTIVE

    //// :::::::::::::::::::::::::::::::l'ensemble des activites en cour, à venir ,
    //// termine

    // activités en avenir
    @ApiOperation(value = "activites/avenir")

    @PostMapping("/activites/avenir")
    public ResponseEntity<Object> ActivitesAvenir(@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Ractivite = droitService.GetLibelle("Read Activite");

            if (users != null) {
                if (users.getRole().getDroits().contains(Ractivite)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + users.getPrenom() + " " + users.getNom() + " a affiche les activites a venir");
                        historiqueService.Create(historique);

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, activiteService.Avenir());

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

                }
            }

            else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // activités en cour
    @ApiOperation(value = "activites/encour")

    @PostMapping("/activites/encour")
    public ResponseEntity<Object> ActivitesEncour(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit Ractivite = droitService.GetLibelle("Read Activite");

            if (users != null) {
                if (users.getRole().getDroits().contains(Ractivite)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + users.getPrenom() + " " + users.getNom() + " a affiche les activites en cour ");
                        historiqueService.Create(historique);

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, activiteService.Encour());
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // activités termines
    @ApiOperation(value = "activites/termines")
    @PostMapping("activites/termines")
    public ResponseEntity<Object> ActivitesTermines(@RequestParam(value = "user") String userVenant) {

        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit Ractivite = droitService.GetLibelle("Read Activite");

            if (users != null) {
                if (users.getRole().getDroits().contains(Ractivite)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + users.getPrenom() + " " + users.getNom() + " a affiche les activites terminees ");
                        historiqueService.Create(historique);

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, activiteService.Termine());
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }
    // :::::::::::::: ::::::::::::::debut mise a jour de madame ::::::::::::::::::::

    // :::::::::::Liste des activites par entite ::::::::::::::::
    @ApiOperation(value = "activites par entite")
    @PostMapping("activites/entite/{identite}")
    public ResponseEntity<Object> ActivitesParEntite(@PathVariable long identite,
            @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit Ractivite = droitService.GetLibelle("Read Activite");

            if (users != null) {
                if (users.getRole().getDroits().contains(Ractivite)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + users.getPrenom() + " " + users.getNom() + " a affiche les activites par entite ");
                        historiqueService.Create(historique);
                        return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                                activiteService.ActiviteEntiteid(identite));

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

                }

                else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // :::::::::::::::::::Liste des activites par intervalle de date :
    // ::::::::::::::::::::
    // fait avec ballo le 26/09

    // ::::::::::::La liste des participants par activité et par intervalle de date,

    @ApiOperation(value = "liste participant par activite")
    @PostMapping("activites/entite/{idactivite}/{date1}/{date2}")
    public ResponseEntity<Object> PostulantParActivite(@RequestParam(value = "user") String userVenant,
            @PathVariable long idactivite, @PathVariable Date date1, @PathVariable Date date2) {
        try {

            // recupere les activites par identifiant
            Activite activite = activiteService.GetById(idactivite);
            // recupere tous les postTirés
            PostulantTire pt = (PostulantTire) postulantTrieService.getAll();

            // recupere tous les tirages vue qu'il est le lien entre postTirer et activite
            Tirage tirage = (Tirage) tirageService.getAll();

            // On recupere les activites par intervalles de temps
            if (activite.getDateDebut().after(date1) && activite.getDateDebut().before(date2)
                    && activite.getDateFin().before(date2)) {

                if (tirage.getListepostulant().getActivite().getId() == activite.getId()
                        && pt.getTirage().getId() == tirage.getId()) {

                    // :::::::::::::::::::::::::::::Histroque::::::::::::::::::::::::::::::::::

                    Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

                    Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                            utilisateur.getPassword());

                    Droit Rpost = droitService.GetLibelle("Read postulant");

                    if (users != null) {
                        if (users.getRole().getDroits().contains(Rpost)) {
                            try {
                                Historique historique = new Historique();
                                Date datehisto = new Date();
                                historique.setDatehistorique(datehisto);
                                historique.setDescription("" + users.getPrenom() + " " + users.getNom()
                                        + " a affiche des postulant de l'activite " + activite.getNom());
                                historiqueService.Create(historique);
                            } catch (Exception e) {
                                // TODO: handle exception
                                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                            }
                            return ResponseMessage.generateResponse("error", HttpStatus.OK,
                                    postulantTrieService.getAll());
                        } else {
                            return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");

                        }
                    } else {
                        return ResponseMessage.generateResponse("error", HttpStatus.OK,
                                "Cet utilisateur n'existe pas !");

                    }
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "");

                }
            } else {
                return ResponseMessage.generateResponse("Erreur", HttpStatus.OK, "");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // La liste des activités par entité et par statut_activité (encours, à venir ou
    // terminée).

    @ApiOperation(value = "activites par entite et par statut")
    @PostMapping("activites/entite/{identite}/{idstatut}")
    public ResponseEntity<Object> ActivitesParEntiteEtParstatut(@RequestParam(value = "user") String userVenant,
            @PathVariable long identite, @PathVariable long idstatut) {
        try {
            Etat etat = etatService.GetById(idstatut);// on recup l'etat en fonction de l'id
            Entite entite = entiteService.GetById(identite);
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit Rentite = droitService.GetLibelle("Read Entite");

            if (users != null) {
                if (users.getRole().getDroits().contains(Rentite)) {

                    // activite de l'estat forni
                    List<Activite> etatActivite = etat.getActivite();
                    List<Activite> aRetourner = new ArrayList<>();
                    for (Activite a : etatActivite) {
                        if (a.getCreateur().getMonEntite() == entite) {
                            aRetourner.add(a);
                        }
                    }

                    Activite activite = (Activite) activiteService.ActiviteEntiteid(identite);// recuperation des
                                                                                              // activite d'une entite
                                                                                              // donnée
                    if (activite.getEtat() == etat.getActivite()) {
                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, activiteService.GetAll());
                    } else {
                        return ResponseMessage.generateResponse("error", HttpStatus.OK, "Une erreur s'est produit");

                    }
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }
        }

        catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // ::::::::::::::::::::::
    @ApiOperation(value = "Statut d'une activite en fonction de son id")
    @PostMapping("statut/activite/{id}")

    public ResponseEntity<Object> ActivitesTermines(@PathVariable long id,
            @RequestParam(value = "user") String userVenant) {

        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Ractivite = droitService.GetLibelle("Read Activite");

            if (user != null) {
                if (user.getRole().getDroits().contains(Ractivite)) {

                    Activite act = new Activite();
                    act = activiteService.GetById(id);
                    Date dateTodate = new Date();
                    if (dateTodate.after(act.getDateDebut()) && dateTodate.before(act.getDateFin())) {
                        return ResponseMessage.generateResponse("En cours", HttpStatus.OK, "L'activité est en cours");
                    } else if (dateTodate.after(act.getDateFin())) {
                        return ResponseMessage.generateResponse("Termine", HttpStatus.OK, "L'activité est terminée");
                    } else if (dateTodate.before(act.getDateDebut())) {
                        return ResponseMessage.generateResponse("A Venir", HttpStatus.OK, "L'activité est à Venir");
                    } else {
                        return ResponseMessage.generateResponse("error", HttpStatus.OK, "");
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

    // :::::::::::::::::::::::Les users active
    @ApiOperation(value = "Les utilisateurs active")
    @PostMapping("/getUsers/active")
    public ResponseEntity<Object> getUsersActives(@RequestParam(value = "user") String userVenant) {
        try {
            // hisorique
            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());

            Droit Ruser = droitService.GetLibelle("Read Utilisateur");

            try {

                if (user.getRole().getDroits().contains(Ruser)) {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            "" + user.getPrenom() + " " + user.getNom() + " a afficher  un utilisateur active");
                    historiqueService.Create(historique);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            utilisateurService.RecupererUserParEtat(true));

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");
                }

            } catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // :::::::::::::::::::::::Les users active
    @ApiOperation(value = "Les utilisateurs")
    @PostMapping("/getUsers/all")
    public ResponseEntity<Object> getUsers(@RequestParam(value = "user") String userVenant) {
        try {
            // hisorique
            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());

            Droit Ruser = droitService.GetLibelle("Read Utilisateur");

            try {

                if (user.getRole().getDroits().contains(Ruser)) {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            "" + user.getPrenom() + " " + user.getNom() + " a afficher  un utilisateur active");
                    historiqueService.Create(historique);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            utilisateurService.getAll());

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");
                }

            } catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // :::::::::::::::::::::::Les users desactives
    @ApiOperation(value = "Les utilisateurs desactives")
    @PostMapping("/getUsers/desactive")
    public ResponseEntity<Object> getUsersDesactives(@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit Ruser = droitService.GetLibelle("Read Utilisateur");

            if (user.getRole().getDroits().contains(Ruser)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            "" + user.getPrenom() + " " + user.getNom() + " a afficher  un utilisateur desactive");
                    historiqueService.Create(historique);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        utilisateurService.RecupererUserParEtat(false));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exceptroleion
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
    //

    // :::::::::::::::::::::::Les salles disponible
    @ApiOperation(value = "Les salles disponible")
    @PostMapping("/getSalles/disponible")
    public ResponseEntity<Object> getSallesDispo(@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit Rsalle = droitService.GetLibelle("Read Salle");

            if (user.getRole().getDroits().contains(Rsalle)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            "" + user.getPrenom() + " " + user.getNom() + " a afficher  les salles disponible");
                    historiqueService.Create(historique);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, salleService.ParEtat(true));

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // :::::::::::::::::::::::::::::::::::::::Apprenants ou Participant
    // :::::::::::::::::::::::::::::::
    @ApiOperation(value = "Ajouter AppouParticipant")
    @PostMapping("/aoup/{idActivite}")
    public ResponseEntity<Object> ajouterAouP(@RequestParam(value = "aoup") String aoup,
            @RequestParam(value = "user") String userVenant, @PathVariable("idActivite") Long idActivite) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Postulant p = new JsonMapper().readValue(aoup, Postulant.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Caoup = droitService.GetLibelle("Create AouP");

            if (user.getRole().getDroits().contains(Caoup)) {
                try {

                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            "" + user.getPrenom() + " " + user.getNom()
                                    + " a enregistre des apprenants ou participants");
                    historiqueService.Create(historique);

                    Activite ac = activiteService.GetById(idActivite);
                    Postulant createP = postulantService.creer(p);

                    AouP aou = new AouP();
                    aou.setActivite(ac);
                    aou.setPostulant(createP);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            aouPService.Create(aou));
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // ::::::::::::::::::::::::::::::::::: Modifier AouP
    // ::::::::::::::::::::::::::::::::::::
    @ApiOperation(value = "Modifier AppouParticipant")
    @PostMapping("/aoup/modifier/{id}")
    public ResponseEntity<Object> ModifierAouP(@PathVariable long id, @RequestParam(value = "aoup") String aoup,
            @RequestParam(value = "user") String userVenant) {

        try {
            AouP aouP = aouPService.GetById(id);

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            AouP aou = new JsonMapper().readValue(aoup, AouP.class);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit Uaoup = droitService.GetLibelle("Update AouP");

            if (user.getRole().getDroits().contains(Uaoup)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                            + " a modifie l apprenants ou le participant sur lactivite " + aouP.getActivite());
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            aouPService.Update(id, aou));
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
    // ::::::::::::::::::::::::::::::::::: Supprimer AouP
    // ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Supprimer AouP")
    @PostMapping("/aoup/supprimer/{id}")
    public ResponseEntity<Object> SupprimerAouP(@PathVariable long id,
            @RequestParam(value = "user") String userVenant) {

        try {
            AouP aouP = aouPService.GetById(id);
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit Daoup = droitService.GetLibelle("Delete AouP");

            if (user.getRole().getDroits().contains(Daoup)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                            + " a supprime l apprenants ou le participant sur lactivite " + aouP.getActivite());
                    historiqueService.Create(historique);
                    aouPService.Delete(id);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,"ok");
                           
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // ::::::::::::::::::::::::::::::::::: Get par id AouP
    // ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Apprenant ou participant par id")
    @PostMapping("/aoup/GetId/{id}")
    public ResponseEntity<Object> GetAouPparId(@PathVariable long id, @RequestParam(value = "user") String userVenant) {

        try {
            // Histroique
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Raoup = droitService.GetLibelle("Read AouP");

            if (user.getRole().getDroits().contains(Raoup)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            "" + user.getPrenom() + " " + user.getNom() + " a affiche un apprenant ou un participant ");
                    historiqueService.Create(historique);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        aouPService.GetById(id));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // :::::::::::::::::::::::::::::::::::::::Droit :::::::::::::::::::::::::::::::
    @ApiOperation(value = "Ajouter Droit")
    @PostMapping("/droit/new")
    public ResponseEntity<Object> ajouterDroit(@RequestParam(value = "droit") String droit,
            @RequestParam(value = "user") String userVenant) {
        try {
            // Histroique
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Droit drt = new JsonMapper().readValue(droit, Droit.class);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Cdroit = droitService.GetLibelle("Create Role");

            if (user.getRole().getDroits().contains(Cdroit)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            "" + user.getPrenom() + " " + user.getNom() + " a ajoute le droit " + drt.getLibelle());
                    historiqueService.Create(historique);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            droitService.Create(drt));
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // ::::::::::::::::::::::::::::::::::: Modifier Droit
    // ::::::::::::::::::::::::::::::::::::
    @ApiOperation(value = "Modifier Droit")
    @PostMapping("/droit/modifier/{id}")
    public ResponseEntity<Object> ModifierDroit(@PathVariable long id, @RequestParam(value = "droit") String droit,
            @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Droit drt = new JsonMapper().readValue(droit, Droit.class);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            // Histroique

            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription(
                        "" + user.getPrenom() + " " + user.getNom() + " a modifie le droit " + drt.getLibelle());
                historiqueService.Create(historique);
            } catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    droitService.Update(id, drt));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
    // ::::::::::::::::::::::::::::::::::: Supprimer droit
    // ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Supprimer Droit")
    @PostMapping("/droit/supprimer/{id}")
    public ResponseEntity<Object> SupprimerDroit(@PathVariable long id,
            @RequestParam(value = "user") String userVenant) {
        try {

            // Histroique
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription("" + user.getPrenom() + " " + user.getNom() + " a supprime un droit ");
                historiqueService.Create(historique);
            } catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    droitService.Delete(id));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
    // ::::::::::::::::::::::::::::::::::: Get par id AouP
    // ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Droit par id")
    @PostMapping("/droit/GetId/{id}")
    public ResponseEntity<Object> GetDroitparId(@PathVariable long id,
            @RequestParam(value = "user") String userVenant) {
        try {
            Droit droit = droitService.GetById(id);
            // Histroique
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription(
                        "" + user.getPrenom() + " " + user.getNom() + " a affiche le droit " + droit.getLibelle());
                historiqueService.Create(historique);
            } catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }

            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    droitService.GetById(id));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // :::::::::::::::::::::::::::::::::::::::::::::Tous les droit
    // :::::::::::::::::::::::
    @ApiOperation(value = "Tous les droits")
    @PostMapping("/droit/Getall")
    public ResponseEntity<Object> GetToutDroit(@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            // Histroique
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription(
                        "" + user.getPrenom() + " " + user.getNom() + " a affiche tous les  droits ");
                historiqueService.Create(historique);
            } catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }

            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    droitService.GetAll());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // :::::::::::::::::::::::::::Droit par libelle
    // ::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Droit par libelle")
    @PostMapping("/droit/GetLibelle/{libelle}")
    public ResponseEntity<Object> GetDroitparLibelle(@PathVariable String libelle,
            @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique
                        .setDescription("" + user.getPrenom() + " " + user.getNom() + " a affiche le droit " + libelle);
                historiqueService.Create(historique);
            } catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    droitService.GetLibelle(libelle));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    @ApiOperation(value = "Nombre de participants feminin")
    @PostMapping("/partcipantfeminins")
    public ResponseEntity<Object> participantsFeminins(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit RAoup = droitService.GetLibelle("Read AouP");

            if (user.getRole().getDroits().contains(RAoup)) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        aouPService.listFeminins());
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    @ApiOperation(value = "Nombre de participants enfants")
    @PostMapping("/partcipantenfants")
    public ResponseEntity<Object> participantsEnfants(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit RAoup = droitService.GetLibelle("Read AouP");

            if (user.getRole().getDroits().contains(RAoup)) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        aouPService.listEnfants());
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // ROLE===============================================
    @ApiOperation(value = "Creer un role.")
    @PostMapping("/create/role")
    public ResponseEntity<Object> createRole(@RequestParam(value = "role") String role,
            @RequestParam(value = "user") String userVenant) {
        try {

            Role role1 = new JsonMapper().readValue(role, Role.class);

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit createrole = droitService.GetLibelle("Create Role");

            if (user.getRole().getDroits().contains(createrole)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique
                            .setDescription(
                                    "" + user.getPrenom() + " " + user.getNom() + " a cree  une nouvelle role ");
                    historiqueService.Create(historique);
                    Role NewEntite = roleService.create(role1);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, NewEntite);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Afficher tous les roles")
    @PostMapping("/role/getAll")
    public ResponseEntity<Object> GetAllEntite(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit getRole = droitService.GetLibelle("Read Role");

            if (user.getRole().getDroits().contains(getRole)) {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique
                        .setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a affiche les roles ");
                historiqueService.Create(historique);
                List<Role> getAllRole = roleService.getAll();
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, getAllRole);
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    // ::::::::::::::Modifier Role ::::::::::::::::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Modifier un role.")
    @PostMapping("/update/role/{idrole}")
    public ResponseEntity<Object> UpdateRole(@PathVariable long idrole,@RequestParam(value = "role") String role,
                                             @RequestParam(value = "user") String userVenant) {
        try {

            Role role1 = new JsonMapper().readValue(role, Role.class);

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit createrole = droitService.GetLibelle("Update Role");

            if (user.getRole().getDroits().contains(createrole)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique
                            .setDescription(
                                    "" + user.getPrenom() + " " + user.getNom() + " a modifier  le  role "+role1.getLibellerole());
                    historiqueService.Create(historique);
                    Role NewEntite = roleService.update(role1,idrole);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, NewEntite);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Recuperer un role par son id.")
    @PostMapping("/getrole/{idrole}")
    public ResponseEntity<Object> getRole(@PathVariable long idrole,
                                             @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit createrole = droitService.GetLibelle("Read Role");

            if (user.getRole().getDroits().contains(createrole)) {
                try {
                    Role role=roleService.read(idrole);

                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique
                            .setDescription(
                                    "" + user.getPrenom() + " " + user.getNom() + " a recuperer  le  role "+role.getLibellerole());
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, role);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    // ::::::::::::::::::::::::::::::::Supprimer role ::::::::::::::::::::::::::::::::::
    // ::::::::::::::Modifier Role ::::::::::::::::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Supprimer un role.")
    @PostMapping("/Delete/role/{idrole}")
    public ResponseEntity<Object> DeleteRole(@PathVariable long idrole,
                                             @RequestParam(value = "user") String userVenant) {
        try {


            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Deleterole = droitService.GetLibelle("Delete Role");

            if (user.getRole().getDroits().contains(Deleterole)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique
                            .setDescription(
                                    "" + user.getPrenom() + " " + user.getNom() + " a Supprimer  un  role ");
                    historiqueService.Create(historique);
                    Role NewEntite = roleService.delete(idrole);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, NewEntite);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }


    // ::::::::::::::::::::::::Toutes les tirages :::::::::::::::::::::::::::::::
    @ApiOperation(value = "Toutes les tirages")
    @PostMapping("/TouteslesTirages")
    public ResponseEntity<Object> TouteslesTirages(@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit ReadTirage = droitService.GetLibelle("Read Tirage");

            if (user.getRole().getDroits().contains(ReadTirage)) {

                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique
                        .setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a afficher les tirages ");
                historiqueService.Create(historique);
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        tirageService.getAll());
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    /// ::::::::::::::::::::::::::activite par entite
    @ApiOperation(value = "activite par entite")
    @PostMapping("/entiteActivites/{identite}")
    public ResponseEntity<Object> EntitesActivite(@PathVariable("identite") Long identite,
            @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Entite entite = entiteService.GetById(identite);

            List<Activite> activites = activiteService.FindAllAct();

            Droit REntite = droitService.GetLibelle("Read Entite");

            List<Activite> ActiviteEntite = new ArrayList<>();

            if (user.getRole().getDroits().contains(REntite)) {

                for (Activite a : activites) {
                    if (a.getCreateur().getGererEntite() == entite) {
                        ActiviteEntite.add(a);
                    }
                }

                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique
                        .setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a affiche les activites par entites ");
                historiqueService.Create(historique);

                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        ActiviteEntite);
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    /// ::::::::::::::::::::::::::personnel par entite
    @ApiOperation(value = "personnel par entite")
    @PostMapping("/entitepersonnels/{identite}")
    public ResponseEntity<Object> EntitesPersonnel(@PathVariable("identite") Long identite,
            @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Entite entite = entiteService.GetById(identite);

            // List<Activite> activites=activiteService.FindAllAct();

            Droit REntite = droitService.GetLibelle("Read Utilisateur");

            // List<Activite> ActiviteEntite=new ArrayList<>();

            if (user.getRole().getDroits().contains(REntite)) {

                // for(Activite a:activites){
                // if(a.getCreateur().getGererEntite()==entite){
                // ActiviteEntite.add(a);
                // }
                // }

                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique
                        .setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a afficher les personnels par entite ");
                historiqueService.Create(historique);

                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        entite.getUtilisateurEntite());
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    @ApiOperation(value = "Les partcipants d'une activite donnée ")
    @PostMapping("/ParticipantParActivte/{idactivite}")
    public ResponseEntity<Object> ParticipantParActivte(@PathVariable long idactivite,
            @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Activite act = activiteService.GetById(idactivite);
            Droit RAoup = droitService.GetLibelle("Read AouP");
            List<AouP> aoup = aouPService.GetAll();
            List<AouP> listearetourner = new ArrayList<>();

            if (user.getRole().getDroits().contains(RAoup)) {
                for (AouP aou : aoup) {
                    if (aou.getActivite() == act.getAoup()) {
                        listearetourner.add(aou);

                    }
                    ;
                }

                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique
                        .setDescription(
                                "" + user.getPrenom() + " " + user.getNom()
                                        + " a afficher les participant une activite ");
                historiqueService.Create(historique);

                return ResponseMessage.generateResponse("ok", HttpStatus.OK, listearetourner);
                // return ResponseMessage.generateResponse("error", HttpStatus.OK, "");

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    /// ::::::::::::::::::::::::::apprenants par entite
    @ApiOperation(value = "apprenants par entite")
    @PostMapping("/entiteapprenantss/{identite}")
    public ResponseEntity<Object> EntitesApprenants(@PathVariable("identite") Long identite,
            @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            // Entite entite = entiteService.GetById(identite);

            List<Activite> activites = activiteService.ActiviteEntiteid(identite);

            Droit RAouP = droitService.GetLibelle("Read AouP");

            // List<Activite> ActiviteEntite = new ArrayList<>();

            List<AouP> apprenants=new ArrayList<>();

            if (user.getRole().getDroits().contains(RAouP)) {

                for (Activite a : activites) {

                    try {
                        apprenants.addAll(a.getAoup());

                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    
                }

                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique
                        .setDescription(
                                "" + user.getPrenom() + " " + user.getNom()
                                        + " a afficher les participant par entite ");
                historiqueService.Create(historique);

                // List<AouP> apprenants = new ArrayList<>();

                // for (Activite a : ActiviteEntite) {
                //     apprenants.addAll(a.getAoup());
                // }

                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        apprenants);
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    /// ::::::::::::::::::::::::::Tirages valides
    @ApiOperation(value = "Tirages valides")
    @PostMapping("/tirage/valides")
    public ResponseEntity<Object> TiragesValides(@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit RTirage = droitService.GetLibelle("Read Tirage");

            if (user.getRole().getDroits().contains(RTirage)) {

                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique
                        .setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a afficher les tirages valides ");
                historiqueService.Create(historique);

                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        tirageService.tiragesValides(true));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    /// ::::::::::::::::::::::::::Liste par id
    @ApiOperation(value = "recupere une liste par id")
    @PostMapping("/liste/{idliste}")
    public ResponseEntity<Object> ListeParId(@PathVariable("idliste") Long idliste,
            @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            ListePostulant list = listePostulantService.GetById(idliste);

            Droit REntite = droitService.GetLibelle("Read ListePostulant");

            if (user.getRole().getDroits().contains(REntite)) {

                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique
                        .setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a recuper une liste par  " + idliste);
                historiqueService.Create(historique);

                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        list);
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }


    //::::::::::::::::::PERSONNEL PAR ENTITE::::::::::::::::
    @ApiOperation(value = "PERSONNEL PAR ENTITE")
    @PostMapping("/personnelEntite/{identite}")
    public ResponseEntity<Object> PersonnelEntite(@PathVariable("identite") Long identite,
                                             @RequestParam(value = "user") String userVenant) {
        try {


            Entite entite = entiteService.GetById(identite);
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

        List<Utilisateur> us = utilisateurService.getAll();
            List<Utilisateur> aRetourner = new ArrayList<>();
            for (Utilisateur u: us) {
                if (u.getMonEntite() == entite.getUtilisateurEntite()) {
                    aRetourner.add(u);

                }
            }
            Droit REntite = droitService.GetLibelle("Read ListePostulant");

            if (user.getRole().getDroits().contains(REntite)) {

                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique
                        .setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a recuper les personnels  de l'entite  " + entite.getLibelleentite());
                historiqueService.Create(historique);

                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        aRetourner);
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }


    
    // ---------------------------CRUD
    // INTERVENANT
    // EXTERNE-------------------------------------------------------------->
    @ApiOperation(value = "Creer un intervenant interne.")
    @PostMapping("/create/intervenant")
    public ResponseEntity<Object> createIntervenant(@RequestParam(value = "data") String data,
            @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateu = new JsonMapper().readValue(userVenant, Utilisateur.class);

            IntervenantExterne utilisateur = new JsonMapper().readValue(data, IntervenantExterne.class);

            // Role role = RoleService.GetByLibelle("USER");

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateu.getLogin(),
                    utilisateu.getPassword());
            Droit CUser = droitService.GetLibelle("Create Intervenant");

            if (users != null) {
                if (users.getRole().getDroits().contains(CUser)) {

                    if (intervenantExterneService.getByEmail(utilisateur.getEmail()) == null) {

                        try {
                            Historique historique = new Historique();
                            Date datehisto = new Date();
                            historique.setDatehistorique(datehisto);
                            historique.setDescription(users.getPrenom() + " " + users.getNom()
                                    + " a cree un personnel externe du nom de " + utilisateur.getNom());

                            historiqueService.Create(historique);

                            IntervenantExterne NewUser = intervenantExterneService.creer(utilisateur);
                            // System.out.println(NewUser.getLogin());
                            return ResponseMessage.generateResponse("ok", HttpStatus.OK, NewUser);
                        } catch (Exception e) {
                            // TODO: handle exception
                            return ResponseMessage.generateResponse("ijjciiii", HttpStatus.OK, e.getMessage());

                        }

                    } else {
                        return ResponseMessage.generateResponse("error", HttpStatus.OK, "Adresse mail existante");

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


    @ApiOperation(value = "Ensemble des intervenants.")
    @PostMapping("/intervenant/all")
    public ResponseEntity<Object> createIntervenant(@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateu = new JsonMapper().readValue(userVenant, Utilisateur.class);

            // Role role = RoleService.GetByLibelle("USER");

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateu.getLogin(),
                    utilisateu.getPassword());
            Droit CUser = droitService.GetLibelle("Read Intervenant");

            if (users != null) {
                if (users.getRole().getDroits().contains(CUser)) {


                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(users.getPrenom() + " " + users.getNom()
                                + " a cree recuperer la liste des intervenants externes.");

                        historiqueService.Create(historique);

                        //IntervenantExterne NewUser = intervenantExterneService.creer(users);
                        // System.out.println(NewUser.getLogin());
                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, intervenantExterneService.getAll());
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("ijjciiii", HttpStatus.OK, e.getMessage());

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


    @ApiOperation(value = "Participant par activite.")
    @PostMapping("/participants/{idActivite}")
    public ResponseEntity<Object> ParticipantActivite(@RequestParam(value = "user") String userVenant,@PathVariable("idActivite") Long idActivite) {
        try {

            Utilisateur utilisateu = new JsonMapper().readValue(userVenant, Utilisateur.class);

            // Role role = RoleService.GetByLibelle("USER");

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateu.getLogin(),
                    utilisateu.getPassword());
            Droit CAoup= droitService.GetLibelle("Read AouP");

            if (users != null) {
                if (users.getRole().getDroits().contains(CAoup)) {


                    try {

                        Activite activite=activiteService.GetById(idActivite);
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(users.getPrenom() + " " + users.getNom()
                                + " a cree recuperer la liste des participants de lactivite "+idActivite);

                        historiqueService.Create(historique);

                        //IntervenantExterne NewUser = intervenantExterneService.creer(users);
                        // System.out.println(NewUser.getLogin());
                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, activite.getAoup());
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("ijjciiii", HttpStatus.OK, e.getMessage());

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



    @ApiOperation(value = "Activites sans participants.")
    @PostMapping("/activitesansparticipants")
    public ResponseEntity<Object> ListeSansParticipant(@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateu = new JsonMapper().readValue(userVenant, Utilisateur.class);

            // Role role = RoleService.GetByLibelle("USER");

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateu.getLogin(),
                    utilisateu.getPassword());
            Droit CAoup= droitService.GetLibelle("Read AouP");

            if (users != null) {
                if (users.getRole().getDroits().contains(CAoup)) {


                    try {

                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(users.getPrenom() + " " + users.getNom()
                                + " a cree recuperer les listes sans participants");

                        historiqueService.Create(historique);

                        List<Activite> allACtivite=activiteService.GetAll();
                        List<Activite> listeAretourner=new ArrayList<>();
                        for(Activite a:allACtivite){
                            try {
                                if(a.getAoup().size()==0){
                                    listeAretourner.add(a);
                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }

                        //IntervenantExterne NewUser = intervenantExterneService.creer(users);
                        // System.out.println(NewUser.getLogin());
                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, listeAretourner);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("ijjciiii", HttpStatus.OK, e.getMessage());

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

    // INTERVENANT
    // EXTERNE-------------------------------------------------------------->

    // l'ensemble des listes tirer lors de tirage pour kadi
    /*
     * @ApiOperation(value = "Liste tirer lors d'un tirage")
     * 
     * @PostMapping("/")
     * public ResponseEntity<Object> AfficherListPost(@PathVariable Long
     * idlistepostulant,@RequestParam(value = "user") String userVenant) {
     * try {
     * Utilisateur utilisateur = new JsonMapper().readValue(userVenant,
     * Utilisateur.class);
     * 
     * Utilisateur user =
     * utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
     * utilisateur.getPassword());
     * Droit RAoup = droitService.GetLibelle("Read ListePostulant");
     * 
     * if (user.getRole().getDroits().contains(RAoup)) {
     * return ResponseMessage.generateResponse("ok", HttpStatus.OK,
     * tirageService.AfficherListPost(idlistepostulant));
     * } else {
     * return ResponseMessage.generateResponse("error", HttpStatus.OK,
     * "Non autorisé");
     * 
     * }
     * 
     * } catch (Exception e) {
     * // TODO: handle exception
     * return ResponseMessage.generateResponse("error", HttpStatus.OK,
     * e.getMessage());
     * 
     * }
     * }
     */

    

}
