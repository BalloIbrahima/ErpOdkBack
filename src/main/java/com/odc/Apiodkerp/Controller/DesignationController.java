package com.odc.Apiodkerp.Controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Models.Designation;
import com.odc.Apiodkerp.Models.Droit;
import com.odc.Apiodkerp.Models.Historique;
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
@RequestMapping("/designation")
@Api(value = "designation", description = "Les fonctionnalités liées à une designation")
@CrossOrigin
public class DesignationController {

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

    // :::::::::::::::::::::::::::::::::::::::DESIGNATION
    // :::::::::::::::::::::::::::::::
    @ApiOperation(value = "Ajouter Designation")
    @PostMapping("/designation")
    public ResponseEntity<Object> ajouterDesignation(@RequestParam(value = "designation") String designation,
            @RequestParam(value = "user") String userVenant) {
        try {

            // Histroique
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Designation desi = new JsonMapper().readValue(designation, Designation.class);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Cdesignation = droitService.GetLibelle("Create Designation");

            if (user.getRole().getDroits().contains(Cdesignation)) {
                try {
                    desi.setEtat(true);
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription("" + user.getPrenom() + " " + user.getNom() + " a ajoute la desigantion "
                            + desi.getLibelle());
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            designationService.Create(desi));
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

    // ::::::::::::::::::::::::::::::::::: Modifier Designation
    // ::::::::::::::::::::::::::::::::::::
    @ApiOperation(value = "Modifier Desi")
    @PostMapping("/designation/modifier/{id}")
    public ResponseEntity<Object> ModifierDesignation(@PathVariable long id,
            @RequestParam(value = "designation") String designation, @RequestParam(value = "user") String userVenant) {
        try {

            // Historique
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Designation desi = new JsonMapper().readValue(designation, Designation.class);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Cdesignation = droitService.GetLibelle("Create Designation");

            if (user.getRole().getDroits().contains(Cdesignation)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription("" + user.getPrenom() + " " + user.getNom() + " a modifie la desigantion "
                            + desi.getLibelle());
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            designationService.Update(id, desi));
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
    @PostMapping("/designation/supprimer/{id}")
    public ResponseEntity<Object> SupprimerDesignation(@PathVariable long id,
            @RequestParam(value = "user") String userVenant) {
        try {
            Designation designation = designationService.GetById(id);

            // Histroique
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Ddesignation = droitService.GetLibelle("Delete Designation");

            if (user.getRole().getDroits().contains(Ddesignation)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique
                            .setDescription("" + user.getPrenom() + " " + user.getNom() + " a supprime la desigantion "
                                    + designation.getLibelle());
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            designationService.Delete(id));
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
    // ::::::::::::::::::::::::::::::::::: Get pzr id AouP
    // ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Designation par id")
    @PostMapping("/Designation/GetId/{id}")
    public ResponseEntity<Object> GetDesigantionparId(@PathVariable long id,
            @RequestParam(value = "user") String userVenant) {
        try {

            // Histroique
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Gdesignation = droitService.GetLibelle("Read Designation");

            if (user.getRole().getDroits().contains(Gdesignation)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            "" + user.getPrenom() + " " + user.getNom() + " a affiche une designation ");
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            designationService.GetById(id));
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


    // :::::::::::::::::::::::::Toutes les  Designations ::::::::::::::::::::::::::



    @ApiOperation(value = "Toutes les  Designations ")
    @PostMapping("/Designation/GetAll")
    public ResponseEntity<Object> TouteslesDesignations(@RequestParam(value = "user") String userVenant) {
        try {

            // Histroique
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Gdesignation = droitService.GetLibelle("Read Designation");

            if (user.getRole().getDroits().contains(Gdesignation)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            "" + user.getPrenom() + " " + user.getNom() + " a affiche Toutes les designations ");
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            designationService.GetAll());
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
    }}
