package com.odc.Apiodkerp.Controller;

import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Models.PostulantTire;
import com.odc.Apiodkerp.Repository.PostulantTrieRepository;
import com.odc.Apiodkerp.Service.PostulantTrieService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/responsable")
@AllArgsConstructor
@Api(value = "responsable", description = "Les fonctionnalités liées à un responsable")
@CrossOrigin
public class ResponsableController {
    private PostulantTrieService postulantTrieService;
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
