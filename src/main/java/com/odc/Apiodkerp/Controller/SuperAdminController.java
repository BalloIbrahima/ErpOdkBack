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

    // Pour le login d'un super administrateur
    @ApiOperation(value = "Pour le login d'un super administrateur.")
    @PostMapping("/login/{login}/{password}")
    public ResponseEntity<Object> CreateAdmin(@PathVariable("login") String login,
            @PathVariable("password") String password) {

        try {
            Utilisateur Superutilisateur = utilisateurService.login(login, password);
            Role admin = RoleService.GetByLibelle("ADMIN");
            if (Superutilisateur != null && Superutilisateur.getActive() == true) {
                if (Superutilisateur.getRole() == admin && Superutilisateur.getActive() == true) {
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, Superutilisateur);
                } else {
                    return ResponseMessage.generateResponse("non autorise", HttpStatus.OK, null);
                }
            } else {
                return ResponseMessage.generateResponse("utilisateur n'existe pas", HttpStatus.OK, null);
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    ////
    @ApiOperation(value = "Lien pour créer une salle")
    @PostMapping("/creersalle/{login}/{password}")
    public ResponseEntity<Object> creerSalle(@RequestBody Salle salle, @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            // Utilisateur user = utilisateurService.trouverParLoginAndPass(login,password);

            Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(login, password);
            System.out.println(utilisateur);
            if (utilisateur.getRole() == RoleService.GetByLibelle("ADMIN")) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription("" + utilisateur.getPrenom() + " " + utilisateur.getNom()
                            + " a crée une salle du nom de " + salle.getLibelle());
                    historiqueService.Create(historique);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }
                salle.setUtilisateur(utilisateur);
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, salleService.create(salle));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // ::::::::::Recuperer salle par id
    @ApiOperation(value = "Recuperer salle par id")
    @GetMapping("getSalle/{id}/{login}/{password}")
    public ResponseEntity<Object> getSalle(@PathVariable("id") Long id, @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Salle salle = new Salle();
            Droit Rsalle = droitService.GetLibelle("Read Salle");

            if (user != null) {
                if (user.getRole().getDroits().contains(Rsalle)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a recuperer une salle du nom de " + salle.getId());
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, salleService.read(id));

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

    @ApiOperation(value = "Lien pour modifier une salle")
    @PutMapping("/modifiersalle/{id}/{login}/{password}")
    public ResponseEntity<Object> modifier(@RequestBody Salle salle, @PathVariable long id,
            @PathVariable("login") String login, @PathVariable("password") String password) {
        try {
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

            Droit Usalle = droitService.GetLibelle("Update Salle");

            if (user != null) {
                if (user.getRole().getDroits().contains(Usalle)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a modifier une salle du nom de " + salleService.getByIdsalle(id).getLibelle());
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, salleService.update(salle, id));
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "Lien pour suprimer une salle")
    @DeleteMapping("/supprimersalle/{id}/{login}/{password}")
    public ResponseEntity<Object> supprimer(@PathVariable long id, @PathVariable String login,
            @PathVariable String password) {
        try {
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

            Droit DSalle = droitService.GetLibelle("Delete Salle");

            if (user != null) {
                if (user.getRole().getDroits().contains(DSalle)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a supprimé une salle du nom de ");
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    salleService.delete(id);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, "Salle supprimer avec succès !");
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "Lien pour lier une salle à une activite")
    @GetMapping("/attribuersalle/{idsalle}/{idactivite}/{login}/{password}")
    public ResponseEntity<Object> attribuerSalle(@PathVariable long idsalle, @PathVariable long idactivite,
            @PathVariable String login, @PathVariable String password) {
        try {
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

            Droit Ra = droitService.GetLibelle("Read Activite");

            if (user != null) {
                if (user.getRole().getDroits().contains(Ra)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a attribué  une salle du nom de  " + salleService.getByIdsalle(idsalle)
                                + " à l'activte " + activiteService.GetById(idactivite));
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            activiteService.attribuerSalle(idsalle, idactivite));
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // pour recuperer toutes les salles
    @ApiOperation(value = "Pour recuperer toutes les salles")
    @GetMapping("/salle/all/{login}/{password}")
    public ResponseEntity<Object> getAll(@PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            Utilisateur users = utilisateurService.trouverParLoginAndPass(login, password);
            Droit Rs = droitService.GetLibelle("Read Salle");

            if (users.getRole().getDroits().contains(Rs)) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        salleService.getAll());
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // ---------------------------CRUD
    // USER-------------------------------------------------------------->
    @ApiOperation(value = "Creer un utilisateur.")
    @PostMapping("/create/user/{login}/{password}}/{idadmin}")
    public ResponseEntity<Object> createUser(@RequestParam(value = "data") String data, @PathVariable long idadmin,
            @PathVariable("login") String login, @PathVariable("password") String password,
            @RequestParam(value = "file", required = false) MultipartFile file, @RequestBody Utilisateur utilis) {
        try {
            Utilisateur user = utilisateurService.getById(idadmin);

            Utilisateur utilisateur = new JsonMapper().readValue(data, Utilisateur.class);

            Role role = RoleService.GetByLibelle("USER");

            Utilisateur users = utilisateurService.trouverParLoginAndPass(login, password);
            Droit CUser = droitService.GetLibelle("Create Utilisateur");

            if (users != null) {
                if (users.getRole().getDroits().contains(CUser)) {

                    if (utilisateurService.getByEmail(utilisateur.getEmail()) == null) {
                        utilisateur.setRole(role);
                        if (file != null) {
                            utilisateur.setImage(SaveImage.save("user", file, utilisateur.getEmail()));
                        }

                        // Historique
                        try {
                            Historique historique = new Historique();
                            Date datehisto = new Date();
                            historique.setDatehistorique(datehisto);
                            historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                    + " a crée un utilisateur du nom de " + utilisateur.getNom());
                            historiqueService.Create(historique);
                        } catch (Exception e) {
                            // TODO: handle exception
                            return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                        }

                        Utilisateur NewUser = utilisateurService.creer(utilisateur);
                        System.out.println(NewUser.getLogin());
                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, NewUser);
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
    @PutMapping("/update/user/{idadmin}/{id}/{login}/{password}")
    public ResponseEntity<Object> updateUser(@RequestBody Utilisateur utilisateur, @PathVariable long idadmin,
            @PathVariable("login") String login, @PathVariable("password") String password,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            if (file != null) {
                SaveImage.save("user", file, utilisateur.getEmail());
            }

            Utilisateur users = utilisateurService.trouverParLoginAndPass(login, password);
            Droit UUser = droitService.GetLibelle("Update Utilisateur");

            if (users != null) {
                if (users.getRole().getDroits().contains(UUser)) {

                    // Historique
                    Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + user.getPrenom() + " " + user.getNom() + " a modifier un utilisateur du nom de ");
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
    @DeleteMapping("/delete/user/{id}/{login}/{password}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id,
            @PathVariable String password, @PathVariable String login) {
        try {

            // Historique
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

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
    @GetMapping("/getAll/user/{login}/{password}")
    public ResponseEntity<Object> GetAllUser(@PathVariable String login,
            @PathVariable String password) {
        try {
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

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
    @GetMapping("/get/user/{id}/{login}/{password}")
    public ResponseEntity<Object> GetIdUtilisateur(@PathVariable("id") Long id,
            @PathVariable String login, @PathVariable String password) {
        try {

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
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
    @PostMapping("/create/responsable/{login}/{password}")
    public ResponseEntity<Object> createResponsable(@RequestParam(value = "data") String data,
            @PathVariable String login, @PathVariable String password,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Utilisateur admin = utilisateurService.trouverParLoginAndPass(login, password);
            Droit Ruser = droitService.GetLibelle("Create Utilisateur");

            if (admin.getRole().getDroits().contains(Ruser)) {
                Utilisateur utilisateur = new Utilisateur();
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
                                "" + admin.getPrenom() + " " + admin.getNom() + " a crée un responsable du nom de "
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
    @PutMapping("/update/responsable/{idResponsable}/{login}/{password}")
    public ResponseEntity<Object> updateResponsable(@RequestParam(value = "data") String data,
            @PathVariable("idResponsable") Long idResponsable, @PathVariable String login,
            @PathVariable String password,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(data, Utilisateur.class);
            Utilisateur responsable = utilisateurService.getById(idResponsable);

            Utilisateur admin = utilisateurService.trouverParLoginAndPass(login, password);

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
    @DeleteMapping("/delete/responsable/{idResponsable}/{login}/{password}")
    public ResponseEntity<Object> deleteResponsable(@PathVariable String login,
            @PathVariable String password,
            @PathVariable("idResponsable") Long idResponsable) {
        try {
            Utilisateur admin = utilisateurService.trouverParLoginAndPass(login, password);
            if (admin.getRole() == RoleService.GetByLibelle("ADMIN")) {

                Utilisateur respon = utilisateurService.getById(idResponsable);

                Droit deleteResponsable = droitService.GetLibelle("Delete Utilisateur");

                if (admin.getRole().getDroits().contains(deleteResponsable)) {
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + admin.getPrenom() + " " + admin.getNom()
                                + " a supprimé un responsable du nom de " + respon.getNom() + " " + respon.getPrenom());
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
    @GetMapping("/getAll/responsable/{login}/{password}")
    public ResponseEntity<Object> GetAllResponsable(@PathVariable String login,
            @PathVariable String password) {
        try {

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

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
    @GetMapping("/get/responsable/{id}/{login}/{password}")
    public ResponseEntity<Object> GetIdResponsable(@PathVariable("id") Long id, @PathVariable String login,
            @PathVariable String password,
            @RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur idResponsable = utilisateurService.getById(id);

            Utilisateur admin = utilisateurService.trouverParLoginAndPass(login, password);

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
    // ENTITE-------------------------------------------------------------->
    @ApiOperation(value = "Creer un entite.")
    @PostMapping("/create/entite/{login}/{password}")
    public ResponseEntity<Object> createEntite(@RequestBody Entite entite, @PathVariable String login,
            @PathVariable String password) {
        try {

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit createrole = droitService.GetLibelle("Create Entite");

            if (user.getRole().getDroits().contains(createrole)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique
                            .setDescription(
                                    "" + user.getPrenom() + " " + user.getNom() + " a crée  une nouvelle entite ");
                    historiqueService.Create(historique);
                    Entite NewEntite = entiteService.Create(entite);
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

    @ApiOperation(value = "Modifier un entite")
    @PutMapping("/update/entite/{id}/{login}/{password}")
    public ResponseEntity<Object> updateEntite(@PathVariable("id") Long id, @RequestBody Entite entite,
            @PathVariable String login,
            @PathVariable String password) {
        try {
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit updatentite = droitService.GetLibelle("Update Entite");

            if (user.getRole().getDroits().contains(updatentite)) {
                Entite UpdateEntite = entiteService.Update(id, entite);
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, UpdateEntite);
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Supprimer un entite")
    @DeleteMapping("/delete/entite/{id}/{login}/{password}")
    public ResponseEntity<Object> DeleteEntite(@PathVariable Long id, @PathVariable String login,
            @PathVariable String password) {
        try {
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit deleterole = droitService.GetLibelle("Delete Entite");

            if (user.getRole().getDroits().contains(deleterole)) {
                entiteService.Delete(id);
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Affichager tout les entités")
    @GetMapping("/getAll/entite/{login}/{password}")
    public ResponseEntity<Object> GetAllEntite(@PathVariable String login,
            @PathVariable String password) {
        try {

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit getentite = droitService.GetLibelle("Read Entite");

            if (user.getRole().getDroits().contains(getentite)) {
                List<Entite> getAllEntite = entiteService.GetAll();
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, getAllEntite);
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Affichager une entite")
    @GetMapping("/get/entite/{id}/{login}/{password}")
    public ResponseEntity<Object> GetIdEntite(@RequestParam("id") Long id, @RequestBody Entite entite,
            @PathVariable String login,
            @PathVariable String password) {
        try {

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
            Droit getentite = droitService.GetLibelle("Read Entite");

            if (user.getRole().getDroits().contains(getentite)) {
                Entite idEntite = entiteService.GetById(id);
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, idEntite);

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    /// active un utilisateur
    @ApiOperation(value = "Active un utilisateur")
    @GetMapping("/active/{idUser}/{login}/{password}")
    ResponseEntity<Object> activeUser(@PathVariable String login,
            @PathVariable String password, @PathVariable("idUser") Long idUser) {

        try {

            Utilisateur admin = utilisateurService.trouverParLoginAndPass(login, password);
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
    @GetMapping("/desactive/{idUser}/{login}/{password}")
    ResponseEntity<Object> desactiveUser(@PathVariable String login,
            @PathVariable String password, @PathVariable("idUser") Long idUser) {

        try {
            Utilisateur admin = utilisateurService.trouverParLoginAndPass(login, password);
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

    ///////// type activite
    @ApiOperation(value = "methode pour la création d'une type d' activité.")
    @PostMapping("/Typeactivite/creer/{login}/{password}")
    public ResponseEntity<Object> CreateTypeActivite(@RequestBody TypeActivite typeActivite,
            @PathVariable String login,
            @PathVariable String password) {
        try {
            Utilisateur admin = utilisateurService.trouverParLoginAndPass(login, password);
            Droit createtype = droitService.GetLibelle("Create TypeActivite");

            if (admin.getRole().getDroits().contains(createtype)) {
                if (typeActiviteService.getByLibelle(typeActivite.getLibelle()) == null) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + admin.getPrenom() + " " + admin.getNom()
                                + " a crée un type d'activite  de libelle " + typeActivite.getLibelle());
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            typeActiviteService.creer(typeActivite));
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "libelle existant");
                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "methode pour la Suppression d'une type d' activité.")
    @PostMapping("/Typeactivite/{id}/{login}/{password}")
    public ResponseEntity<Object> SupprimerTypeActivite(@PathVariable long id, @PathVariable String login,
            @PathVariable String password,
            @RequestBody TypeActivite typeActivite) {
        try {
            // historique
            Utilisateur users = utilisateurService.trouverParLoginAndPass(login, password);
            Droit deletetetype = droitService.GetLibelle("Delete TypeActivite");

            if (users.getRole().getDroits().contains(deletetetype)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription("" + users.getPrenom() + " " + users.getNom()
                            + " a supprimé un type d'activte du nom de " + typeActivite.getLibelle());
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, typeActiviteService.delete(id));

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

    @ApiOperation(value = "methode pour la modification d'une type d' activité.")
    @PostMapping("/Typeactivite/modification/{login}/{password}")
    public ResponseEntity<Object> ModifTypeActivite(@PathVariable String login,
            @PathVariable String password, @RequestBody TypeActivite typeActivite) {
        try {

            Utilisateur users = utilisateurService.trouverParLoginAndPass(login, password);

            Droit readtetype = droitService.GetLibelle("Read TypeActivite");

            if (users.getRole().getDroits().contains(readtetype)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique
                            .setDescription(
                                    "" + users.getPrenom() + " " + users.getNom() + " a modifié un type d'activte");
                    historiqueService.Create(historique);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, typeActiviteService.update(typeActivite));

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Recuperer l'ensemble des types acticite")
    @GetMapping("Typeactivite/getall/{login}/{password}")
    public ResponseEntity<Object> TypaActiviteAll(@PathVariable String login,
            @PathVariable String password) {
        try {
            Utilisateur users = utilisateurService.trouverParLoginAndPass(login, password);

            Droit readtetype = droitService.GetLibelle("Read TypeActivite");

            if (users.getRole().getDroits().contains(readtetype)) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, typeActiviteService.getAll());

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
    @GetMapping("/totalpersonnel/{iduser}")
    public ResponseEntity<Object> TotalPostulant(@PathVariable long iduser) {
        try {
            Utilisateur users = utilisateurService.getById(iduser);

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

    // :::::::::::::::::::::::::::::::::::total entite
    // ::::::::::::::::::::::::::::::::::::::
    @ApiOperation(value = "totalentite")
    @GetMapping("/totalentite/{login}/{password}")
    public ResponseEntity<Object> TotalEntite(@PathVariable String login, @PathVariable String password) {
        try {
            Utilisateur users = utilisateurService.trouverParLoginAndPass(login, password);
            Droit Rentite = droitService.GetLibelle("Lire une entitee");

            if (users != null) {
                if (users.getRole().getDroits().contains(Rentite)) {

                    try {

                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + users.getPrenom() + " " + users.getNom() + " a affiché toutes les entités ");
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, utilisateurService.TotalEntite());

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

    //// :::::::::::::::::::::::::::::::l'ensemble des activites en cour, à venir ,
    //// termine

    // activités en avenir
    @ApiOperation(value = "activites/avenir")
    @GetMapping("activites/avenir/{id}")
    public ResponseEntity<Object> ActivitesAvenir(@PathVariable long id) {
        try {

            Utilisateur users = utilisateurService.getById(id);

            Droit Ractivite = droitService.GetLibelle("Read Actvite");

            if (users != null) {
                if (users.getRole().getDroits().contains(Ractivite)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + users.getPrenom() + " " + users.getNom() + " a affiché les activités à venir ");
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
    @GetMapping("activites/encour/{id}")
    public ResponseEntity<Object> ActivitesEncour(@PathVariable long id) {
        try {
            Utilisateur users = utilisateurService.getById(id);
            Droit Ractivite = droitService.GetLibelle("Read Actvite");

            if (users != null) {
                if (users.getRole().getDroits().contains(Ractivite)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + users.getPrenom() + " " + users.getNom() + " a affiché les activités en cour ");
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
    @GetMapping("activites/termines/{login}/{password}")
    public ResponseEntity<Object> ActivitesTermines(@PathVariable String login, @PathVariable String password) {

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
    @GetMapping("activites/entite/{identite}/{login}/{password}")
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
    @GetMapping("activites/entite/{idactivite}/{date1}/{date2}/{login}/{password}")
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
    @GetMapping("activites/entite/{identite}/{idstatut}/{login}/{password}")
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
    @GetMapping("statut/activite/{id}/{login}/{password}")
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

    // ::::::::::::::Comparaison date pour salle disponible
    // ::::::::::::::::::::::::::::::
    @ApiOperation(value = "Comparaison date pour salle disponible")
    @GetMapping("SalleDispo/{date1}/{date2}/{login}/{password}")
    public ResponseEntity<Object> SalleDispoDate(@PathVariable String login, @PathVariable String password,
            @PathVariable Date date1, @PathVariable Date date2) {
        try {
            List<Activite> activites = activiteService.FindAllAct();
            List<Salle> salle = new ArrayList<>();
            Utilisateur users = utilisateurService.trouverParLoginAndPass(login, password);
            Droit RSalle = droitService.GetLibelle("Read Salle");

            if (users != null) {
                if (users.getRole().getDroits().contains(RSalle)) {
                    for(Activite act: activites){
                        if (act.getDateDebut().before(date1) && act.getDateDebut().before(date2)
                                && act.getDateFin().after(date1)
                                && act.getDateFin().after(date2)
                                || act.getDateDebut().after(date1) && act.getDateDebut().after(date2)
                                && act.getDateFin().before(date1) && act.getDateFin().before(date2)) {
                            // Historique

                            salle.add(act.getSalle());
                        }
                    }

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + users.getPrenom() + " " + users.getNom()
                                + " a afficher  salle disponible dans l'intervalle " + date1 + " et " + date2);
                        historiqueService.Create(historique);
                        return ResponseMessage.generateResponse("error", HttpStatus.OK,salle );

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


    //:::::::::::::::Salle disponible :::::::::::::::::::::::::::::::
    @ApiOperation(value = " salle disponible")
    @GetMapping("SalleDisponible/{login}/{password}")
    public ResponseEntity<Object> SalleDispoDate(@PathVariable String login, @PathVariable String password) {
        try {
            Date date = new Date();
            List<Activite> activites = activiteService.FindAllAct();
            List<Salle> salle = new ArrayList<>();
            Utilisateur users = utilisateurService.trouverParLoginAndPass(login, password);
            Droit RSalle = droitService.GetLibelle("Read Salle");

            if (users != null) {
                if (users.getRole().getDroits().contains(RSalle)) {
                    for(Activite act: activites){
                        if (act.getDateDebut().after(date) && act.getDateFin().after(date)) {
                            // Historique

                            salle.add(act.getSalle());
                        }
                    }

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + users.getPrenom() + " " + users.getNom()
                                + " a afficher  salle disponible  " + date );
                        historiqueService.Create(historique);
                        return ResponseMessage.generateResponse("error", HttpStatus.OK,salle );

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
    @GetMapping("/getUsers/active/{login}/{password}")
    public ResponseEntity<Object> getUsersActives(@PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            // hisorique
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
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

    // :::::::::::::::::::::::Les users desactives
    @ApiOperation(value = "Les utilisateurs desactives")
    @GetMapping("/getUsers/desactive/{login}/{password}")
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
    @GetMapping("/getSalles/disponible/{login}/{password}")
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

    // :::::::::::::::::::::::Les salles indisponible
    @ApiOperation(value = "Les salles indisponible")
    @GetMapping("/getSalles/indisponible/{login}/{password}")
    public ResponseEntity<Object> getSallesIndispo(@PathVariable("login") String login,
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
                            "" + user.getPrenom() + " " + user.getNom() + " a afficher  les salles indisponible");
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            salleService.ParEtat(false));
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

    // :::::::::::::::::::::::::::::::::::::::Apprenants ou Participant
    // :::::::::::::::::::::::::::::::
    @ApiOperation(value = "Ajouter AppouParticipant")
    @PostMapping("/aoup/{login}/{password}")
    public ResponseEntity<Object> ajouterAouP(@RequestBody AouP aoup, @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

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
                            aouPService.Create(aoup));
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
    @PutMapping("/aoup/modifier/{id}/{login}/{password}")
    public ResponseEntity<Object> ModifierAouP(@PathVariable long id, @PathVariable("login") String login,
            @PathVariable("password") String password,
            @RequestBody AouP aoup) {
        try {
            AouP aouP = aouPService.GetById(id);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

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
                            aouPService.Update(id, aoup));
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
    @DeleteMapping("/aoup/supprimer/{id}/{login}/{password}")
    public ResponseEntity<Object> SupprimerAouP(@PathVariable long id, @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            AouP aouP = aouPService.GetById(id);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);
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
    @GetMapping("/aoup/GetId/{id}/{login}/{password}")
    public ResponseEntity<Object> GetAouPparId(@PathVariable long id, @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            // Histroique
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

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

    // :::::::::::::::::::::::::::::::::::::::DESIGNATION
    // :::::::::::::::::::::::::::::::
    @ApiOperation(value = "Ajouter Designation")
    @PostMapping("/designation/{login}/{password}")
    public ResponseEntity<Object> ajouterDesignation(@RequestBody Designation designation,
            @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {

            // Histroique
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

            Droit Cdesignation = droitService.GetLibelle("Create Designation");

            if (user.getRole().getDroits().contains(Cdesignation)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription("" + user.getPrenom() + " " + user.getNom() + " a ajouté la desigantion "
                            + designation.getLibelle());
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            designationService.Create(designation));
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

    // ::::::::::::::::::::::::::::::::::: Modifier Designation
    // ::::::::::::::::::::::::::::::::::::
    @ApiOperation(value = "Modifier Desi")
    @PutMapping("/designation/modifier/{id}/{login}/{password}")
    public ResponseEntity<Object> ModifierDesignation(@PathVariable long id, @PathVariable("login") String login,
            @PathVariable("password") String password,
            @RequestBody Designation designation) {
        try {

            // Historique
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

            Droit Cdesignation = droitService.GetLibelle("Create Designation");

            if (user.getRole().getDroits().contains(Cdesignation)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription("" + user.getPrenom() + " " + user.getNom() + " a modifié la desigantion "
                            + designation.getLibelle());
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            designationService.Update(id, designation));
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

    @ApiOperation(value = "Supprimer Designation")
    @DeleteMapping("/designation/supprimer/{id}/{login}/{password}")
    public ResponseEntity<Object> SupprimerDesignation(@PathVariable long id, @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            Designation designation = designationService.GetById(id);

            // Histroique
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

            Droit Ddesignation = droitService.GetLibelle("Delete Designation");

            if (user.getRole().getDroits().contains(Ddesignation)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique
                            .setDescription("" + user.getPrenom() + " " + user.getNom() + " a supprimé la desigantion "
                                    + designation.getLibelle());
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            designationService.Delete(id));
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
    // ::::::::::::::::::::::::::::::::::: Get pzr id AouP
    // ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Designation par id")
    @GetMapping("/Designation/GetId/{id}/{login}/{password}")
    public ResponseEntity<Object> GetDesigantionparId(@PathVariable long id, @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {

            // Histroique
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

            Droit Gdesignation = droitService.GetLibelle("Read Designation");

            if (user.getRole().getDroits().contains(Gdesignation)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            "" + user.getPrenom() + " " + user.getNom() + " a affiché une designation ");
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            designationService.GetById(id));
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

    // :::::::::::::::::::::::::::::::::::::::Droit :::::::::::::::::::::::::::::::
    @ApiOperation(value = "Ajouter Droit")
    @PostMapping("/droit/new/{login}/{password}")
    public ResponseEntity<Object> ajouterDroit(@RequestBody Droit droit, @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            // Histroique
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

            Droit Cdroit = droitService.GetLibelle("Create Role");

            if (user.getRole().getDroits().contains(Cdroit)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            "" + user.getPrenom() + " " + user.getNom() + " a ajouté le droit " + droit.getLibelle());
                    historiqueService.Create(historique);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            droitService.Create(droit));
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
    @PutMapping("/droit/modifier/{id}")
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
    @DeleteMapping("/droit/supprimer/{iduser}/{id}")
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
    // ::::::::::::::::::::::::::::::::::: Get pzr id AouP
    // ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Droit par id")
    @GetMapping("/droit/GetId/{iduser}/{id}")
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
    @GetMapping("/droit/GetLibelle/{iduser}/{libelle}")
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

    // :::::::::::::::::::::::::::::::::::::::FormationEmail
    // :::::::::::::::::::::::::::::::
    @ApiOperation(value = "Ajouter FormatEmail")
    @PostMapping("/formaemail/{login}/{password}")
    public ResponseEntity<Object> ajouterFormatEmail(@RequestBody FormatEmail formatEmail,
            @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

            Droit Cformat = droitService.GetLibelle("Create FormatEmail");

            if (user.getRole().getDroits().contains(Cformat)) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        formatEmailService.Create(formatEmail));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // ::::::::::::::::::::::::::::::::::: Modifier FormatEmail
    // ::::::::::::::::::::::::::::::::::::
    @ApiOperation(value = "Modifier formatEmail")
    @PutMapping("/formatemail/modifier/{id}/{login}/{password}")
    public ResponseEntity<Object> ModifierFormatEmail(@PathVariable long id, @RequestBody FormatEmail formatEmail,
            @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

            Droit Uformat = droitService.GetLibelle("Update FormatEmail");

            if (user.getRole().getDroits().contains(Uformat)) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        formatEmailService.Update(id, formatEmail));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
    // ::::::::::::::::::::::::::::::::::: Supprimer FormatEmail
    // ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Supprimer Format Email")
    @DeleteMapping("/formatemail/supprimer/{id}")
    public ResponseEntity<Object> SupprimerFormatEmail(@PathVariable long id, @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {

            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

            Droit Dformat = droitService.GetLibelle("Delete FormatEmail");

            if (user.getRole().getDroits().contains(Dformat)) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        formatEmailService.Delete(id));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
    // ::::::::::::::::::::::::::::::::::: Get pzr id FormatEmail
    // ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Format email par id")
    @GetMapping("/formatEmail/GetId/{id}/{login}/{password}")
    public ResponseEntity<Object> GetFormatEMailparId(@PathVariable long id, @PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            Utilisateur user = utilisateurService.trouverParLoginAndPass(login, password);

            Droit Gformat = droitService.GetLibelle("Read FormatEmail");

            if (user.getRole().getDroits().contains(Gformat)) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        formatEmailService.GetById(id));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

}
