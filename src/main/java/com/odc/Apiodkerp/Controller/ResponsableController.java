package com.odc.Apiodkerp.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.odc.Apiodkerp.Configuration.ResponseMessage;

import com.odc.Apiodkerp.Models.Utilisateur;
import io.swagger.annotations.ApiOperation;

import com.odc.Apiodkerp.Models.PostulantTire;
import com.odc.Apiodkerp.Models.Role;
import com.odc.Apiodkerp.Repository.PostulantTrieRepository;
import com.odc.Apiodkerp.Service.PostulantTrieService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
import com.odc.Apiodkerp.Models.ListePostulant;
import com.odc.Apiodkerp.Models.Postulant;

import com.odc.Apiodkerp.Models.Tirage;

import com.odc.Apiodkerp.Service.ActiviteService;
import com.odc.Apiodkerp.Service.EntiteService;
import com.odc.Apiodkerp.Service.EtatService;
import com.odc.Apiodkerp.Service.ListePostulantService;
import com.odc.Apiodkerp.Service.PostulantService;

import com.odc.Apiodkerp.Service.PresenceService;
import com.odc.Apiodkerp.Service.RoleService;
import com.odc.Apiodkerp.Service.SalleService;
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
    @PostMapping("/listpostulant/new/{libelleliste}")
    public ResponseEntity<Object> ImportListePostulant(@PathVariable("libelleliste") String libelleliste,
            @RequestParam("file") MultipartFile file) {

        try {
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
    // la methode pour importer une liste de postulant
    @ApiOperation(value = "la methode pour importer une liste de postulant.")
    @PostMapping("/listparticipant/new/{idTirage}")
    public ResponseEntity<Object> ImportListeParticipant(@RequestParam("file") MultipartFile file,
            @PathVariable("idTirage") Long idTirage) {

        try {
            Tirage tirage = tirageService.getById(idTirage);
            if (tirage != null) {
                if (ExcelImport.verifier(file)) {

                    List<Postulant> postulants = ExcelImport.postulantsExcel(file);
                    // insertion des postulants recuperes à partir du fichier excel
                    for (Postulant p : postulants) {

                        if (postulantService.GetByEmail(p.getEmail()) == null
                                & p.getNom() != null & p.getPrenom() != null) {
                            Postulant pc = postulantService.creer(p);

                            tirageService.ajouterParticipant(pc, idTirage);
                            // p.getListePostulants().add(listePostulant);
                            // listePostulant.getPostulants().add(pc);

                        } else if (p.getEmail() != null & p.getNom() != null & p.getPrenom() != null) {

                            Postulant pc = postulantService.GetByEmail(p.getEmail());
                            tirageService.ajouterParticipant(pc, idTirage);

                        }

                    }

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            null);

                } else {
                    return ResponseMessage.generateResponse("error", HttpStatus.OK,
                            "Veuiller fournir un fichier Excel valide !");
                }
            } else {
                // Il existe une liste avec la même libelle
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Cette tirage n'existe deja");

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
            HttpServletResponse response) {
        response.setContentType("application/octet-stream");

        try {
            Tirage tirage = tirageService.getById(idtirage);
            List<Postulant> postulantsListe = new ArrayList<>();

            for (PostulantTire p : tirage.getPostulanttires()) {

                postulantsListe.add(p.getPostulant());
            }

            ExcelGenerator generator = new ExcelGenerator(postulantsListe);
            generator.genererFichierExcel(response);

            return ResponseMessage.generateResponse("ok", HttpStatus.OK, postulantsListe);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    // la methode pour effectuer un tirage
    @ApiOperation(value = "La methode Pour effectuer un tirage.")
    @PostMapping("/tirage/new/{libelleliste}/{idactivite}/{iduser}/{nombre}/{libelleTirage}")
    public ResponseEntity<Object> DoTirage(@PathVariable("libelleliste") String libelleliste,
            @PathVariable("nombre") Long nombre, @PathVariable("idactivite") Long idactivite,
            @PathVariable("iduser") Long iduser,
            @PathVariable("libelleTirage") String libelleTirage) {

        Utilisateur utilisateur = utilisateurService.getById(iduser);
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
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, postulanttires);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // ---------------------------------Postulant
    // Tire---------------------------------------//
    // Creation de postulant tiré
    @ApiOperation(value = "Ajouter participant")
    @PostMapping("/create/participant/{idtirage}")
    public ResponseEntity<Object> createPostulantTire(@RequestBody Postulant participant,
            @PathVariable("idtirage") Long idtirage) {

        try {
            tirageService.ajouterParticipant(participant, idtirage);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    // Afficher de postulant tiré
    @ApiOperation(value = "Afficher participant par son id")
    @GetMapping("/read/{id}")
    public ResponseEntity<Object> readPostulantTire(@PathVariable long id) {
        try {
            PostulantTire post = postulantTrieService.read(id);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, post);

        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }
    }

    // Modifier de postulant tiré par son id
    @ApiOperation(value = "modifier participant par son id")
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updatePostulantTire(@RequestBody Postulant postulant, @PathVariable long id) {
        try {
            Postulant post = postulantService.update(id, postulant);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, post);

        } catch (Exception e) {
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());
        }

    }

    // Supprimer Postulant tiré par son id
    @ApiOperation(value = "Supprimer participant par son id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deletePostulantTire(@PathVariable long id) {
        try {
            postulantTrieService.delete(id);
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, null);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

    // Afficher tous les postulants
    @ApiOperation(value = "Afficher tous les participants")
    @GetMapping("/All")
    public ResponseEntity<Object> getAllPostulantTire() {
        try {
            return ResponseMessage.generateResponse("ok", HttpStatus.OK, postulantTrieService.getAll());

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }

    }

}
