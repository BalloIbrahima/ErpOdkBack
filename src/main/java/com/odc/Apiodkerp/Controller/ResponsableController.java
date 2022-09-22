package com.odc.Apiodkerp.Controller;

import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Models.PostulantTire;
import com.odc.Apiodkerp.Repository.PostulantTrieRepository;
import com.odc.Apiodkerp.Service.PostulantTrieService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

import java.util.List;

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

// ---------------------------------Postulant Tire---------------------------------------//
    //Creation de postulant tiré
    @ApiOperation(value = "Ajouter participant")
    @PostMapping("/create")
    public ResponseEntity<PostulantTire> createPostulantTire(@RequestBody PostulantTire postulantTire){
        postulantTrieService.create(postulantTire);
        return new ResponseEntity<>(postulantTire, HttpStatus.CREATED);
    }
    //Afficher de postulant tiré
    @ApiOperation(value = "Afficher participant par son id")
    @GetMapping("/read/{id}")
    public ResponseEntity<Object> readPostulantTire(@PathVariable long id){
        try {
            PostulantTire post=postulantTrieService.read(id);
            return new ResponseEntity<>(post,HttpStatus.OK);
        }catch (Exception e) {
            return ResponseMessage.generateResponse("postulant non trouvé", HttpStatus.OK,e.getMessage());
        }
    }
    //Modifier de postulant tiré par son id
    @ApiOperation(value = "modifier participant par son id")
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updatePostulantTire(@RequestBody PostulantTire postulantTire,@PathVariable long id){
        try {
            PostulantTire post=postulantTrieService.update(postulantTire,id);
            return new ResponseEntity<>(post,HttpStatus.OK);
        }catch (Exception e){
            return ResponseMessage.generateResponse("Participant non trouvé",HttpStatus.OK,e.getMessage());
        }

    }
    //Supprimer Postulant tiré par son id
    @ApiOperation(value = "Supprimer participant par son id")
    @DeleteMapping("/delete/{id}")
    public void deletePostulantTire(@PathVariable long id){
         postulantTrieService.delete(id);
    }
    //Afficher tous les postulants
    @ApiOperation(value = "Afficher tous les participants")
    @GetMapping("/All")
    public List<PostulantTire> getAllPostulantTire(){
        return postulantTrieService.getAll();
    }
    
}
