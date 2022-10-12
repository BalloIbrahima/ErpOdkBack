package com.odc.Apiodkerp.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.lang.model.element.Element;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.odc.Apiodkerp.Configuration.ResponseMessage;

import com.odc.Apiodkerp.Models.Utilisateur;
import io.swagger.annotations.ApiOperation;

import com.odc.Apiodkerp.Models.PostulantTire;
import com.odc.Apiodkerp.Models.Role;
import com.odc.Apiodkerp.Models.Tache;
import com.odc.Apiodkerp.Repository.PostulantTrieRepository;
import com.odc.Apiodkerp.Service.PostulantTrieService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.odc.Apiodkerp.Configuration.ExcelGenerator;
import com.odc.Apiodkerp.Configuration.ExcelImport;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.AouP;
import com.odc.Apiodkerp.Models.Droit;
import com.odc.Apiodkerp.Models.Historique;
import com.odc.Apiodkerp.Models.IntervenantExterne;
import com.odc.Apiodkerp.Models.ListePostulant;
import com.odc.Apiodkerp.Models.Postulant;

import com.odc.Apiodkerp.Models.Tirage;

import com.odc.Apiodkerp.Service.ActiviteService;
import com.odc.Apiodkerp.Service.AouPService;
import com.odc.Apiodkerp.Service.DroitService;
import com.odc.Apiodkerp.Service.EntiteService;
import com.odc.Apiodkerp.Service.EtatService;
import com.odc.Apiodkerp.Service.HistoriqueService;
import com.odc.Apiodkerp.Service.IntervenantExterneService;
import com.odc.Apiodkerp.Service.ListePostulantService;
import com.odc.Apiodkerp.Service.NotificationService;
import com.odc.Apiodkerp.Service.PostulantService;

import com.odc.Apiodkerp.Service.PresenceService;
import com.odc.Apiodkerp.Service.RoleService;
import com.odc.Apiodkerp.Service.SalleService;
import com.odc.Apiodkerp.Service.StatusService;
import com.odc.Apiodkerp.Service.TacheService;
import com.odc.Apiodkerp.Service.TirageService;
import com.odc.Apiodkerp.Service.TypeActiviteService;
import com.odc.Apiodkerp.Service.UtilisateurService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/responsable")
@Api(value = "responsable", description = "Les fonctionnalités liées à un responsable")
@CrossOrigin
public class ResponsableController {

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

    @Autowired
    private TacheService tacheService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private IntervenantExterneService intervenantExterneService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AouPService aouPService;

    @Autowired
    private HistoriqueService historiqueService;

