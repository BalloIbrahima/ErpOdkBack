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
import com.odc.Apiodkerp.Service.ActiviteService;
import com.odc.Apiodkerp.Service.EntiteService;
import com.odc.Apiodkerp.Service.EtatService;
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

    @Autowired
    private TacheService tacheService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private IntervenantExterneService intervenantExterneService;

    @Autowired
    private NotificationService notificationService;

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
            Utilisateur user =   utilisateurService.getById(iduser);
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription(""+user.getPrenom()+ " "+user.getNom()+" a crée une salle du nom de "+salle.getLibelle());
                historiqueService.Create(historique);}
            catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }
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

    // ::::::::::Recuperer salle par id
    @ApiOperation(value = "Recuperer salle par id")
    @GetMapping("getSalle/{iduser}/{id}")
    public ResponseEntity<Object> getSalle(@PathVariable("id") Long id,@PathVariable("iduser") Long iduser) {
        try {
            Utilisateur user =   utilisateurService.getById(iduser);
            Salle salle = new Salle();

            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription(""+user.getPrenom()+ " "+user.getNom()+" a recuperer une salle du nom de "+salle.getId());
                historiqueService.Create(historique);}
            catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, salleService.read(id));

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    @ApiOperation(value = "Lien pour modifier une salle")
    @PutMapping("/modifiersalle/{iduser}/{id}")
    public ResponseEntity<Object> modifier(@RequestBody Salle salle, @PathVariable long id,@PathVariable long iduser) {
        try {
            Utilisateur user =   utilisateurService.getById(iduser);
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription(""+user.getPrenom()+ " "+user.getNom()+" a modifier une salle du nom de "+salleService.getByIdsalle(id).getLibelle());
                historiqueService.Create(historique);}
            catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, salleService.update(salle, id));
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "Lien pour suprimer une salle")
    @DeleteMapping("/supprimersalle/{iduser}/{id}")
    public ResponseEntity<Object> supprimer(@PathVariable long id,@PathVariable long iduser) {
        try {
            Utilisateur user =   utilisateurService.getById(iduser);
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription(""+user.getPrenom()+ " "+user.getNom()+" a supprimé une salle du nom de ");
                historiqueService.Create(historique);}
            catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }
            salleService.delete(id);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, "Salle supprimer avec succès !");
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "Lien pour lier une salle à une activite")
    @GetMapping("/attribuersalle/{iduser}/{idsalle}/{idactivite}")
    public ResponseEntity<Object> attribuerSalle(@PathVariable long idsalle, @PathVariable long idactivite,@PathVariable long iduser) {
        try {
            Utilisateur user =   utilisateurService.getById(iduser);
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription(""+user.getPrenom()+ " "+user.getNom()+" a attribué  une salle du nom de  "+salleService.getByIdsalle(idsalle)+ " à l'activte "+activiteService.GetById(idactivite));
                historiqueService.Create(historique);}
            catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    activiteService.attribuerSalle(idsalle, idactivite));
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // pour recuperer toutes les salles
    @ApiOperation(value = "Pour recuperer toutes les salles")
    @GetMapping("/salle/all")
    public ResponseEntity<Object> getAll() {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    salleService.getAll());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // ---------------------------CRUD
    // USER-------------------------------------------------------------->
    @ApiOperation(value = "Creer un utilisateur.")
    @PostMapping("/create/user/{idadmin}")
    public ResponseEntity<Object> createUser(@RequestParam(value = "data") String data,@PathVariable long idadmin,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Utilisateur user =   utilisateurService.getById(idadmin);


            Utilisateur utilisateur = new JsonMapper().readValue(data, Utilisateur.class);

            Role role = RoleService.GetByLibelle("USER");

            if (utilisateurService.getByEmail(utilisateur.getEmail()) == null) {
                utilisateur.setRole(role);
                if (file != null) {
                    utilisateur.setImage(SaveImage.save("user", file, utilisateur.getEmail()));
                }

                //Historique
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(""+user.getPrenom()+ " "+user.getNom()+" a crée un utilisateur du nom de "+utilisateur.getNom());
                    historiqueService.Create(historique);}
                catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }

                Utilisateur NewUser = utilisateurService.creer(utilisateur);
                System.out.println(NewUser.getLogin());
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
    @PutMapping("/update/user/{idadmin}/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody Utilisateur utilisateur, @PathVariable long idadmin,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            if (file != null) {
                SaveImage.save("user", file, utilisateur.getEmail());
            }
            //Historique
            Utilisateur user =   utilisateurService.getById(idadmin);
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription(""+user.getPrenom()+ " "+user.getNom()+" a modifier un utilisateur du nom de ");
                historiqueService.Create(historique);}
            catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

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
    @DeleteMapping("/delete/user/{idadmin}/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id,@PathVariable Long idadmin) {
        try {
            utilisateurService.delete(id);

            //Historique
            Utilisateur user =   utilisateurService.getById(idadmin);
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription(""+user.getPrenom()+ " "+user.getNom()+" a supprimé un utilisateur du nom de "+utilisateurService.getById(id));
                historiqueService.Create(historique);}
            catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }

            return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Affichager tout les utilisateurs")
    @GetMapping("/getAll/user/{iduser}")
    public ResponseEntity<Object> GetAllUser(@PathVariable long  iduser) {
        try {
            Utilisateur user =   utilisateurService.getById(iduser);
            try {
            Historique historique = new Historique();
            Date datehisto = new Date();
            historique.setDatehistorique(datehisto);
            historique.setDescription(""+user.getPrenom()+ "a affiché tous les utilisateurs");
            historiqueService.Create(historique);

            System.out.println(historique.getDescription());

            Role uRole = RoleService.GetByLibelle("USER");
            List<Utilisateur> getAllUtilisateur = utilisateurService.RetrouverParRole(uRole);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, getAllUtilisateur);

            } catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }


        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    @ApiOperation(value = "Affichager un utilisateur")
    @GetMapping("/get/user/{iduser}/{id}")
    public ResponseEntity<Object> GetIdUtilisateur(@PathVariable("id") Long id,@PathVariable("iduser") Long iduser) {
        try {

            Utilisateur user =   utilisateurService.getById(iduser);
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription(""+user.getPrenom()+ " "+user.getNom()+" a afficher  un utilisateur du nom de "+utilisateurService.getById(id));
                historiqueService.Create(historique);}
            catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

            }
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

                    //Histoirque
                    Utilisateur user =   utilisateurService.getById(idAdmin);
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(""+user.getPrenom()+ " "+user.getNom()+" a crée un responsable du nom de "+utilisateur.getNom()+" "+utilisateur.getPrenom());
                        historiqueService.Create(historique);}
                    catch (Exception e) {
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

                Utilisateur user =   utilisateurService.getById(idAdmin);
                Utilisateur respon =   utilisateurService.getById(idResponsable);
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(""+user.getPrenom()+ " "+user.getNom()+" a supprimé un responsable du nom de "+respon.getNom()+" "+respon.getPrenom());
                    historiqueService.Create(historique);}
                catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }
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
    @GetMapping("/getAll/responsable/{iduser}")
    public ResponseEntity<Object> GetAllResponsable(@PathVariable long iduser) {
        try {

            Utilisateur user =   utilisateurService.getById(iduser);
            try {
                Historique historique = new Historique();
                Date datehisto = new Date();
                historique.setDatehistorique(datehisto);
                historique.setDescription(""+user.getPrenom()+ " "+user.getNom()+" a affiché tous les responsables");
                historiqueService.Create(historique);}
             catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }
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

    @ApiOperation(value = "Recuperer l'ensemble des types acticite")
    @GetMapping("Typeactivite/getall")
    public ResponseEntity<Object> TypaActiviteAll() {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, typeActiviteService.getAll());

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
    // :::::::::::::: ::::::::::::::debut mise a jour de madame ::::::::::::::::::::

    // :::::::::::Liste des activites par entite ::::::::::::::::
    @ApiOperation(value = "activites par entite")
    @GetMapping("activites/entite/{identite}")
    public ResponseEntity<Object> ActivitesParEntite(@PathVariable long identite) {
        try {

            return ResponseMessage.generateResponse("error", HttpStatus.OK, activiteService.ActiviteEntiteid(identite));
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
    @GetMapping("activites/entite/{idactivite}/{date1}/{date2}")
    public ResponseEntity<Object> PostulantParActivite(@PathVariable long idactivite, @PathVariable Date date1,
            @PathVariable Date date2) {
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
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, postulantTrieService.getAll());
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
    @GetMapping("activites/entite/{identite}/{idstatut}")
    public ResponseEntity<Object> ActivitesParEntiteEtParstatut(@PathVariable long identite,
            @PathVariable long idstatut) {
        try {
             Etat etat = etatService.GetById(idstatut);//on recup l'etat en fonction de l'id
             Entite entite=entiteService.GetById(identite);

             //activite de l'estat forni
             List<Activite> etatActivite=etat.getActivite();
             List<Activite> aRetourner=new ArrayList<>();
             for(Activite a:etatActivite){
                 if(a.getCreateur().getMonEntite()==entite){
                     aRetourner.add(a);
                 }
             }

            Activite activite = (Activite) activiteService.ActiviteEntiteid(identite);// recuperation des activite d'une entite donnée
             if(activite.getEtat()== etat.getActivite()){
                 return ResponseMessage.generateResponse("error", HttpStatus.OK, activiteService.GetAll());
             }
             else {
                 return ResponseMessage.generateResponse("error", HttpStatus.OK, "Une erreur s'est produit");
            Etat etat = etatService.GetById(idstatut);// on recup l'etat en fonction de l'id
            Activite activite = (Activite) activiteService.ActiviteEntiteid(identite);// recuperation des activite d'une
                                                                                      // entite donnée
            if (activite.getEtat() == etat.getActivite()) {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, activiteService.GetAll());
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Une erreur s'est produit");

            }
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // ::::::::::::::::::::::
    @ApiOperation(value = "Statut d'une activite en fonction de son id")
    @GetMapping("statut/activite/{id}")
    public ResponseEntity<Object> ActivitesTermines(@PathVariable long id) {
        try {
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

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // ::::::::::::::Comparaison date pour salle disponible
    // ::::::::::::::::::::::::::::::
    @ApiOperation(value = "Comparaison date pour salle disponible")
    @GetMapping("SalleDispo/{date1}/{date2}")
    public ResponseEntity<Object> SalleDispoDate(@PathVariable Date date1, @PathVariable Date date2) {
        try {
            Activite act = activiteService.FindAllAct();
            List<Salle> salle = new ArrayList<>();

            if (act.getDateDebut().before(date1) && act.getDateDebut().before(date2) && act.getDateFin().after(date1)
                    && act.getDateFin().after(date2)
                    || act.getDateDebut().after(date1) && act.getDateDebut().after(date2)
                            && act.getDateFin().before(date1) && act.getDateFin().before(date2)) {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, salle.add(act.getSalle()));

            }
            return ResponseMessage.generateResponse("error", HttpStatus.OK, activiteService.Termine());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // :::::::::::::::::::::::Les users active
    @ApiOperation(value = "Les utilisateurs active")
    @GetMapping("/getUsers/active")
    public ResponseEntity<Object> getUsersActives() {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, utilisateurService.RecupererUserParEtat(true));

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // :::::::::::::::::::::::Les users desactives
    @ApiOperation(value = "Les utilisateurs desactives")
    @GetMapping("/getUsers/desactive")
    public ResponseEntity<Object> getUsersDesactives() {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    utilisateurService.RecupererUserParEtat(false));

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
    //

    // :::::::::::::::::::::::Les salles disponible
    @ApiOperation(value = "Les salles disponible")
    @GetMapping("/getSalles/disponible")
    public ResponseEntity<Object> getSallesDispo() {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, salleService.ParEtat(true));

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // :::::::::::::::::::::::Les salles indisponible
    @ApiOperation(value = "Les salles indisponible")
    @GetMapping("/getSalles/indisponible")
    public ResponseEntity<Object> getSallesIndispo() {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    salleService.ParEtat(false));

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // /////////////////////INTERVENANT externe:::
    @ApiOperation(value = "Nouveau intervenant")
    @PostMapping("/intervenat/new")
    public ResponseEntity<Object> CreateIntervenant(@RequestBody IntervenantExterne intervenantExterne) {

        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    intervenantExterneService.creer(intervenantExterne));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Mettre à jour intervenant")
    @PutMapping("/intervenat/update/{id}")
    public ResponseEntity<Object> UpdateIntervenant(@RequestBody IntervenantExterne intervenantExterne,
            @PathVariable("id") Long id) {

        try {
            IntervenantExterne iE = intervenantExterneService.getById(id);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    intervenantExterneService.update(iE));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Suprimer intervenant")
    @DeleteMapping("/intervenat/delete/{id}")
    public ResponseEntity<Object> DeleteIntervenant(@PathVariable("id") Long id) {

        try {
            intervenantExterneService.delete(id);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    "Suprimer");
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "L'ensemble des intervenant externes")
    @GetMapping("/intervenat/all")
    public ResponseEntity<Object> TotalIntervenant() {

        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    intervenantExterneService.getAll());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // :::::::::::::::::::::::::::::::::::

    // ::::::::::::::taches
    @ApiOperation(value = "Nouvelle tache")
    @PostMapping("/tache/new")
    public ResponseEntity<Object> CreateTache(@RequestBody Tache tache) {

        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    tacheService.creer(tache));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Mettre à jour tache")
    @PutMapping("/tache/update/{id}")
    public ResponseEntity<Object> UpdateIntervenant(@RequestBody Tache tache,
            @PathVariable("id") Long id) {

        try {
            Tache tache2 = tacheService.getById(id);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    tacheService.update(tache2));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Suprimer tache")
    @DeleteMapping("/tache/delete/{id}")
    public ResponseEntity<Object> DeleteTache(@PathVariable("id") Long id) {

        try {
            tacheService.delete(id);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    "Suprimer");
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "L'ensemble des taches")
    @GetMapping("/tache/all")
    public ResponseEntity<Object> TotalTaches() {

        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    tacheService.getAll());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // :::::::::::::::::::::::::::::::::::

    // :::::::::::::::::::::::::::::::::::

    // ::::::::::::::Statut
    @ApiOperation(value = "Nouveau statut")
    @PostMapping("/status/new")
    public ResponseEntity<Object> CreateStatut(@RequestBody Statut status) {

        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    statusService.creer(status));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Mettre à jour statut")
    @PutMapping("/status/update/{id}")
    public ResponseEntity<Object> UpdateStatut(@RequestBody Statut status,
            @PathVariable("id") Long id) {

        try {
            Statut stat = statusService.getById(id);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    statusService.update(stat));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Suprimer statut")
    @DeleteMapping("/status/delete/{id}")
    public ResponseEntity<Object> DeleteStatus(@PathVariable("id") Long id) {

        try {
            tacheService.delete(id);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    "Suprimer");
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "L'ensemble des status")
    @GetMapping("/status/all")
    public ResponseEntity<Object> TotalStatus() {

        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    statusService.getAll());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // :::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "L'ensemble des notification")
    @GetMapping("/notification/all")
    public ResponseEntity<Object> TotalNotification() {

        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    notificationService.getAll());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }



    // :::::::::::::::::::::::::::::::::::::::Apprenants ou Participant :::::::::::::::::::::::::::::::
    @ApiOperation(value = "Ajouterr AppouParticipant")
    @PostMapping("/aoup")
    public ResponseEntity<Object> ajouterAouP(@RequestBody AouP aoup) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    aouPService.Create(aoup));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }


//::::::::::::::::::::::::::::::::::: Modifier AouP ::::::::::::::::::::::::::::::::::::
    @ApiOperation(value = "Modifier AppouParticipant")
    @PutMapping("/aoup/modifier/{id}")
    public ResponseEntity<Object> ModifierAouP(@PathVariable long id, @RequestBody AouP aoup) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    aouPService.Update(id,aoup));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
//::::::::::::::::::::::::::::::::::: Supprimer AouP ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Supprimer AouP")
    @DeleteMapping("/aoup/supprimer/{id}")
    public ResponseEntity<Object> SupprimerAouP(@PathVariable long id) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    aouPService.Delete(id));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    //::::::::::::::::::::::::::::::::::: Get pzr id AouP ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Apprenant ou participant par id")
    @GetMapping("/aoup/GetId/{id}")
    public ResponseEntity<Object> GetAouPparId(@PathVariable long id) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    aouPService.GetById(id));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }


    // :::::::::::::::::::::::::::::::::::::::DESIGNATION :::::::::::::::::::::::::::::::
    @ApiOperation(value = "Ajouterr Designation")
    @PostMapping("/designation")
    public ResponseEntity<Object> ajouterDesignation(@RequestBody Designation designation) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    designationService.Create(designation));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }


    //::::::::::::::::::::::::::::::::::: Modifier Designation ::::::::::::::::::::::::::::::::::::
    @ApiOperation(value = "Modifier Desi")
    @PutMapping("/designation/modifier/{id}")
    public ResponseEntity<Object> ModifierDesignation(@PathVariable long id, @RequestBody Designation designation) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    designationService.Update(id,designation));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
//::::::::::::::::::::::::::::::::::: Supprimer AouP ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Supprimer Designation")
    @DeleteMapping("/designation/supprimer/{id}")
    public ResponseEntity<Object> SupprimerDesignation(@PathVariable long id) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    designationService.Delete(id));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
    //::::::::::::::::::::::::::::::::::: Get pzr id AouP ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Designation par id")
    @GetMapping("/Designation/GetId/{id}")
    public ResponseEntity<Object> GetDesigantionparId(@PathVariable long id) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    designationService.GetById(id));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }




    // :::::::::::::::::::::::::::::::::::::::Droit :::::::::::::::::::::::::::::::
    @ApiOperation(value = "Ajouter Droit")
    @PostMapping("/droit")
    public ResponseEntity<Object> ajouterDroit(@RequestBody Droit droit) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    droitService.Create(droit));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }


    //::::::::::::::::::::::::::::::::::: Modifier Droit ::::::::::::::::::::::::::::::::::::
    @ApiOperation(value = "Modifier Droit")
    @PutMapping("/droit/modifier/{id}")
    public ResponseEntity<Object> ModifierDroit(@PathVariable long id, @RequestBody Droit droit) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    droitService.Update(id,droit));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
