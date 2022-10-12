package com.odc.Apiodkerp.Controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Configuration.SaveImage;
import com.odc.Apiodkerp.Models.Droit;
import com.odc.Apiodkerp.Models.Entite;
import com.odc.Apiodkerp.Models.Historique;
import com.odc.Apiodkerp.Models.Utilisateur;
import com.odc.Apiodkerp.Service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/entite")
@Api(value = "entite", description = "Les fonctionnalités liées à une entite")
@CrossOrigin
public class EntiteController {
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

    // ENTITE-------------------------------------------------------------->
    @ApiOperation(value = "Creer un entite.")
    @PostMapping("/create/entite")
    public ResponseEntity<Object> createEntite(@RequestParam(value = "entite") String enti,
            @RequestParam(value = "file", required = true) MultipartFile file,
            @RequestParam(value = "user") String userVenant) {
                Entite entite=null;
                Utilisateur user=null;
            try {

            entite = new JsonMapper().readValue(enti, Entite.class);
            if (file != null) {
                System.out.println("ggggg");
                entite.setImage(SaveImage.save("activite", file, entite.getLibelleentite()));
            }
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            
             user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
                
            List<Entite> entit = entiteService.GetAll();
            Boolean IsGerant=false;
            Droit createrole = droitService.GetLibelle("Create Entite");

            if (user.getRole().getDroits().contains(createrole)) {

                for (Entite en : entit) {
                    if (entite.getGerant() == en.getGerant()) {
                        IsGerant=true;
                        break;
                    }
                }

                if(IsGerant==true){
                    return ResponseMessage.generateResponse("error", HttpStatus.OK,
                                "cette personne est deja gerant d'une entite");
                }else{
                    try {
                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique
                                .setDescription(
                                        "" + user.getPrenom() + " " + user.getNom()
                                                + " a cree  une nouvelle entite ");
                        historiqueService.Create(historique);
                        Entite NewEntite = entiteService.Create(entite);
    
                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, NewEntite);
    
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());
    
                    }
                }
                
                

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Modifier un entite")
    @PostMapping("/update/entite/{id}")
    public ResponseEntity<Object> updateEntite(@PathVariable("id") Long id, @RequestParam(value = "entite") String enti,
            @RequestParam(value = "user") String userVenant) {
        try {
            Entite entite = new JsonMapper().readValue(enti, Entite.class);
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit updatentite = droitService.GetLibelle("Update Entite");

            if (user.getRole().getDroits().contains(updatentite)) {
                Entite UpdateEntite = entiteService.Update(id, entite);
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, UpdateEntite);
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Supprimer une entite")
    @PostMapping("/delete/entite/{id}")
    public ResponseEntity<Object> DeleteEntite(@PathVariable Long id, @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Entite entite =entiteService.GetById(id);
            Droit deleterole = droitService.GetLibelle("Delete Entite");
               entite.setIsdelete(true);
            if (user.getRole().getDroits().contains(deleterole)) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,  entiteService.Update(id,entite));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Affichager tout les entités")
    @PostMapping("/getAll/entite")
    public ResponseEntity<Object> GetAllEntite(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit getentite = droitService.GetLibelle("Read Entite");

            if (user.getRole().getDroits().contains(getentite)) {
                List<Entite> getAllEntite = entiteService.GetAll();
              List<Entite> enti =  entiteService.GetAll();
              List<Entite> en = new ArrayList<>();
                for(Entite e:getAllEntite){
                    try {
                        if(e.getIsdelete().equals(false)){
                            en.add(e);
                        }
                    }catch (Exception exception){

                    }

                }
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, en);
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    @ApiOperation(value = "Affichager une entite")
    @PostMapping("/get/entite/{id}")
    public ResponseEntity<Object> GetIdEntite(@PathVariable("id") Long id,
            @RequestParam(value = "user") String userVenant) {
        // @RequestParam(value = "entite") String enti
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit getentite = droitService.GetLibelle("Read Entite");

            if (user.getRole().getDroits().contains(getentite)) {
                Entite idEntite = entiteService.GetById(id);
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, idEntite);

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // :::::::::::::::::::::::::::::::::::total entite
    // ::::::::::::::::::::::::::::::::::::::
    @ApiOperation(value = "totalentite")
    @PostMapping("/totalentite")
    public ResponseEntity<Object> TotalEntite(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit Rentite = droitService.GetLibelle("Lire une entitee");

            if (users != null) {
                if (users.getRole().getDroits().contains(Rentite)) {

                    try {

                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(
                                "" + users.getPrenom() + " " + users.getNom() + " a affiche toutes les entites ");
                        historiqueService.Create(historique);
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("iciiii", HttpStatus.OK, e.getMessage());

                    }
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, utilisateurService.TotalEntite());

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");
                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }
}
