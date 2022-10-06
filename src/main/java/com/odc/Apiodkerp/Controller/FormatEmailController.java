package com.odc.Apiodkerp.Controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Configuration.SaveImage;
import com.odc.Apiodkerp.Models.*;
import com.odc.Apiodkerp.Service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/formatemail")
@Api(value = "formatemail", description = "Les fonctionnalités liées à une formatemail")
@CrossOrigin

public class FormatEmailController {

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

    // :::::::::::::::::::::::::::::::::::::::FormationEmail
    // :::::::::::::::::::::::::::::::
    @ApiOperation(value = "Ajouter FormatEmail")
    @PostMapping("/formaemail")
    public ResponseEntity<Object> ajouterFormatEmail(@RequestParam(value = "formatemail") String formatEmail,
            @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            FormatEmail formatEmai = new JsonMapper().readValue(formatEmail, FormatEmail.class);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit Cformat = droitService.GetLibelle("Create FormatEmail");

            if (user.getRole().getDroits().contains(Cformat)) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        formatEmailService.Create(formatEmai));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }

    // ::::::::::::::::::::::::::::::::::: Modifier FormatEmail
    // ::::::::::::::::::::::::::::::::::::
    @ApiOperation(value = "Modifier formatEmail")
    @PostMapping("/formatemail/modifier/{id}")
    public ResponseEntity<Object> ModifierFormatEmail(@PathVariable long id,
            @RequestParam(value = "formatemail") String formatEmail, @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            FormatEmail formatEmai = new JsonMapper().readValue(formatEmail, FormatEmail.class);
            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Uformat = droitService.GetLibelle("Update FormatEmail");

            if (user.getRole().getDroits().contains(Uformat)) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        formatEmailService.Update(id, formatEmai));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
    // ::::::::::::::::::::::::::::::::::: Supprimer FormatEmail
    // ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Supprimer Format Email")
    @PostMapping("/formatemail/supprimer/{id}")
    public ResponseEntity<Object> SupprimerFormatEmail(@PathVariable long id,
            @RequestParam(value = "user") String userVenant) {
        try {

            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Dformat = droitService.GetLibelle("Delete FormatEmail");

            if (user.getRole().getDroits().contains(Dformat)) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        formatEmailService.Delete(id));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }
    // ::::::::::::::::::::::::::::::::::: Get par id FormatEmail
    // ::::::::::::::::::::::::::::::::::::

    @ApiOperation(value = "Format email par id")
    @PostMapping("/formatEmail/GetId/{id}")
    public ResponseEntity<Object> GetFormatEMailparId(@PathVariable long id,
            @RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());

            Droit Gformat = droitService.GetLibelle("Read FormatEmail");

            if (user.getRole().getDroits().contains(Gformat)) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,
                        formatEmailService.GetById(id));
            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }


    @ApiOperation(value = "Afficher toutes les formats mail")
    @PostMapping("/getAll")
    public ResponseEntity<Object> getAllformatEmail(@RequestParam(value = "user") String userVenant) {
        try {
            Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

            Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
                    utilisateur.getPassword());
            Droit RAoup = droitService.GetLibelle("Read formatEmail");

            if (user.getRole().getDroits().contains(RAoup)) {
                return ResponseMessage.generateResponse("ok", HttpStatus.OK,formatEmailService.GetAll());

            } else {
                return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorise");

            }

        } catch (Exception e) {
            // TODO: handle exception
            return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

        }
    }


    // @ApiOperation(value = "Afficher toutes les formats mail")
    // @PostMapping("/getAll")
    // public ResponseEntity<Object> getAllformatEmail(@RequestParam(value = "user") String userVenant) {
    //     try {
    //         Utilisateur utilisateur = new JsonMapper().readValue(userVenant, Utilisateur.class);

    //         Utilisateur user = utilisateurService.trouverParLoginAndPass(utilisateur.getLogin(),
    //                 utilisateur.getPassword());
    //         Droit RAoup = droitService.GetLibelle("Read formatEmail");

    //         if (user.getRole().getDroits().contains(RAoup)) {
    //             return ResponseMessage.generateResponse("ok", HttpStatus.OK,formatEmailService.GetAll());

    //         } else {
    //             return ResponseMessage.generateResponse("error", HttpStatus.OK, "Non autorisé");

    //         }

    //     } catch (Exception e) {
    //         // TODO: handle exception
    //         return ResponseMessage.generateResponse("error", HttpStatus.OK, e.getMessage());

    //     }
    // }

}