    @Autowired
    private DroitService droitService;

    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    // la methode pour importer une liste de postulant
    @ApiOperation(value = "la methode pour importer une liste de postulant.")
    @PostMapping("/listpostulant/new/{libelleliste}/{idActivite}")
    public ResponseEntity<Object> ImportListePostulant(@PathVariable("libelleliste") String libelleliste,
            @PathVariable("idActivite") Long idActivite,
            @RequestParam(value = "user") String userVenant,
            @RequestParam("file") MultipartFile file) {

        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur Simpleutilisateur = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Activite activite = activiteService.GetById(idActivite);
            Droit createListe = droitService.GetLibelle("Create ListePostulant");

            if (Simpleutilisateur != null) {
                if (Simpleutilisateur.getRole().getDroits().contains(createListe)) {
                    ListePostulant liste = listePostulantService.retrouveParLibelle(libelleliste);
                    if (liste == null) {
                        if (ExcelImport.verifier(file)) {
                            ListePostulant listePostulant = new ListePostulant();

                            listePostulant.setLibelle(libelleliste);
                            listePostulant.setDateimport(new Date());
                            listePostulant.setActivite(activite);

                            ListePostulant listSaved = listePostulantService.creer(listePostulant);

                            List<Postulant> postulants = ExcelImport.postulantsExcel(file);
                            // insertion des postulants recuperes à partir du fichier excel
                            for (Postulant p : postulants) {

                                if (postulantService.GetByEmail(p.getEmail()) == null
                                        & p.getNom() != null & p.getPrenom() != null) {
                                    p.getListePostulants().add(listSaved);
                                    Postulant pc = postulantService.creer(p);

                                    // p.getListePostulants().add(listePostulant);
                                    // listePostulant.getPostulants().add(pc);

                                } else if (p.getEmail() != null & p.getNom() != null & p.getPrenom() != null) {
                                    Postulant pc = postulantService.GetByEmail(p.getEmail());
                                    pc.getListePostulants().add(listSaved);
                                    postulantService.creer(pc);
                                    // listePostulant.getPostulants().add(pc);

                                }

                            }

                            Historique historique = new Historique();
                            historique.setDatehistorique(new Date());
                            historique.setDescription(Simpleutilisateur.getPrenom() + " " + Simpleutilisateur.getNom()
                                    + " a importer une liste de postulant.");

                            historiqueService.Create(historique);
                            return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                                    listSaved);

                        } else {
                            return ResponseMessage.generateResponse("error", HttpStatus.OK,
                                    "Veuiller fournir un fichier Excel valide!");
                        }
                    } else {
                        // Il existe une liste avec la même libelle
                        return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cette lise existe deja");

                    }
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK,
                        "Cet utilisateur n'existe pas !");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    // la methode pour importer une liste de participant
    @ApiOperation(value = "la methode pour importer une liste de participant.")
    @PostMapping("/listparticipant/new/{libelleliste}/{idactivite}")
    public ResponseEntity<Object> ImportListeParticipant(@RequestParam("file") MultipartFile file,
            @PathVariable("libelleliste") String libelleliste,
            @PathVariable("idactivite") Long idactivite, @RequestParam(value = "user") String userVenant) {

        try {
            Activite activite = activiteService.GetById(idactivite);

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur Simpleutilisateur = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit createAoup = droitService.GetLibelle("Create AouP");

            if (Simpleutilisateur != null) {

                if (Simpleutilisateur.getRole().getDroits().contains(createAoup)) {
                    if (activite != null) {
                        ListePostulant liste = listePostulantService.retrouveParLibelle(libelleliste);
                        if (liste == null) {
                            if (ExcelImport.verifier(file)) {

                                ///
                                ListePostulant listePostulant = new ListePostulant();

                                listePostulant.setLibelle(libelleliste);
                                listePostulant.setDateimport(new Date());
                                listePostulant.setActivite(activite);

                                ListePostulant listSaved = listePostulantService.creer(listePostulant);

                                ///
                                List<Postulant> postulants = ExcelImport.postulantsExcel(file);
                                // insertion des postulants recuperes à partir du fichier excel
                                for (Postulant p : postulants) {

                                    if (postulantService.GetByEmail(p.getEmail()) == null
                                            & p.getNom() != null & p.getPrenom() != null) {
                                        Postulant pc1 = postulantService.creer(p);
                                        pc1.getListePostulants().add(listSaved);
                                        Postulant pc = postulantService.creer(p);

                                        AouP aprenants = new AouP();
                                        aprenants.setActivite(activite);
                                        aprenants.setPostulant(pc);
                                        aprenants.setTirage(false);
                                        aouPService.Create(aprenants);
                                        // tirageService.ajouterParticipant(pc, idTirage);
                                        // p.getListePostulants().add(listePostulant);
                                        // listePostulant.getPostulants().add(pc);

                                    } else if (p.getEmail() != null & p.getNom() != null & p.getPrenom() != null) {

                                        Postulant pc1 = postulantService.GetByEmail(p.getEmail());
                                        pc1.getListePostulants().add(listSaved);
                                        Postulant pc = postulantService.creer(pc1);

                                        AouP aprenants = new AouP();
                                        aprenants.setActivite(activite);
                                        aprenants.setPostulant(pc);
                                        aprenants.setTirage(false);
                                        aouPService.Create(aprenants);
                                        // tirageService.ajouterParticipant(pc, idTirage);
                                    }

                                }

                                Historique historique = new Historique();
                                historique.setDatehistorique(new Date());
                                historique
                                        .setDescription(Simpleutilisateur.getPrenom() + " " + Simpleutilisateur.getNom()
                                                + " a importer une liste de participant.");

                                historiqueService.Create(historique);

                                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                                        null);

                            } else {
                                return ResponseMessage.generateResponse("error", HttpStatus.OK,
                                        "Veuiller fournir un fichier Excel valide !");
                            }
                        } else {
                            // Il existe une liste avec la même libelle
                            return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cette lise existe deja");

                        }

                    } else {
                        // Il existe une liste avec la même libelle
                        return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cette activite existe deja");

                    }
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK,
                        "Cet utilisateur n'existe pas !");
            }
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // methode pour exporter des postulants tirés
    @ApiOperation(value = "methode pour exporter des postulants tirés.")
    @PostMapping("/export/{idtirage}")
    public ResponseEntity<Object> exporterTirage(@PathVariable("idtirage") Long idtirage,
            @RequestParam(value = "user") String userVenant,
            HttpServletResponse response) {
        response.setContentType("application/octet-stream");

        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur Simpleutilisateur = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Tirage tirage = tirageService.getById(idtirage);
            List<Postulant> postulantsListe = new ArrayList<>();

            Droit ReadAoup = droitService.GetLibelle("Read AouP");

            if (Simpleutilisateur != null) {
                if (Simpleutilisateur.getRole().getDroits().contains(ReadAoup)) {
                    for (PostulantTire p : tirage.getPostulanttires()) {

                        postulantsListe.add(p.getPostulant());
                    }

                    ExcelGenerator generator = new ExcelGenerator(postulantsListe);
                    generator.genererFichierExcel(response);

                    //
                    Historique historique = new Historique();
                    historique.setDatehistorique(new Date());
                    historique.setDescription(Simpleutilisateur.getPrenom() + " " + Simpleutilisateur.getNom()
                            + " a exporter une liste de postulants tires.");

                    historiqueService.Create(historique);

                    //
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, postulantsListe);
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    // la methode pour effectuer un tirage
    @ApiOperation(value = "La methode Pour effectuer un tirage.")
    @PostMapping("/tirage/new/{libelleliste}/{nombre}/{libelleTirage}")
    public ResponseEntity<Object> DoTirage(@PathVariable("libelleliste") String libelleliste,
            @PathVariable("nombre") Long nombre,
            @RequestParam(value = "user") String userVenant,
            @PathVariable("libelleTirage") String libelleTirage) {

        try {
            Tirage exist = tirageService.findByLibelle(libelleTirage);
            if (exist == null) {
                Droit createTirage = droitService.GetLibelle("Create Tirage");

                Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

                Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                        utilisateurs.getPassword());

                ListePostulant listePostulant = listePostulantService.retrouveParLibelle(libelleliste);

                Activite activite = listePostulant.getActivite();
                //activiteService.GetById(idactivite);

                Tirage tirage = new Tirage();
                tirage.setLibelle(libelleTirage);
                tirage.setListepostulant(listePostulant);
                
                tirage.setActivite(activite);
                tirage.setValider(false);
                tirage.setUtilisateur(utilisateur);

                System.out.println(libelleTirage);
                System.out.println(nombre);
                System.out.println(libelleliste);

                System.out.println(listePostulant.getPostulants());

                if (nombre <= listePostulant.getPostulants().size()) {
                    Tirage postulanttires = tirageService.creer(tirage, listePostulant.getPostulants(),
                            nombre);

                    if (utilisateur != null) {
                        if (utilisateur.getRole().getDroits().contains(createTirage)) {
                            try {

                                //
                                Historique historique = new Historique();
                                historique.setDatehistorique(new Date());
                                historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                                        + " a effectuer un tirage.");
                                historiqueService.Create(historique);

                                //
                                return ResponseMessage.generateResponse("ok", HttpStatus.OK, postulanttires);

                            } catch (Exception e) {
                                // TODO: handle exception
                                return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

                            }
                        } else {
                            return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise !");

                        }
                    } else {
                        return ResponseMessage.generateResponse("error", HttpStatus.OK,
                                "Cet utilisateur n'existe pas !");

                    }

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Nombre de postulants insufusant !");

                }

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Ce tirage existe deja !");
            }
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    // la methode pour retourner l'ensemble des postulants tires par id du tirage
    @ApiOperation(value = "la methode pour retourner l'ensemble des postulants tires par id du tirage.")
    @PostMapping("/tirage/postulants/{idTirage}")
    public ResponseEntity<Object> DoTirage(@PathVariable("idTirage") Long idTirage,
            @RequestParam(value = "user") String userVenant) {

        try {
            Tirage exist = tirageService.getById(idTirage);
            if (exist != null) {
                Droit ReadTirage = droitService.GetLibelle("Read Tirage");

                Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

                Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                        utilisateurs.getPassword());

              


                if (utilisateur != null) {
                    if (utilisateur.getRole().getDroits().contains(ReadTirage)) {
                        try {

                            //
                            Historique historique = new Historique();
                            historique.setDatehistorique(new Date());
                            historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                                    + " a afficher l'ensemble des postulants tires du tirage."+idTirage);
                            historiqueService.Create(historique);

                            //
                            return ResponseMessage.generateResponse("ok", HttpStatus.OK, exist.getPostulanttires());

                        } catch (Exception e) {
                            // TODO: handle exception
                            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

                        }
                    } else {
                        return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise !");

                    }
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK,
                            "Cet utilisateur n'existe pas !");

                }

               
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Ce tirage n'existe pas !");
            }
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }


    // ---------------------------------Postulant
    // Tire---------------------------------------//
    // Creation de participant
    @ApiOperation(value = "Ajouter participant")
    @PostMapping("/create/participant/{idactivite}")
    public ResponseEntity<Object> createPostulantTire(@RequestBody Postulant participant,
            @PathVariable("idactivite") Long idactivite, @RequestParam(value = "user") String userVenant) {

        try {
            Activite activite = activiteService.GetById(idactivite);

            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());
            Droit addAouP = droitService.GetLibelle("Create AouP");

            if (utilisateur != null) {
                if (utilisateur.getRole().getDroits().contains(addAouP)) {
                    AouP aouP = new AouP();
                    aouP.setActivite(activite);
                    aouP.setPostulant(participant);
                    aouP.setTirage(false);

                    //
                    Historique historique = new Historique();
                    historique.setDatehistorique(new Date());
                    historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                            + " a a joute le participant: " + aouP.getPostulant().getEmail()
                            + " à l'activite avec l'ID: "
                            + activite.getNom());
                    historiqueService.Create(historique);

                    //

                    // tirageService.ajouterParticipant(participant, idtirage);
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise !");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    // Afficher de postulant tiré
    @ApiOperation(value = "Afficher participant par son id")
    @PostMapping("/read/{id}")
    public ResponseEntity<Object> readPostulantTire(@PathVariable long id,
            @RequestParam(value = "user") String userVenant) {
        try {
            Droit readAouP = droitService.GetLibelle("Read AouP");

            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());
            PostulantTire post = postulantTrieService.read(id);

            if (utilisateur != null) {
                if (utilisateur.getRole().getDroits().contains(readAouP)) {
                    //
                    Historique historique = new Historique();
                    historique.setDatehistorique(new Date());
                    historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                            + " a affiche le participant: " + post.getPostulant().getEmail());

                    historiqueService.Create(historique);

                    //
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, post);
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise !");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }

        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // Modifier de postulant tiré par son id
    @ApiOperation(value = "modifier participant par son id")
    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updatePostulantTire(@RequestBody Postulant postulant, @PathVariable long id,
            @RequestParam(value = "user") String userVenant) {
        try {
            Postulant post = postulantService.update(id, postulant);
            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());
            Droit updateAouP = droitService.GetLibelle("Update AouP");

            if (utilisateur != null) {
                if (utilisateur.getRole().getDroits().contains(updateAouP)) {
                    Historique historique = new Historique();
                    historique.setDatehistorique(new Date());
                    historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                            + " a modifie le participant: " + post.getEmail());

                    historiqueService.Create(historique);

                    //

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, post);
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise !");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }
            //

        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    // Supprimer Postulant tiré par son id
    @ApiOperation(value = "Supprimer participant par son id")
    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> deletePostulantTire(@PathVariable long id,
            @RequestParam(value = "user") String userVenant) {
        try {
            postulantTrieService.delete(id);
            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());
            Droit deleteAouP = droitService.GetLibelle("Delete AouP");

            if (utilisateur != null) {
                if (utilisateur.getRole().getDroits().contains(deleteAouP)) {
                    //
                    Historique historique = new Historique();
                    historique.setDatehistorique(new Date());
                    historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                            + " a suprime le postulant tire avec l'id: " + id);

                    historiqueService.Create(historique);

                    //
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise !");
                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    // Afficher tous les postulants
    @ApiOperation(value = "Afficher tous les participants")
    @PostMapping("/participants/All")
    public ResponseEntity<Object> getAllPostulantTire(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());
            Droit readAouP = droitService.GetLibelle("Read AouP");

            if (utilisateur != null) {
                if (utilisateur.getRole().getDroits().contains(readAouP)) {
                    //
                    Historique historique = new Historique();
                    historique.setDatehistorique(new Date());
                    historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                            + " a affiche lensemble des participants.");

                    historiqueService.Create(historique);

                    //
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, aouPService.GetAll());

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise !");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    //::::::::::Filte des participants:::::::::::
    @ApiOperation(value="filtrer des participants")
    @PostMapping("/participants/filtre/{typeactivite}/{datedebut}/{datefin}")
    public ResponseEntity<Object> filtrerPostulant(@RequestParam (value="user") String user, @PathVariable String typeactivite, @PathVariable String datedebut, @PathVariable String datefin){
        try{
            Utilisateur utilisateur = new JsonMapper().readValue(user , Utilisateur.class);
            Utilisateur utilisateur1 = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit readAouP = droitService.GetLibelle("Read AouP");
            if (utilisateur1!=null){
                if (utilisateur1.getRole().getDroits().contains(readAouP)){
                    Historique historique = new Historique();
                    historique.setDatehistorique(new Date());
                    historique.setDescription(utilisateur1.getPrenom() + " " + utilisateur1.getNom() + "a filtre des participants");
                    historiqueService.Create(historique);

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, aouPService.filtrerParticipant(typeactivite, datedebut, datefin));
                } else return ResponseMessage.generateResponse("error",HttpStatus.OK,"a te taimai !");
            } return ResponseMessage.generateResponse("error", HttpStatus.OK,"Cet utilisateur n'existe pas !");
        } catch (JsonMappingException e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK,e.getMessage());
        } catch (JsonProcessingException e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK,e.getMessage());
        }
    }

    // :::::::::::::::total postulant: :::::::::::::::::::::

    // ::::::::::::::::Tache::::::::::::::::::::::::::::::::::::::::::
    // creer une tache
    @ApiOperation(value = "creer une tache")
    @PostMapping("/tache/creer")
    public ResponseEntity<Object> createTache(@RequestParam(value = "user") String userVenant,
            @RequestParam(value = "tache") String tach) {
        try {
            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());
            Tache tache = new JsonMapper().readValue(tach, Tache.class);
            Droit Ctache = droitService.GetLibelle("Create tache");

            if (utilisateur != null) {
                if (utilisateur.getRole().getDroits().contains(Ctache)) {
                    //
                    Historique historique = new Historique();
                    historique.setDatehistorique(new Date());
                    historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                            + " a cree une tache");

                    historiqueService.Create(historique);

                    //
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, tacheService.creer(tache));

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise !");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    // Modifier une tache
    @ApiOperation(value = "Mettre à jour une tache")
    @PostMapping("/tache/update/")
    public ResponseEntity<Object> updateTache(@RequestParam(value = "user") String userVenant,
            @RequestParam(value = "tache") String tach) {
        try {

            Tache tache = new JsonMapper().readValue(tach, Tache.class);

            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());
            Droit Crtache = droitService.GetLibelle("Ctache");

            if (utilisateur != null) {
                if (utilisateur.getRole().getDroits().contains(Crtache)) {
                    //
                    Historique historique = new Historique();
                    historique.setDatehistorique(new Date());
                    historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                            + " a cree une tache");

                    historiqueService.Create(historique);

                    //
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, tacheService.creer(tache));

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise !");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    // ::::::::::::Afficher tous les taches
    @ApiOperation(value = "Afficher tous les taches")
    @PostMapping("/taches/All")
    public ResponseEntity<Object> getAllTache(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());
            Droit readTache = droitService.GetLibelle("Read Tache");

            if (utilisateur != null) {
                if (utilisateur.getRole().getDroits().contains(readTache)) {
                    //
                    Historique historique = new Historique();
                    historique.setDatehistorique(new Date());
                    historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                            + " a affiche l'ensemble des taches.");

                    historiqueService.Create(historique);

                    //
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, tacheService.getAll());

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise !");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    // ::::::::::::Afficher tous les taches en fonction d'une d'une activite
    @ApiOperation(value = "Afficher tous les taches en fonction d'une activite")
    @PostMapping("/taches/{idactivite}")
    public ResponseEntity<Object> getAllTacheForActivite(@RequestParam(value = "user") String userVenant,
            @PathVariable("idactivite") Long idactivite) {
        try {
            Activite activite = activiteService.GetById(idactivite);
            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());
            Droit readTache = droitService.GetLibelle("Read Tache");

            if (utilisateur != null) {
                if (utilisateur.getRole().getDroits().contains(readTache)) {
                    //
                    Historique historique = new Historique();
                    historique.setDatehistorique(new Date());
                    historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                            + " a affiche l'ensemble des taches.");

                    historiqueService.Create(historique);

                    //
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, activite.getTache());

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise !");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    @ApiOperation(value = "Suprimer une tache")
    @PostMapping("/taches/{idtache}")
    public ResponseEntity<Object> deleteTache(@RequestParam(value = "user") String userVenant,
            @PathVariable("idtache") Long idtache) {
        try {

            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());
            Droit deleteTache = droitService.GetLibelle("Delete Tache");

            if (utilisateur != null) {
                if (utilisateur.getRole().getDroits().contains(deleteTache)) {
                    //
                    tacheService.delete(idtache);
                    Historique historique = new Historique();
                    historique.setDatehistorique(new Date());
                    historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                            + " a affiche l'ensemble des taches.");

                    historiqueService.Create(historique);

                    //
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, "Supression effectuer avec succes");

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise !");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    // ::::::::::::::::::liste de postulant
    @ApiOperation(value = "Recuperer toutes les taches")
    @PostMapping("/taches/all")
    public ResponseEntity<Object> ToutesTaches(@RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateurs = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(utilisateurs.getLogin(),
                    utilisateurs.getPassword());
            Droit deleteTache = droitService.GetLibelle("Delete Tache");

            if (utilisateur != null) {
                if (utilisateur.getRole().getDroits().contains(deleteTache)) {
                    //
                    Historique historique = new Historique();
                    historique.setDatehistorique(new Date());
                    historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                            + " a affiche l'ensemble des taches.");

                    historiqueService.Create(historique);

                    //
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, "Supression effectuer avec succes");

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise !");

                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cet utilisateur n'existe pas !");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    @ApiOperation(value = "Valider tirage.")
    @PostMapping("/valider/tirage/{idTirage}")
    public ResponseEntity<Object> validerTirage(@PathVariable("idTirage") Long idTirage,
            @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateu = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Tirage tirage = tirageService.getById(idTirage);

            // Role role = RoleService.GetByLibelle("USER");

            Utilisateur users = utilisateurService.trouverParLoginAndPass(utilisateu.getLogin(),
                    utilisateu.getPassword());
            Droit Utirage = droitService.GetLibelle("Update Tirage");

            if (users != null) {
                if (users.getRole().getDroits().contains(Utirage)) {

                    try {
                        tirage.setValider(true);

                        Historique historique = new Historique();
                        Date datehisto = new Date();
                        historique.setDatehistorique(datehisto);
                        historique.setDescription(users.getPrenom() + " " + users.getNom()
                                + " a valider le tirage " + tirage.getId());

                        historiqueService.Create(historique);
                        for (PostulantTire pt : tirage.getPostulanttires()) {
                            AouP aoup = new AouP();
                            aoup.setPostulant(pt.getPostulant());
                            aoup.setTirage(true);
                            aoup.setActivite(tirage.getListepostulant().getActivite());

                            aouPService.Create(aoup);

                        }

                        // System.out.println(NewUser.getLogin());
                        return ResponseMessage.generateResponse("ok", HttpStatus.OK, "Tirage validé");
                    } catch (Exception e) {
                        // TODO: handle exception
                        return ResponseMessage.generateResponse("ijjciiii", HttpStatus.OK, e.getMessage());

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
}
