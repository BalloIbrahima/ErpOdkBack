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

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit cSalle = droitService.GetLibelle("Create Salle");

            // Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(login,
            // password);
            System.out.println(utilisateur);
            if (user.getRole().getDroits().contains(cSalle)) {
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                            + " a cree une salle du nom de " + salle.getLibelle());
                    historiqueService.Create(historique);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }
                // salle.setUtilisateur(utilisateur);
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
    @PostMapping("getSalle/{id}")
    public ResponseEntity<Object> getSalle(@PathVariable("id") Long id,
            @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
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
                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, salleService.read(id));

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

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
    @PostMapping("/modifiersalle/{id}")
    public ResponseEntity<Object> modifier(@PathVariable long id, @RequestParam(value = "user") String userVenant,
            @RequestParam(value = "salle") String sal) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            Salle salle = new JsonMapper().readValue(sal, Salle.class);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

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
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }
        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // @ApiOperation(value = "Lien pour suprimer une salle")
    // @PostMapping("/supprimersalle/{id}")
    // public ResponseEntity<Object> supprimer(@PathVariable long id, @RequestParam(value = "user") String userVenant) {
    //     try {
    //         Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

    //         Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
    //                 utilisateur.getPassword());

    //         Droit DSalle = droitService.GetLibelle("Delete Salle");

    //         if (user != null) {
    //             if (user.getRole().getDroits().contains(DSalle)) {

    //                 try {
    //                     Historique historique = new Historique();
    //                     Date datehisto = new Date();
    //                     historique.setDatehistorique(datehisto);
    //                     historique.setDescription(
    //                             "" + user.getPrenom() + " " + user.getNom() + " a supprime une salle du nom de ");
    //                     historiqueService.Create(historique);
    //                 } catch (Exception e) {
    //                     // TODO: handle exception
    //                     return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

    //                 }
    //                 salleService.delete(id);
    //                 return ResponseMessage.generateResponse("ok", HttpStatus.OK, "Salle supprime avec succes !");
    //             } else {
    //                 return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");

    //             }
    //         } else {
    //             return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

    //         }
    //     } catch (Exception e) {
    //         return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
    //     }
    // }

    @ApiOperation(value = "Lien pour lier une salle à une activite")
    @PostMapping("/attribuersalle/{idsalle}/{idactivite}")
    public ResponseEntity<Object> attribuerSalle(@PathVariable long idsalle, @PathVariable long idactivite,
            @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Ra = droitService.GetLibelle("Read Activite");

            if (user != null) {
                if (user.getRole().getDroits().contains(Ra)) {

                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + user.getPrenom() + " " + user.getNom()
                                + " a attribue  une salle du nom de  " + salleService.getByIdsalle(idsalle)
                                + " à l'activte " + activiteService.GetById(idactivite));
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            activiteService.attribuerSalle(idsalle, idactivite));
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");

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
    @PostMapping("/salle/all/{login}/{password}")
    public ResponseEntity<Object> getAll(@PathVariable("login") String login,
            @PathVariable("password") String password) {
        try {
            Utilisateur users = utilisateurService.trouverParLoginAndPass(login, password);
            Droit Rs = droitService.GetLibelle("Read Salle");

            if (users.getRole().getDroits().contains(Rs)) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        salleService.getAll());
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // ::::::::::::::Comparaison date pour salle disponible
    // ::::::::::::::::::::::::::::::
    @ApiOperation(value = "Comparaison date pour salle disponible")
    @PostMapping("SalleDispo/{date1}/{date2}/{login}/{password}")
    public ResponseEntity<Object> SalleDispoDate(@PathVariable String login, @PathVariable String password,
            @PathVariable Date date1, @PathVariable Date date2) {
        try {
            List<Activite> activites = activiteService.FindAllAct();
            List<Salle> salle = new ArrayList<>();
            Utilisateur users = utilisateurService.trouverParLoginAndPass(login, password);
            Droit RSalle = droitService.GetLibelle("Read Salle");

            if (users != null) {
                if (users.getRole().getDroits().contains(RSalle)) {
                    for (Activite act : activites) {
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
                        return ResponseMessage.generateResponse("error", HttpStatus.OK, salle);

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");
                }
            }

            return ResponseMessage.generateResponse("error", HttpStatus.OK, activiteService.Termine());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // :::::::::::::::Salle disponible :::::::::::::::::::::::::::::::
    @ApiOperation(value = " salle disponible")
    @PostMapping("/SalleDisponible")
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
                    for (Activite act : activites) {
                        try {
                            if (act.getDateDebut().after(date) && act.getDateFin().after(date) && act.getSalle()!=null) {
                                // Historique
    
                                salle.add(act.getSalle());
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        
                    }

                    List<Salle> salles = salleService.getAll();
                    for (Salle s : salles) {
                        if (s.getActivite().size() == 0) {
                            salle.add(s);
                        }
                    }
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + users.getPrenom() + " " + users.getNom()
                                + " a afficher  salle disponible" + date);
                        historiqueService.Create(historique);
                        return ResponseMessage.generateResponse("error", HttpStatus.OK, salle);

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");
                }
            }

            return ResponseMessage.generateResponse("error", HttpStatus.OK, activiteService.Termine());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    @ApiOperation(value = "l'ensemble des salles indisponibles")
    @PostMapping("/SalleInDisponible")
    public ResponseEntity<Object> InsalleDispo(@RequestParam(value = "user") String userVenant) {
        try {
            List<Activite> acts = activiteService.FindAllAct();
            List<Salle> salle = new ArrayList<>();

            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());

            Date today = new Date();

            Droit RSalle = droitService.GetLibelle("Read Salle");

            if (users != null) {
                if (users.getRole().getDroits().contains(RSalle)) {
                    for (Activite act : acts) {
                        try {
                            if (act.getDateDebut().after(today) && act.getDateFin().before(today)
                                    || act.getDateDebut().before(today) && act.getDateFin().after(today) && act.getSalle()!=null) {

                                //salle.add(act.getSalle());
                                // Historique

                                salle.add(act.getSalle());
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                       
                    }

                    
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription("" + users.getPrenom() + " " + users.getNom()
                                + " a afficher  salle disponible ");
                        historiqueService.Create(historique);

                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, salle);

                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");
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
    @PostMapping("/getSalles/indisponible/{login}/{password}")
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
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // :::::::::::::::::::::::Suprimer salle
    @ApiOperation(value = "Suprimer salle")
    @PostMapping("/suprime/{idSalle}")
    public ResponseEntity<Object> SuprimerSalle(@PathVariable("idSalle") Long idSalle,@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Salle salle=salleService.getByIdsalle(idSalle);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit Rsalle = droitService.GetLibelle("Delete Salle");

            if (user.getRole().getDroits().contains(Rsalle)) {
                if(salle!=null){

                    if(salle.getActivite().size()==0){
                        try {
                            Historique historique = new Historique();
                            Date datehisto = new Date();
                            historique.setDatehistorique(datehisto);
                            historique.setDescription(
                                    "" + user.getPrenom() + " " + user.getNom() + " a suprime  une salle"+idSalle);
                            historiqueService.Create(historique);
        
                            salleService.delete(idSalle);
                            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                                    "Salle suprime");
                        } catch (Exception e) {
                            // TODO: handle exception
                            return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());
        
                        }
                    }else{
                        // for(activite a:salle.getActivite()){
                        //     if()
                        // }
                        return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cette salle est lié a des activites");

                    }
                    
                }else{
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cette salle n'existe pas !");

                }
                

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }


    // :::::::::::::::::::::::Les activites sans salle
    @ApiOperation(value = "Les activites sans salle")
    @PostMapping("/activitesanssalle")
    public ResponseEntity<Object> ActiviteSansSalle(@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            //Salle salle=salleService.getByIdsalle(idSalle);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit Rsalle = droitService.GetLibelle("Read Activite");

            if (user.getRole().getDroits().contains(Rsalle)) {
              
                List<Activite> allActivite=activiteService.GetAll();
                List<Activite> ListAretourne=new ArrayList<>();

                for(Activite a:allActivite){
                    if(a.getSalle()==null){
                        ListAretourne.add(a);
                    }
                }
                try {
                    Historique historique = new Historique();
                    Date datehisto = new Date();
                    historique.setDatehistorique(datehisto);
                    historique.setDescription(
                            "" + user.getPrenom() + " " + user.getNom() + " a recuperer les activites sans salles.");
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            ListAretourne);
                } catch (Exception e) {
                    // TODO: handle exception
                    return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                }
            }else{
                // for(activite a:salle.getActivite()){
                //     if()
                // }
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cette salle est lié a des activites");

            }
                    
                
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

}
