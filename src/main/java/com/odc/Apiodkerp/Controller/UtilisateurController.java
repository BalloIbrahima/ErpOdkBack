package com.odc.Apiodkerp.Controller;

import com.odc.Apiodkerp.Models.*;

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

import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Configuration.SaveImage;
import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.Role;
import com.odc.Apiodkerp.Models.Utilisateur;

import org.springframework.web.bind.annotation.*;

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

    // Pour le login d'un utilisateur
    @ApiOperation(value = "Pour le login d'un utilisateur.")
    @PostMapping("/login/{login}/{password}")
    public ResponseEntity<Object> login(@PathVariable("login") String login,
            @PathVariable("password") String password) {

        try {
            Utilisateur Simpleutilisateur = utilisateurService.login(login, password);
            Role user = RoleService.GetByLibelle("USER");
            if (Simpleutilisateur != null) {
                if (Simpleutilisateur.getRole() == user && Simpleutilisateur.getActive() == true) {

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, Simpleutilisateur);
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

    // Modification de l'activite
    @ApiOperation(value = "Modification de l'activite en fonction de l'id")
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Activite activite,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            if (file != null) {
                activite.setImage(SaveImage.save("activite", file, activite.getNom()));
            }
            return ResponseMessage.generateResponse("error", HttpStatus.OK, activiteService.Update(id, activite));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    // affichage d'activite en fonction de l'id
    @ApiOperation(value = "Affichage de l'activite en fonction de l'id")
    @GetMapping("/afficherActivit/{id}")
    public ResponseEntity<Object> AfficherActivit(@PathVariable long id) {
        try {

            return ResponseMessage.generateResponse("ok", HttpStatus.OK, activiteService.GetById(id));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // Supprimer d'activite en fonction de l'id
    @ApiOperation(value = "Supprimer une activite en fonction de l'id")
    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<Object> supprimer(@PathVariable long id) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, activiteService.Delete(id));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    // Afficher activite en fonction de l'etat
    @ApiOperation(value = "Affichage de l'activite en fonction de son etat")
    @GetMapping("/afficherActiviteEtat/{etat}")
    public ResponseEntity<Object> AfficherActivite(@PathVariable Etat etat) {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, activiteService.GetByEtat(etat));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

        //afficher toutes les activites
        @ApiOperation(value = "Afficher toutes les  activite  ")
        @GetMapping("/lapresence")
        public ResponseEntity<Object> ToutesActivite(){
            try{
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,  activiteService.FindAllAct());
            }
            catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
            }
        }


    //Afficher une activite
    @ApiOperation(value = "Afficher une activite en fonction de l'id ")
    @GetMapping("/lapresence/{idactivite}")
    public ResponseEntity<Object> Afficheractivite(@PathVariable long idactivite ){
        try{
            return ResponseMessage.generateResponse("ok", HttpStatus.OK,  activiteService.GetById(idactivite));
        }
        catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // Ajouter des participants ou apprenants à la liste de presence
    @ApiOperation(value = "Creer la liste de presence ")
    @PostMapping("/lapresence/{idactivite}/{idpostulanttire}")
    public ResponseEntity<Object> presence(@PathVariable long idactivite, @PathVariable long idpostulanttire) {
        try {
            Presence presence = new Presence();
            presence.setActivite(activiteService.GetById(idactivite));
            presence.setPostulantTire(postulantTrieService.read(idpostulanttire));
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, presenceService.creer(presence));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = " afficher la liste de presence en fonction de l'id de l'activite")
    @GetMapping("/lapresence/{idactivite}/")
    public ResponseEntity<Object> Listepresence(@PathVariable long idactivite) {
        try {
            Activite act = activiteService.GetById(idactivite);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, act.getPresences());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    //

    @ApiOperation(value = "Modification de l'entite en fonction de l'id")
    @PutMapping("/updatecc/{id}")
    public ResponseEntity<Object> updateEntite(@PathVariable Long id, @RequestBody Entite entite) {
        try {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, entiteService.Update(id, entite));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }
    ////

    // methode pour la création d'une activité
    @ApiOperation(value = "methode pour la création d'une activité.")
    @PostMapping("/activite/new/{idutilisateur}")
    public ResponseEntity<Object> CreateTirage(@RequestBody Activite activite,
            @PathVariable("idutilisateur") Long idutilisateur,
            @RequestParam(value = "file", required = true) MultipartFile file) {

        if (file != null) {
            try {
                Utilisateur user = utilisateurService.getById(idutilisateur);

                activite.setImage(SaveImage.save("activite", file, activite.getNom()));

                return ResponseMessage.generateResponse("ok", HttpStatus.OK, activiteService.Create(activite));

            } catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
            }
        } else {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, "Fichier vide");
        }

    }
}
