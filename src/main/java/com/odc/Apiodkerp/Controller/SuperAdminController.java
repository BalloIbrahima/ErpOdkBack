package com.odc.Apiodkerp.Controller;

import com.odc.Apiodkerp.Models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Configuration.SaveImage;
import com.odc.Apiodkerp.Service.ActiviteService;
import com.odc.Apiodkerp.Service.EntiteService;
import com.odc.Apiodkerp.Service.EtatService;
import com.odc.Apiodkerp.Service.ListePostulantService;
import com.odc.Apiodkerp.Service.PostulantService;
import com.odc.Apiodkerp.Service.PostulantTrieService;
import com.odc.Apiodkerp.Service.PresenceService;
import com.odc.Apiodkerp.Service.RoleService;
import com.odc.Apiodkerp.Service.SalleService;
import com.odc.Apiodkerp.Service.TirageService;
import com.odc.Apiodkerp.Service.TypeActiviteService;
import com.odc.Apiodkerp.Service.UtilisateurService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
    @PostMapping("/creersalle/{iduser}")
    public ResponseEntity<Object> creerSalle(@RequestBody Salle salle, @PathVariable("iduser") Long iduser) {
        try {
            Utilisateur utilisateur = utilisateurService.getById(iduser);
            System.out.println(utilisateur);
            if (utilisateur.getRole() == RoleService.GetByLibelle("ADMIN")) {
                salle.setUtilisateur(utilisateur);
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, salleService.create(salle));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "Lien pour modifier une salle")
    @PutMapping("/modifiersalle/{id}")
    public ResponseEntity<Object> modifier(@RequestBody Salle salle, @PathVariable long id) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, salleService.update(salle, id));
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "Lien pour modifier une salle")
    @DeleteMapping("/supprimersalle/{id}")
    public ResponseEntity<Object> supprimer(@PathVariable long id) {
        try {
            salleService.delete(id);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, "Salle supprimer avec succès !");
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "Lien pour modifier une salle")
    @GetMapping("/attribuersalle/{idsalle}/{idactivite}")
    public ResponseEntity<Object> attribuerSalle(@PathVariable long idsalle, @PathVariable long idactivite) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    activiteService.attribuerSalle(idsalle, idactivite));
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // ---------------------------CRUD
    // USER-------------------------------------------------------------->
    @ApiOperation(value = "Creer un utilisateur.")
    @PostMapping("/create/user")
    public ResponseEntity<Object> createUser(@RequestParam(value = "data") String data,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(data, Utilisateur.class);

            Role role = RoleService.GetByLibelle("USER");

            if (utilisateurService.getByEmail(utilisateur.getEmail()) == null) {
                utilisateur.setRole(role);
                if (file != null) {
                    utilisateur.setImage(SaveImage.save("user", file, utilisateur.getEmail()));
                }
                Utilisateur NewUser = utilisateurService.creer(utilisateur);
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, NewUser);
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Adresse mail existante");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "Modifier un utilisateur.")
    @PutMapping("/update/user/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody Utilisateur utilisateur,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            if (file != null) {
                SaveImage.save("user", file, utilisateur.getEmail());
            }
            long userid = utilisateur.getId();
            Utilisateur UpdateUtilisateur = utilisateurService.update(utilisateur);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, UpdateUtilisateur);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    @ApiOperation(value = "Supprimer un utilisateur")
    @DeleteMapping("/delete/user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        try {
            utilisateurService.delete(id);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Affichager tout les utilisateurs")
    @GetMapping("/getAll/user")
    public ResponseEntity<Object> GetAllUser() {
        try {
            Role uRole = RoleService.GetByLibelle("USER");
            List<Utilisateur> getAllUtilisateur = utilisateurService.RetrouverParRole(uRole);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, getAllUtilisateur);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    @ApiOperation(value = "Affichager un utilisateur")
    @GetMapping("/get/user/{id}")
    public ResponseEntity<Object> GetIdUtilisateur(@PathVariable("id") Long id) {
        try {
            Utilisateur idUser = utilisateurService.getById(id);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, idUser);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // ---------------------------CRUD
    // Responsable-------------------------------------------------------------->
    @ApiOperation(value = "Creer un responsable.")
    @PostMapping("/create/responsable/{idAdmin}")
    public ResponseEntity<Object> createResponsable(@RequestParam(value = "data") String data,
            @PathVariable("idAdmin") Long idAdmin,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Utilisateur admin = utilisateurService.getById(idAdmin);
            if (admin.getRole() == RoleService.GetByLibelle("ADMIN")) {
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
    @PutMapping("/update/responsable/{idResponsable}")
    public ResponseEntity<Object> updateResponsable(@RequestParam(value = "data") String data,
            @PathVariable("idResponsable") Long idResponsable,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(data, Utilisateur.class);
            Utilisateur responsable = utilisateurService.getById(idResponsable);

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

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "Supprimer un responsable")
    @DeleteMapping("/delete/responsable/{idAdmin}/{idResponsable}")
    public ResponseEntity<Object> deleteResponsable(@PathVariable("idAdmin") Long idAdmin,
            @PathVariable("idResponsable") Long idResponsable) {
        try {
            Utilisateur admin = utilisateurService.getById(idAdmin);
            if (admin.getRole() == RoleService.GetByLibelle("ADMIN")) {
                utilisateurService.delete(idResponsable);
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    @ApiOperation(value = "Affichager toute les responsables")
    @GetMapping("/getAll/responsable")
    public ResponseEntity<Object> GetAllResponsable() {
        try {
            Role reponsable = RoleService.GetByLibelle("RESPONSABLE");
            List<Utilisateur> getAllResponsable = utilisateurService.RetrouverParRole(reponsable);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, getAllResponsable);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    @ApiOperation(value = "Affichager un responsable")
    @GetMapping("/get/responsable/{id}")
    public ResponseEntity<Object> GetIdResponsable(@PathVariable("id") Long id, @RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur idResponsable = utilisateurService.getById(id);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, idResponsable);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // ---------------------------CRUD
    // ENTITE-------------------------------------------------------------->
    @ApiOperation(value = "Creer un entite.")
    @PostMapping("/create/entite")
    public ResponseEntity<Object> createEntite(@RequestBody Entite entite) {
        try {
            Entite NewEntite = entiteService.Create(entite);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, NewEntite);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Modifier un entite.")
    @PutMapping("/update/entite/{id}")
    public ResponseEntity<Object> updateEntite(@PathVariable("id") Long id, @RequestBody Entite entite) {
        try {
            Entite UpdateEntite = entiteService.Update(id, entite);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, UpdateEntite);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Supprimer un entite")
    @DeleteMapping("/delete/entite/{id}")
    public ResponseEntity<Object> DeleteEntite(@PathVariable Long id) {
        try {
            entiteService.Delete(id);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Affichager tout les entités")
    @GetMapping("/getAll/entite")
    public ResponseEntity<Object> GetAllEntite() {
        try {
            List<Entite> getAllEntite = entiteService.GetAll();
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, getAllEntite);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Affichager une entite")
    @GetMapping("/get/entite/{id}")
    public ResponseEntity<Object> GetIdEntite(@RequestParam("id") Long id, @RequestBody Entite entite) {
        try {
            Entite idEntite = entiteService.GetById(id);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, idEntite);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    /// active un utilisateur
    @ApiOperation(value = "Active un utilisateur")
    @GetMapping("/active/{idAmin}/{idUser}")
    ResponseEntity<Object> activeUser(@PathVariable("idAmin") Long idAmin, @PathVariable("idUser") Long idUser) {

        try {
            Utilisateur admin = utilisateurService.getById(idAmin);
            if (admin.getRole() == RoleService.GetByLibelle("ADMIN")) {
                Utilisateur user = utilisateurService.getById(idUser);
                user.setActive(true);
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
    @GetMapping("/desactive/{idAmin}/{idUser}")
    ResponseEntity<Object> desactiveUser(@PathVariable("idAmin") Long idAmin, @PathVariable("idUser") Long idUser) {

        try {
            Utilisateur admin = utilisateurService.getById(idAmin);
            if (admin.getRole() == RoleService.GetByLibelle("ADMIN")) {
                Utilisateur user = utilisateurService.getById(idUser);
                user.setActive(false);
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
    @PostMapping("/Typeactivite/creer")
    public ResponseEntity<Object> CreateTypeActivite(@RequestBody TypeActivite typeActivite) {
        try {
            if (typeActiviteService.getByLibelle(typeActivite.getLibelle()) == null) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, typeActiviteService.creer(typeActivite));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "libelle existant");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "methode pour la Suppression d'une type d' activité.")
    @PostMapping("/Typeactivite/{id}")
    public ResponseEntity<Object> SupprimerTypeActivite(@PathVariable long id, @RequestBody TypeActivite typeActivite) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, typeActiviteService.delete(id));

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "methode pour la modification d'une type d' activité.")
    @PostMapping("/Typeactivite/modification")
    public ResponseEntity<Object> ModifTypeActivite(@RequestBody TypeActivite typeActivite) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, typeActiviteService.update(typeActivite));

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    // :::::::::::::::total postulant ::::::::::::::::::::

    @ApiOperation(value = "Total postulant")
    @GetMapping("/totalpersonnel")
    public ResponseEntity<Object> TotalPostulant() {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, utilisateurService.TotalPersonnel());

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    // :::::::::::::::total entite ::::::::::::::::::::

    @ApiOperation(value = "totalentite")
    @GetMapping("/totalentite")
    public ResponseEntity<Object> TotalEntite() {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, utilisateurService.TotalEntite());

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    //// :::::::::::::::::::::::::::::::l'ensemble des activites en cour, à venir ,
    //// termine

    // activités en avenir
    @ApiOperation(value = "activites/avenir")
    @GetMapping("activites/avenir")
    public ResponseEntity<Object> ActivitesAvenir() {
        try {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, activiteService.Avenir());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // activités en cour
    @ApiOperation(value = "activites/encour")
    @GetMapping("activites/encour")
    public ResponseEntity<Object> ActivitesEncour() {
        try {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, activiteService.Encour());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // activités termines
    @ApiOperation(value = "activites/termines")
    @GetMapping("activites/termines")
    public ResponseEntity<Object> ActivitesTermines() {
        try {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, activiteService.Termine());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

}
