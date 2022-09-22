package com.odc.Apiodkerp.Controller;

import com.odc.Apiodkerp.Models.Entite;
import org.graalvm.compiler.core.common.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Models.Role;
import com.odc.Apiodkerp.Models.Utilisateur;
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
                                              @PathVariable("password") String passord) {

        try {
            Utilisateur Superutilisateur = utilisateurService.login(login, passord);
            Role admin = new Role();
            if (Superutilisateur != null) {
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


    //---------------------------CRUD USER-------------------------------------------------------------->
    @ApiOperation(value = "Creer un utilisateur.")
    @PostMapping("/create/user")
    public ResponseEntity<Object> createUser(@RequestBody Utilisateur utilisateur) {
        try {
            Role role = RoleService.GetByLibelle("USER");
            utilisateur.setRole(role);
            Utilisateur NewUser = utilisateurService.creer(utilisateur);
            return new ResponseEntity<>(NewUser, HttpStatus.CREATED);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("Utilisateur non trouvé", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "Modifier un utilisateur.")
    @PutMapping("/update/user/{id}")
    public ResponseEntity<Utilisateur> updateUser(@RequestBody Utilisateur utilisateur) {
        Utilisateur UpdateUtilisateur = utilisateurService.update(utilisateur);
        return new ResponseEntity<>(UpdateUtilisateur, HttpStatus.OK);
    }

    @ApiOperation(value = "Supprimer un utilisateur")
    @DeleteMapping("/delete/user/{id}")
    public ResponseEntity<Utilisateur> deleteUser(@PathVariable Long id) {
        utilisateurService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Affichager tout les utilisateurs")
    @GetMapping("/getAll/user")
    public ResponseEntity<List<Utilisateur>> GetAllUser() {
        List<Utilisateur> getAllUtilisateur = utilisateurService.getAll();
        return new ResponseEntity<>(getAllUtilisateur, HttpStatus.OK);
    }

    @ApiOperation(value = "Affichager un utilisateur")
    @GetMapping("/get/user/{id}")
    public ResponseEntity<Object> GetIdUtilisateur(@PathVariable("id") Long id, @RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur idUser = utilisateurService.getById(id);
            return new ResponseEntity<>(idUser, HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("id non trouvé", HttpStatus.OK, e.getMessage());
        }
    }


    //---------------------------CRUD Responsable-------------------------------------------------------------->
    @ApiOperation(value = "Creer un responsable.")
    @PostMapping("/create/responsable")
    public ResponseEntity<Object> createResponsable(@RequestBody Utilisateur utilisateur) {
        try {
            Role role = RoleService.GetByLibelle("RESPONSABLE");
            utilisateur.setRole(role);
            Utilisateur NewResponsable = utilisateurService.creer(utilisateur);
            return new ResponseEntity<>(NewResponsable, HttpStatus.CREATED);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("Responsable non trouvé", HttpStatus.OK, e.getMessage());
        }
    }


    @ApiOperation(value = "Modifier un responsable.")
    @PutMapping("/update/responsable/{id}")
    public ResponseEntity<Object> updateResponsable(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur UpdateResponsable = utilisateurService.update(utilisateur);
            return new ResponseEntity<>(UpdateResponsable, HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("Responsable non trouvé", HttpStatus.OK, e.getMessage());
        }
    }


    @ApiOperation(value = "Supprimer un responsable")
    @DeleteMapping("/delete/responsable/{id}")
    public ResponseEntity<Object> deleteResponsable(@PathVariable Long id) {
        utilisateurService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Affichager toute les responsables")
    @GetMapping("/getAll/responsable")
    public ResponseEntity<List<Utilisateur>> GetAllResponsable() {
        List<Utilisateur> getAllResponsable = utilisateurService.getAll();
        return new ResponseEntity<>(getAllResponsable, HttpStatus.OK);
    }

    @ApiOperation(value = "Affichager un responsable")
    @GetMapping("/get/responsable/{id}")
    public ResponseEntity<Object> GetIdResponsable(@PathVariable("id") Long id, @RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur idResponsable = utilisateurService.getById(id);
            return new ResponseEntity<>(idResponsable, HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("responsable non trouvé", HttpStatus.OK, e.getMessage());
        }
    }

//---------------------------CRUD ENTITE-------------------------------------------------------------->
        @ApiOperation(value = "Creer un entite.")
        @PostMapping("/create/entite")
        public ResponseEntity<Entite> createEntite (@RequestBody Entite entite){
            Entite NewEntite = entiteService.Create(entite);
            return new ResponseEntity<>(NewEntite, HttpStatus.CREATED);
        }

        @ApiOperation(value = "Modifier un entite.")
        @PutMapping("/update/entite/{id}")
        public ResponseEntity<Entite> updateEntite (@PathVariable("id") Long id, @RequestBody Entite entite){
            Entite UpdateEntite = entiteService.Update(id, entite);
            return new ResponseEntity<>(UpdateEntite, HttpStatus.OK);
        }

        @ApiOperation(value = "Supprimer un entite")
        @DeleteMapping("/delete/entite/{id}")
        public ResponseEntity<Entite> DeleteEntite (@PathVariable Long id){
            entiteService.Delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        @ApiOperation(value = "Affichager tout les entités")
        @GetMapping("/getAll/entite")
        public ResponseEntity<List<Entite>> GetAllEntite () {
            List<Entite> getAllEntite = entiteService.GetAll();
            return new ResponseEntity<>(getAllEntite, HttpStatus.OK);
        }

        @ApiOperation(value = "Affichager une entite")
        @GetMapping("/get/entite/{id}")
        public ResponseEntity<Object> GetIdEntite (@RequestParam("id") Long id, @RequestBody Entite entite){
            try {
                Entite idEntite = entiteService.GetById(id);
                return new ResponseEntity<>(idEntite, HttpStatus.OK);
            } catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("id non trouvé", HttpStatus.OK, e.getMessage());
            }
        }


}