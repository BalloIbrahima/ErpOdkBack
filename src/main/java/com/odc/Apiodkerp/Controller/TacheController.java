package com.odc.Apiodkerp.Controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Models.*;
import com.odc.Apiodkerp.Service.*;
import com.odc.Apiodkerp.ServiceImplementation.EmailDetailsInterf;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/tache")
@Api(value = "tache", description = "Les fonctionnalités liées aux taches")
@CrossOrigin
public class TacheController {
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private ActiviteService activiteService;

    @Autowired
    private EntiteService entiteService;

    @Autowired
    private DroitService droitService;
    @Autowired
    private com.odc.Apiodkerp.Service.RoleService roleService;
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
    private EmailDetailsInterf email;

    @Autowired
    private TacheService tacheService;

    @Autowired
    private StatusService statusService;


    @ApiOperation(value = "Creer tache")
    @PostMapping("/creerTache")
    public ResponseEntity<Object> CREERtACHE( @RequestParam(value = "tache") String tache,
                                             @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            Tache tache1 = new JsonMapper().readValue(tache, Tache.class);

            // Historique
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Du = droitService.GetLibelle("Create Tache");

            if (user != null) {
                if (user.getRole().getDroits().contains(Du)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a cree une tache du nom de " + tache1.getDesignation());
                        historiqueService.Create(historique);
                       

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK,  tacheService.creer(tache1));

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


    ///::::::::::::::::::::::::Update taches ::::::::::::::::

    @ApiOperation(value = "Update tache")
    @PostMapping("/mofifierTache")
    public ResponseEntity<Object> modifierTache(@PathVariable long id, @RequestParam(value = "tache") String tache,
                                              @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            Tache tache1 = new JsonMapper().readValue(tache, Tache.class);

            // Historique
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Du = droitService.GetLibelle("Update Tache");

            if (user != null) {
                if (user.getRole().getDroits().contains(Du)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a modifier une tache du nom de " + tache1.getDesignation());
                        historiqueService.Create(historique);
                        

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, tacheService.update(tache1));

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


    //::::::::::::::::::::::::Supprimer tache ::::::::::::::::::::::
    @ApiOperation(value = "Supprimer tache")
    @PostMapping("/supprimerTache/{id}")
    public ResponseEntity<Object> SupprimerTache( @PathVariable long id,
                                              @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);


            // Historique
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Du = droitService.GetLibelle("Delete Tache");

            if (user != null) {
                if (user.getRole().getDroits().contains(Du)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a supprime une tache " );
                        historiqueService.Create(historique);
                        tacheService.delete(id);
                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, "Supression effectué !");

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


    // ;::::::::::::::::::::::::::::Afficher toutes les taches::::::::::::::::::::
    @ApiOperation(value = "afficher toutes  tache")
    @PostMapping("/AfficherToutesTaches")
    public ResponseEntity<Object> AfficherToutesTaches(
                                              @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            // Historique
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Du = droitService.GetLibelle("Read Tache");

            if (user != null) {
                if (user.getRole().getDroits().contains(Du)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a recuperer toutes les taches  ");
                        historiqueService.Create(historique);
                        

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, tacheService.getAll());


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

    //:::::::::::::::::::: Tache en fonctionnde l'activite ::::::::::::::::

    @ApiOperation(value = "Tache en fonctionnde l'activite")
    @PostMapping("/AfficherToutesTaches/{idactivite}")
    public ResponseEntity<Object> TacheEnFonctionActivite(@PathVariable long idactivite,
            @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            // Historique
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Activite act   = activiteService.GetById(idactivite);

            List<Tache> tache = tacheService.getAll();
            Droit Du = droitService.GetLibelle("Create Utilisateur");
            List<Tache> tacheretout = new ArrayList<>();


            if (user != null) {
                if (user.getRole().getDroits().contains(Du)) {

                    for (Tache t : tache) {
                        if (t.getActivite() == act) {
                            tacheretout.add(t);
                        }}
                    try {

                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a affiche toutes les taches en fonctiond e l'activite  ");
                        historiqueService.Create(historique);
                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, tacheretout);

                        //
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

                   // return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);
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

    //all status
    @ApiOperation(value = "Toutes les status")
    @PostMapping("/status/all")
    public ResponseEntity<Object> allStatuts(@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            // Historique
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            //Activite act   = activiteService.GetById(idactivite);

           

            if (user != null) {

                 
                try {

                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                            + " a affiche tous les staus");
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, statusService.getAll());

                    //
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }

                   // return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);
                

              
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }
}