//::::::::::::::::::::::::::::::::::: Supprimer droit ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Supprimer Droit")
    @DeleteMapping("/droit/supprimer/{id}")
    public ResponseEntity<Object> SupprimerDroit(@PathVariable long id) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    droitService.Delete(id));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
    //::::::::::::::::::::::::::::::::::: Get pzr id AouP ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Droit par id")
    @GetMapping("/droit/GetId/{id}")
    public ResponseEntity<Object> GetDroitparId(@PathVariable long id) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    droitService.GetById(id));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }


    // :::::::::::::::::::::::::::Droit par libelle ::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Droit par libelle")
    @GetMapping("/droit/GetLibelle/{libelle}")
    public ResponseEntity<Object> GetDroitparLibelle(@PathVariable String libelle) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    droitService.GetLibelle(libelle));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }





    // :::::::::::::::::::::::::::::::::::::::FormationEmail :::::::::::::::::::::::::::::::
        @ApiOperation(value = "Ajouter FormatEmail")
    @PostMapping("/formaemail")
    public ResponseEntity<Object> ajouterFormatEmail(@RequestBody FormatEmail formatEmail) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    formatEmailService.Create(formatEmail));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }


    //::::::::::::::::::::::::::::::::::: Modifier FormatEmail ::::::::::::::::::::::::::::::::::::
    @ApiOperation(value = "Modifier formatEmail")
    @PutMapping("/formatemail/modifier/{id}")
    public ResponseEntity<Object> ModifierFormatEmail(@PathVariable long id, @RequestBody FormatEmail formatEmail) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    formatEmailService.Update(id,formatEmail));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
//::::::::::::::::::::::::::::::::::: Supprimer FormatEmail ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Supprimer Format Email")
    @DeleteMapping("/formatemail/supprimer/{id}")
    public ResponseEntity<Object> SupprimerFormatEmail(@PathVariable long id) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    formatEmailService.Delete(id));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
    //::::::::::::::::::::::::::::::::::: Get pzr id FormatEmail ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Format email par id")
    @GetMapping("/formatEmail/GetId/{id}")
    public ResponseEntity<Object> GetFormatEMailparId(@PathVariable long id) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                    formatEmailService.GetById(id));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

}
