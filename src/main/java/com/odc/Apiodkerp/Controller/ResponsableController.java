package com.odc.Apiodkerp.Controller;

import com.odc.Apiodkerp.Configuration.ResponseMessage;
import com.odc.Apiodkerp.Models.Utilisateur;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
