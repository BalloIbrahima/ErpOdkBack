package com.odc.Apiodkerp.Controller;

import com.odc.Apiodkerp.Models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

import java.util.ArrayList;

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
    @PostMapping("/creersalle")
    public ResponseEntity<Object> creerSalle(@RequestBody Salle salle) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, salleService.create(salle));
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
    public ResponseEntity<Object> createUser(@RequestBody Utilisateur utilisateur,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Role role = RoleService.GetByLibelle("USER");
            utilisateur.setRole(role);
            if (file != null) {
                utilisateur.setImage(SaveImage.save("user", file, utilisateur.getEmail()));
            }
            Utilisateur NewUser = utilisateurService.creer(utilisateur);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, NewUser);
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
    public ResponseEntity<Object> GetIdUtilisateur(@PathVariable("id") Long id, @RequestBody Utilisateur utilisateur) {
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
    @PostMapping("/create/responsable")
    public ResponseEntity<Object> createResponsable(@RequestBody Utilisateur utilisateur,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Role role = RoleService.GetByLibelle("RESPONSABLE");
            utilisateur.setRole(role);
            if (file != null) {
                utilisateur.setImage(SaveImage.save("user", file, utilisateur.getEmail()));
            }
            Utilisateur NewResponsable = utilisateurService.creer(utilisateur);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, NewResponsable);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "Modifier un responsable.")
    @PutMapping("/update/responsable/{id}")
    public ResponseEntity<Object> updateResponsable(@RequestBody Utilisateur utilisateur,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {

            if (file != null) {
                utilisateur.setImage(SaveImage.save("user", file, utilisateur.getEmail()));
            }
            Utilisateur UpdateResponsable = utilisateurService.update(utilisateur);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, UpdateResponsable);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "Supprimer un responsable")
    @DeleteMapping("/delete/responsable/{id}")
    public ResponseEntity<Object> deleteResponsable(@PathVariable Long id) {
        utilisateurService.delete(id);
        return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);

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
    @GetMapping("/active/{id}")
    ResponseEntity<Object> activeUser(@PathVariable("id") Long id) {

        try {
            Utilisateur user = utilisateurService.getById(id);
            user.setActive(true);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, utilisateurService.creer(user));

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    /// desactive un utilisateur
    @ApiOperation(value = "Active un utilisateur")
    @GetMapping("/desactive/{id}")
    ResponseEntity<Object> desactiveUser(@PathVariable("id") Long id) {

        try {
            Utilisateur user = utilisateurService.getById(id);
            user.setActive(false);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, utilisateurService.creer(user));

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

}
