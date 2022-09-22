package com.odc.Apiodkerp.Controller;

import com.odc.Apiodkerp.Models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.odc.Apiodkerp.Configuration.ResponseMessage;
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
    @ApiOperation(value = "Lien pour créer une salle")
    @PostMapping("creersalle")
    public ResponseEntity<Object> creerSalle(@RequestBody Salle salle) {
        try {
            return ResponseMessage.generateResponse("ok",HttpStatus.OK,salleService.create(salle));
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error",HttpStatus.OK,e.getMessage());
        }
    }
    @ApiOperation(value = "Lien pour modifier une salle")
    @PutMapping("modifiersalle/{id}")
    public ResponseEntity<Object> modifier(@RequestBody Salle salle, @PathVariable long id) {
        try {
            return ResponseMessage.generateResponse("ok",HttpStatus.OK,salleService.update(salle,id));
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error",HttpStatus.OK,e.getMessage());
        }
    }
    @ApiOperation(value = "Lien pour modifier une salle")
    @DeleteMapping("supprimersalle/{id}")
    public ResponseEntity<Object> supprimer(@PathVariable long id) {
        try {
            salleService.delete(id);
            return ResponseMessage.generateResponse("ok",HttpStatus.OK,"Salle supprimer avec succès !");
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error",HttpStatus.OK,e.getMessage());
        }
    }
    @ApiOperation(value = "Lien pour modifier une salle")
    @GetMapping("attribuersalle/{idsalle}/{idactivite}")
    public ResponseEntity<Object> attribuerSalle(@PathVariable long idsalle,@PathVariable long idactivite) {
        try {
            return ResponseMessage.generateResponse("ok",HttpStatus.OK,activiteService.attribuerSalle(idsalle,idactivite));
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error",HttpStatus.OK,e.getMessage());
        }
    }

}
