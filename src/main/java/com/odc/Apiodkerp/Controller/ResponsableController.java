package com.odc.Apiodkerp.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Models.ListePostulant;
import com.odc.Apiodkerp.Models.Postulant;
import com.odc.Apiodkerp.Models.PostulantTire;
import com.odc.Apiodkerp.Models.Tirage;
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

    // la methode pour importer une liste
    @ApiOperation(value = "la methode pour importer une liste.")
    @PostMapping("/list/new/{libelleliste}")
    public ResponseEntity<Object> CreateTirage(@PathVariable("libelleliste") String libelleliste,
            @RequestParam("file") MultipartFile file) {

        try {
            ListePostulant liste = listePostulantService.retrouveParLibelle(libelleliste);
            if (liste == null) {
                if (ExcelImport.verifier(file)) {
                    ListePostulant listePostulant = new ListePostulant();

                    listePostulant.setLibelle(libelleliste);
                    listePostulant.setDateimport(new Date());

                    List<Postulant> postulants = ExcelImport.postulantsExcel(file);
                    // insertion des postulants recuperes à partir du fichier excel
                    for (Postulant p : postulants) {

                        if (postulantService.GetByEmail(p.getEmail()) == null
                                & p.getNom() != null & p.getPrenom() != null) {
                            Postulant pc = postulantService.creer(p);

                            p.getListePostulants().add(listePostulant);
                            listePostulant.getPostulants().add(pc);

                        } else if (p.getEmail() != null & p.getNom() != null & p.getPrenom() != null) {
                            Postulant pc = postulantService.GetByEmail(p.getEmail());
                            listePostulant.getPostulants().add(pc);

                        }

                    }

                    return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                            listePostulantService.creer(listePostulant));

                } else {
                    return ResponseMessage.generateResponse("Veuiller fournir un fichier Excel valide!", HttpStatus.OK,
                            null);
                }
            } else {
                // Il existe une liste avec la même libelle
                return ResponseMessage.generateResponse("Cette lise existe deja", HttpStatus.OK, null);

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
    @PostMapping("/tirage/new/{libelleliste}/{nombre}/{libelleTirage}")
    public ResponseEntity<Object> DoTirage(@PathVariable("libelleliste") String libelleliste,
            @PathVariable("nombre") Long nombre,
            @PathVariable("libelleTirage") String libelleTirage) {
        ListePostulant listePostulant = listePostulantService.retrouveParLibelle(libelleliste);

        Tirage tirage = new Tirage();
        tirage.setLibelle(libelleTirage);

        List<Postulant> postulanttires = tirageService.creer(tirage, listePostulant.getPostulants(), nombre);

        try {

            return ResponseMessage.generateResponse("ok", HttpStatus.OK, postulanttires);

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
}
