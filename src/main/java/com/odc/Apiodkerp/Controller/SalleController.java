package com.odc.Apiodkerp.Controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Models.*;
import com.odc.Apiodkerp.Service.*;
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
@RequestMapping("/salle")
@Api(value = "salle", description = "Les fonctionnalités liées à une salle.")
@CrossOrigin
public class SalleController {

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



    @ApiOperation(value = "Lien pour créer une salle")
    @PostMapping("/creersalle")
    public ResponseEntity<Object> creerSalle(
            @RequestParam(value = "salle") String sal,
            @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            Salle salle = new JsonMapper().readValue(sal, Salle.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),utilisateur.getPassword());

            //   Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(login, password);
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
    @GetMapping("getSalle")
    public ResponseEntity<Object> getSalle(@PathVariable("id") Long id,
                                           @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(), utilisateur.getPassword());
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
    @PutMapping("/modifiersalle/{id}")
    public ResponseEntity<Object> modifier( @PathVariable long id, @RequestParam(value = "user") String userVenant,
                                            @RequestParam(value = "salle") String sal                       ) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            Salle salle = new JsonMapper().readValue(sal, Salle.class);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(), utilisateur.getPassword());

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
    @DeleteMapping("/supprimersalle/{id}/")
    public ResponseEntity<Object> supprimer(@PathVariable long id,@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(), utilisateur.getPassword());

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
    @GetMapping("/attribuersalle/{idsalle}/{idactivite}")
    public ResponseEntity<Object> attribuerSalle(@PathVariable long idsalle, @PathVariable long idactivite,
                                                 @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(), utilisateur.getPassword());

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
    @GetMapping("SalleDisponible")
    public ResponseEntity<Object> SalleDispoDate(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);


            Date date = new Date();
            List<Activite> activites = activiteService.FindAllAct();
            List<Salle> salle = new ArrayList<>();
            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
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


    // :::::::::::::::::::::::Les salles indisponible
    @ApiOperation(value = "Les salles indisponible")
    @GetMapping("/getSalles/indisponible/{login}/{password}")
    public ResponseEntity<Object> getSallesIndispo(@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
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

}