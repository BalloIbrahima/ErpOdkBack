package com.odc.Apiodkerp.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.lang.model.element.Element;
import javax.servlet.http.HttpServletResponse;

import com.odc.Apiodkerp.Configuration.ResponseMessage;

import com.odc.Apiodkerp.Models.Utilisateur;
import io.swagger.annotations.ApiOperation;

import com.odc.Apiodkerp.Models.PostulantTire;
import com.odc.Apiodkerp.Models.Role;
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

import com.odc.Apiodkerp.Configuration.ExcelGenerator;
import com.odc.Apiodkerp.Configuration.ExcelImport;

import com.odc.Apiodkerp.Models.Activite;
import com.odc.Apiodkerp.Models.AouP;
import com.odc.Apiodkerp.Models.Historique;
import com.odc.Apiodkerp.Models.ListePostulant;
import com.odc.Apiodkerp.Models.Postulant;

import com.odc.Apiodkerp.Models.Tirage;

import com.odc.Apiodkerp.Service.ActiviteService;
import com.odc.Apiodkerp.Service.AouPService;
import com.odc.Apiodkerp.Service.EntiteService;
import com.odc.Apiodkerp.Service.EtatService;
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

    // Pour le login d'un utilisateur
    @ApiOperation(value = "Pour le login d'un utilisateur.")
    @PostMapping("/login/{login}/{password}")
    public ResponseEntity<Object> login(@PathVariable("login") String login,
            @PathVariable("password") String password) {

        try {
            Utilisateur Simpleutilisateur = utilisateurService.login(login, password);
            Role user = RoleService.GetByLibelle("RESPONSABLE");
            if (Simpleutilisateur != null) {
                if (Simpleutilisateur.getRole() == user && Simpleutilisateur.getActive() == true) {

                    Historique historique = new Historique();
                    historique.setDatehistorique(new Date());
                    historique.setDescription(Simpleutilisateur.getPrenom() + " " + Simpleutilisateur.getNom()
                            + " vient de se connecter.");
                    return ResponseMessage.generateResponse("ok", HttpStatus.OK, Simpleutilisateur);
                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK, "non autorise");
                }
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "utilisateur n'existe pas");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    // la methode pour importer une liste de postulant
    @ApiOperation(value = "la methode pour importer une liste de postulant.")
    @PostMapping("/listpostulant/new/{libelleliste}/{idUtilisateur}")
    public ResponseEntity<Object> ImportListePostulant(@PathVariable("libelleliste") String libelleliste,
            @PathVariable("idUtilisateur") Long idUtilisateur,
            @RequestParam("file") MultipartFile file) {

        try {
            Utilisateur Simpleutilisateur = utilisateurService.getById(idUtilisateur);
            ListePostulant liste = listePostulantService.retrouveParLibelle(libelleliste);
            if (liste == null) {
                if (ExcelImport.verifier(file)) {
                    ListePostulant listePostulant = new ListePostulant();

                    listePostulant.setLibelle(libelleliste);
                    listePostulant.setDateimport(new Date());

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

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    // la methode pour importer une liste de participant
    @ApiOperation(value = "la methode pour importer une liste de participant.")
    @PostMapping("/listparticipant/new/{idactivite}/{idUtilisateur}")
    public ResponseEntity<Object> ImportListeParticipant(@RequestParam("file") MultipartFile file,
            @PathVariable("idactivite") Long idactivite, @PathVariable("idUtilisateur") Long idUtilisateur) {

        try {
            Activite activite = activiteService.GetById(idactivite);

            Utilisateur Simpleutilisateur = utilisateurService.getById(idUtilisateur);
            if (activite != null) {
                if (ExcelImport.verifier(file)) {

                    List<Postulant> postulants = ExcelImport.postulantsExcel(file);
                    // insertion des postulants recuperes à partir du fichier excel
                    for (Postulant p : postulants) {

                        if (postulantService.GetByEmail(p.getEmail()) == null
                                & p.getNom() != null & p.getPrenom() != null) {
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

                            Postulant pc = postulantService.GetByEmail(p.getEmail());

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
                    historique.setDescription(Simpleutilisateur.getPrenom() + " " + Simpleutilisateur.getNom()
                            + " a importer une liste de participant.");

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            null);

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK,
                            "Veuiller fournir un fichier Excel valide !");
                }
            } else {
                // Il existe une liste avec la même libelle
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cette activite existe deja");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // methode pour exporter des postulants tirés
    @ApiOperation(value = "methode pour exporter des postulants tirés.")
    @PostMapping("/export/{idtirage}/{idUtilisateur}")
    public ResponseEntity<Object> exporterTirage(@PathVariable("idtirage") Long idtirage,
            @PathVariable("idUtilisateur") Long idUtilisateur,
            HttpServletResponse response) {
        response.setContentType("application/octet-stream");

        try {
            Utilisateur Simpleutilisateur = new Utilisateur();
            Tirage tirage = tirageService.getById(idtirage);
            List<Postulant> postulantsListe = new ArrayList<>();

            for (PostulantTire p : tirage.getPostulanttires()) {

                postulantsListe.add(p.getPostulant());
            }

            ExcelGenerator generator = new ExcelGenerator(postulantsListe);
            generator.genererFichierExcel(response);

            //
            Historique historique = new Historique();
            historique.setDatehistorique(new Date());
            historique.setDescription(Simpleutilisateur.getPrenom() + " " + Simpleutilisateur.getNom()
                    + " a exporter une liste de postulants tirés.");
            //
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, postulantsListe);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    // la methode pour effectuer un tirage
    @ApiOperation(value = "La methode Pour effectuer un tirage.")
    @PostMapping("/tirage/new/{login}/{password}/{libelleliste}/{idactivite}/{iduser}/{nombre}/{libelleTirage}")
    public ResponseEntity<Object> DoTirage(@PathVariable("libelleliste") String libelleliste,
            @PathVariable("nombre") Long nombre, @PathVariable("idactivite") Long idactivite,
                @PathVariable("login") String login, @PathVariable("password") String password,
            @PathVariable("iduser") Long iduser,
            @PathVariable("libelleTirage") String libelleTirage) {

        Tirage exist = tirageService.findByLibelle(libelleTirage);
        if (exist == null) {

            Utilisateur utilisateur = utilisateurService.trouverParLoginAndPass(login,password);
            Activite activite = activiteService.GetById(idactivite);

            ListePostulant listePostulant = listePostulantService.retrouveParLibelle(libelleliste);
            Tirage tirage = new Tirage();
            tirage.setLibelle(libelleTirage);
            tirage.setListepostulant(listePostulant);
            tirage.setActivite(activite);
            tirage.setUtilisateur(utilisateur);

            System.out.println(libelleTirage);
            System.out.println(nombre);
            System.out.println(libelleliste);

            System.out.println(listePostulant.getPostulants());

            List<Postulant> postulanttires = tirageService.creer(tirage, listePostulant.getPostulants(), nombre);

            try {

                //
                Historique historique = new Historique();
                historique.setDatehistorique(new Date());
                historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                        + " a a effectuer un tirage.");
                //
                return ResponseMessage.generateResponse("ok", HttpStatus.OK, postulanttires);

            } catch (Exception e) {
                // TODO: handle exception
                return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

            }
        } else {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, "Ce tirage exist");
        }

    }

    // ---------------------------------Postulant
    // Tire---------------------------------------//
    // Creation de postulant tiré
    @ApiOperation(value = "Ajouter participant")
    @PostMapping("/create/participant/{idactivite}/{idUtilisateur}")
    public ResponseEntity<Object> createPostulantTire(@RequestBody Postulant participant,
            @PathVariable("idactivite") Long idactivite, @PathVariable("idUtilisateur") Long idUtilisateur) {

        try {
            Activite activite = activiteService.GetById(idactivite);
            Utilisateur utilisateur = utilisateurService.getById(idUtilisateur);

            AouP aouP = new AouP();
            aouP.setActivite(activite);
            aouP.setPostulant(participant);
            aouP.setTirage(false);

            //
            Historique historique = new Historique();
            historique.setDatehistorique(new Date());
            historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                    + " a a joute le participant: " + aouP.getPostulant().getEmail() + " à l'activite avec l'ID: "
                    + activite.getNom());
            //

            // tirageService.ajouterParticipant(participant, idtirage);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    // Afficher de postulant tiré
    @ApiOperation(value = "Afficher participant par son id")
    @GetMapping("/read/{id}/{idUtilisateur}")
    public ResponseEntity<Object> readPostulantTire(@PathVariable long id,
            @PathVariable("idUtilisateur") Long idUtilisateur) {
        try {

            Utilisateur utilisateur = utilisateurService.getById(idUtilisateur);
            PostulantTire post = postulantTrieService.read(id);

            //
            Historique historique = new Historique();
            historique.setDatehistorique(new Date());
            historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                    + " a afficher le participant: " + post.getPostulant().getEmail());
            //
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, post);

        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // Modifier de postulant tiré par son id
    @ApiOperation(value = "modifier participant par son id")
    @PutMapping("/update/{id}/{idUtilisateur}")
    public ResponseEntity<Object> updatePostulantTire(@RequestBody Postulant postulant, @PathVariable long id,
            @PathVariable("idUtilisateur") Long idUtilisateur) {
        try {
            Postulant post = postulantService.update(id, postulant);
            Utilisateur utilisateur = utilisateurService.getById(idUtilisateur);

            //
            Historique historique = new Historique();
            historique.setDatehistorique(new Date());
            historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                    + " a modifie le participant: " + post.getEmail());
            //

            return ResponseMessage.generateResponse("ok", HttpStatus.OK, post);

        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    // Supprimer Postulant tiré par son id
    @ApiOperation(value = "Supprimer participant par son id")
    @DeleteMapping("/delete/{id}/{idUtilisateur}")
    public ResponseEntity<Object> deletePostulantTire(@PathVariable long id,
            @PathVariable("idUtilisateur") Long idUtilisateur) {
        try {
            postulantTrieService.delete(id);
            Utilisateur utilisateur = utilisateurService.getById(idUtilisateur);

            //
            Historique historique = new Historique();
            historique.setDatehistorique(new Date());
            historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                    + " a suprime le postulant tire avec l'id: " + id);
            //
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    // Afficher tous les postulants
    @ApiOperation(value = "Afficher tous les participants")
    @GetMapping("/All/{idUtilisateur}")
    public ResponseEntity<Object> getAllPostulantTire(@PathVariable("idUtilisateur") Long idUtilisateur) {
        try {
            Utilisateur utilisateur = utilisateurService.getById(idUtilisateur);

            //
            Historique historique = new Historique();
            historique.setDatehistorique(new Date());
            historique.setDescription(utilisateur.getPrenom() + " " + utilisateur.getNom()
                    + " a affiché l'ensemble des participants.");
            //
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, postulantTrieService.getAll());

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    // :::::::::::::::total postulant: :::::::::::::::::::::

}
