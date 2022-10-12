package com.odc.Apiodkerp.Controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Models.Droit;
import com.odc.Apiodkerp.Models.Historique;
import com.odc.Apiodkerp.Models.TypeActivite;
import com.odc.Apiodkerp.Models.Utilisateur;
import com.odc.Apiodkerp.Service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/typeactivite")
@Api(value = "typeactivite", description = "Les fonctionnalités liées à une type d'activite")
@CrossOrigin
public class TypeActiviteController {

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

    ///////// type activite
    @ApiOperation(value = "methode pour la création d'une type d' activité.")
    @PostMapping("/Typeactivite/creer")
    public ResponseEntity<Object> CreateTypeActivite(@RequestParam(value = "tyepact") String typeActivite,
                                                     @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            TypeActivite typeActivit = new JsonMapper().readValue(typeActivite, TypeActivite.class);
            Utilisateur admin = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),utilisateur.getPassword());
            Droit createtype = droitService.GetLibelle("Create TypeActivite");

            if (admin.getRole().getDroits().contains(createtype)) {
                if (typeActiviteService.getByLibelle(typeActivit.getLibelle()) == null) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + admin.getPrenom() + " " + admin.getNom()
                                + " a crée un type d'activite  de libelle " + typeActivit.getLibelle());
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            typeActiviteService.creer(typeActivit));
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
    @PostMapping("/Typeactivite/{id}")
    public ResponseEntity<Object> SupprimerTypeActivite(@PathVariable long id, @RequestParam(value = "user") String userVenant
            , @RequestParam(value = "tyepact") String typeActivite) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            TypeActivite typeActivit = new JsonMapper().readValue(typeActivite, TypeActivite.class);
            // historique
            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),utilisateur.getPassword());
            Droit deletetetype = droitService.GetLibelle("Delete TypeActivite");

            if (users.getRole().getDroits().contains(deletetetype)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription("" + users.getPrenom() + " " + users.getNom()
                            + " a supprimé un type d'activte du nom de " + typeActivit.getLibelle());
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
    @PostMapping("/Typeactivite/modification")
    public ResponseEntity<Object> ModifTypeActivite(@RequestParam(value = "tyepact") String typeActivite,  @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            TypeActivite typeActivit = new JsonMapper().readValue(typeActivite, TypeActivite.class);

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),utilisateur.getPassword());

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
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, typeActiviteService.update(typeActivit));

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Recuperer l'ensemble des types acticite")
    @PostMapping("/Typeactivite/getall")
    public ResponseEntity<Object> TypaActiviteAll(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),utilisateur.getPassword());

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
}
