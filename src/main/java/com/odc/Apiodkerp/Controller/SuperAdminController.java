package com.odc.Apiodkerp.Controller;

import com.odc.Apiodkerp.Models.*;
import com.odc.Apiodkerp.Repository.HistoriqueRepo;
import com.odc.Apiodkerp.Service.*;
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

    // ---------------------------CRUD
    // USER-------------------------------------------------------------->
    @ApiOperation(value = "Creer un utilisateur.")
    @PostMapping("/create/user")
    public ResponseEntity<Object> createUser(@RequestParam(value = "data") String data,
            @RequestParam(value = "user") String userVenant,
            @RequestParam(value = "file", required = false) MultipartFile file, @RequestBody Utilisateur utilis) {
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
                        if (file != null) {
                            utilisateur.setImage(SaveImage.save("user", file, utilisateur.getEmail()));
                        }

                        // Historique
                        try {
                            Historique historique = new Historique();
                            Date datehisto = new Date();
                            historique.setDatehistorique(datehisto);
                            historique.setDescription("" + users.getPrenom() + " " + users.getNom()
                                    + " a crée un utilisateur du nom de " + utilisateur.getNom());
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
                                        + " a modifier un utilisateur du nom de ");
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
                                + " a supprimé un utilisateur du nom de " + utilisateurService.getById(id));
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
                        historique.setDescription("" + user.getPrenom() + "a affiché tous les utilisateurs");
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
                            "" + user.getPrenom() + " " + user.getNom() + " a affiché tous les responsables");
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
                            "" + admin.getPrenom() + " " + admin.getNom() + " a affiché le responsable du nom de "
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
    @PostMapping("/totalpersonnel")
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
                            "" + users.getPrenom() + " " + users.getNom() + " a affiché tous les postulants ");
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

            Droit Ractivite = droitService.GetLibelle("Read Actvite");

            if (users != null) {
                if (users.getRole().getDroits().contains(Ractivite)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + users.getPrenom() + " " + users.getNom() + " a affiche les activites a venir");
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, activiteService.Avenir());

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
            Droit Ractivite = droitService.GetLibelle("Read Actvite");

            if (users != null) {
                if (users.getRole().getDroits().contains(Ractivite)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + users.getPrenom() + " " + users.getNom() + " a affiche les activites en cour ");
                        historiqueService.Create(historique);

                        return ResponseMessage.generateResponse("error", HttpStatus.OK, activiteService.Encour());
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

    // activités termines
    @ApiOperation(value = "activites/termines")
    @PostMapping("activites/termines")
    public ResponseEntity<Object> ActivitesTermines(@RequestParam(value = "user") String userVenant) {

        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit Ractivite = droitService.GetLibelle("Read Actvite");

            if (users != null) {
                if (users.getRole().getDroits().contains(Ractivite)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + users.getPrenom() + " " + users.getNom() + " a affiché les activités terminée ");
                        historiqueService.Create(historique);

                        return ResponseMessage.generateResponse("error", HttpStatus.OK, activiteService.Termine());
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
    // :::::::::::::: ::::::::::::::debut mise a jour de madame ::::::::::::::::::::

    // :::::::::::Liste des activites par entite ::::::::::::::::
    @ApiOperation(value = "activites par entite")
    @PostMapping("activites/entite/{identite}/{login}/{password}")
    public ResponseEntity<Object> ActivitesParEntite(@PathVariable long identite, @PathVariable String login,
            @PathVariable String password) {
        try {
            Utilisateur users = utilisateurService.trouverParLoginAndPass(login, password);
            Droit Ractivite = droitService.GetLibelle("Read Actvite");

            if (users != null) {
                if (users.getRole().getDroits().contains(Ractivite)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + users.getPrenom() + " " + users.getNom() + " a affiché les activités par entite ");
                        historiqueService.Create(historique);
                        return ResponseMessage.generateResponse("error", HttpStatus.OK,
                                activiteService.ActiviteEntiteid(identite));

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

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

    // :::::::::::::::::::Liste des activites par intervalle de date :
    // ::::::::::::::::::::
    // fait avec ballo le 26/09

    // ::::::::::::La liste des participants par activité et par intervalle de date,

    @ApiOperation(value = "liste participant par activite")
    @PostMapping("activites/entite/{idactivite}/{date1}/{date2}/{login}/{password}")
    public ResponseEntity<Object> PostulantParActivite(@PathVariable String login, @PathVariable String password,
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

                if (tirage.getActivite().getId() == activite.getId() && pt.getTirage().getId() == tirage.getId()) {

                    // :::::::::::::::::::::::::::::Histroque::::::::::::::::::::::::::::::::::

                    Utilisateur users = utilisateurService.trouverParLoginAndPass(login, password);

                    Droit Rpost = droitService.GetLibelle("Read postulant");

                    if (users != null) {
                        if (users.getRole().getDroits().contains(Rpost)) {
                            try {
                                Historique historique = new Historique();
                                Date datehisto = new Date();
                                historique.setDatehistorique(datehisto);
                                historique.setDescription("" + users.getPrenom() + " " + users.getNom()
                                        + " a affiché des postulant de l'activite " + activite.getNom());
                                historiqueService.Create(historique);
                            } catch (Exception e) {
                                // TODO: handle exception
                                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                            }
                            return ResponseMessage.generateResponse("error", HttpStatus.OK,
                                    postulantTrieService.getAll());
                        } else {
                            return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

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
    @PostMapping("activites/entite/{identite}/{idstatut}/{login}/{password}")
    public ResponseEntity<Object> ActivitesParEntiteEtParstatut(@PathVariable String login,
            @PathVariable String password, @PathVariable long identite, @PathVariable long idstatut) {
        try {
            Etat etat = etatService.GetById(idstatut);// on recup l'etat en fonction de l'id
            Entite entite = entiteService.GetById(identite);
            Utilisateur users = utilisateurService.trouverParLoginAndPass(login, password);
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
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

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
    @PostMapping("statut/activite/{id}/{login}/{password}")
    public ResponseEntity<Object> ActivitesTermines(@PathVariable long id, @PathVariable String login,
            @PathVariable String password) {
        try {

            Utilisateur users = utilisateurService.trouverParLoginAndPass(login, password);
            Droit Ractivite = droitService.GetLibelle("Read Activite");

            if (users != null) {
                if (users.getRole().getDroits().contains(Ractivite)) {

                    Activite act = new Activite();
                    act = activiteService.GetById(id);
                    Date dateTodate = new Date();
                    if (dateTodate.after(act.getDateDebut()) && dateTodate.before(act.getDateFin())) {
                        return ResponseMessage.generateResponse("En cours", HttpStatus.OK, "L'activité est en cours");
                    } else if (dateTodate.after(act.getDateFin())) {
                        return ResponseMessage.generateResponse("Terminé", HttpStatus.OK, "L'activité est terminée");
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

    @ApiOperation(value = "l'ensemble des salles indisponibles")
    @PostMapping("/SalleInDisponible")
    public ResponseEntity<Object> InsalleDispo(@RequestParam(value = "user") String userVenant) {
        try {
            List<Activite> acts = activiteService.FindAllAct();
            List<Salle> salle = new ArrayList<>();

            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());

            Date today = new Date();

            Droit RSalle = droitService.GetLibelle("Read Salle");

            if (users != null) {
                if (users.getRole().getDroits().contains(RSalle)) {
                    for (Activite act : acts) {
                        if (act.getDateDebut().after(today) && act.getDateFin().before(today)
                                || act.getDateDebut().before(today) && act.getDateFin().after(today)) {

                            salle.add(act.getSalle());
                            // Historique

                            salle.add(act.getSalle());
                        }
                    }
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + users.getPrenom() + " " + users.getNom()
                                + " a afficher  salle disponible dans l'intervalle ");
                        historiqueService.Create(historique);

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, salle);

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
                }
            }

            return ResponseMessage.generateResponse("error", HttpStatus.OK, activiteService.Termine());
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
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
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
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
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
    @PostMapping("/getUsers/desactive/{login}/{password}")
    public ResponseEntity<Object> getUsersDesactives(@PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
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
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
    //

    // :::::::::::::::::::::::Les salles disponible
    @ApiOperation(value = "Les salles disponible")
    @PostMapping("/getSalles/disponible/{login}/{password}")
    public ResponseEntity<Object> getSallesDispo(@PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
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
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // :::::::::::::::::::::::::::::::::::::::Apprenants ou Participant
    // :::::::::::::::::::::::::::::::
    @ApiOperation(value = "Ajouter AppouParticipant")
    @PostMapping("/aoup")
    public ResponseEntity<Object> ajouterAouP(@RequestParam(value = "aoup") String aoup,
            @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            AouP aou = new JsonMapper().readValue(aoup, AouP.class);
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
                                    + " a enregistré des apprenants ou participants");
                    historiqueService.Create(historique);

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
                            + " a modifié l' apprenants ou le participant sur l'activite " + aouP.getActivite());
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            aouPService.Update(id, aou));
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
                            + " a supprimé l' apprenants ou le participant sur l'activite " + aouP.getActivite());
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            aouPService.Delete(id));
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
                            "" + user.getPrenom() + " " + user.getNom() + " a affiché un apprenant ou un participant ");
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
    @PostMapping("/droit/new/{login}/{password}")
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
                            "" + user.getPrenom() + " " + user.getNom() + " a ajouté le droit " + drt.getLibelle());
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
    public ResponseEntity<Object> ModifierDroit(@PathVariable long id, @PathVariable long iduser,
            @RequestBody Droit droit) {
        try {

            // Histroique
            Utilisateur user = utilisateurService.getById(iduser);
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription(
                        "" + user.getPrenom() + " " + user.getNom() + " a modifié le droit " + droit.getLibelle());
                historiqueService.Create(historique);
            } catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    droitService.Update(id, droit));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
    // ::::::::::::::::::::::::::::::::::: Supprimer droit
    // ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Supprimer Droit")
    @PostMapping("/droit/supprimer/{iduser}/{id}")
    public ResponseEntity<Object> SupprimerDroit(@PathVariable long id, @PathVariable long iduser) {
        try {

            // Histroique
            Utilisateur user = utilisateurService.getById(iduser);
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription("" + user.getPrenom() + " " + user.getNom() + " a supprimé un droit ");
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
    @PostMapping("/droit/GetId/{iduser}/{id}")
    public ResponseEntity<Object> GetDroitparId(@PathVariable long id, @PathVariable long iduser) {
        try {
            Droit droit = droitService.GetById(id);
            // Histroique
            Utilisateur user = utilisateurService.getById(iduser);
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription(
                        "" + user.getPrenom() + " " + user.getNom() + " a affiché le droit " + droit.getLibelle());
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

    // :::::::::::::::::::::::::::Droit par libelle
    // ::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Droit par libelle")
    @PostMapping("/droit/GetLibelle/{iduser}/{libelle}")
    public ResponseEntity<Object> GetDroitparLibelle(@PathVariable String libelle, @PathVariable long iduser) {
        try {

            Utilisateur user = utilisateurService.getById(iduser);
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique
                        .setDescription("" + user.getPrenom() + " " + user.getNom() + " a affiché le droit " + libelle);
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

}
