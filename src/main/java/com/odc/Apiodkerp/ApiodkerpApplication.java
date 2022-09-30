package com.odc.Apiodkerp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.odc.Apiodkerp.Enum.Genre;
import com.odc.Apiodkerp.Models.Droit;
import com.odc.Apiodkerp.Models.Etat;
import com.odc.Apiodkerp.Models.FormatEmail;
import com.odc.Apiodkerp.Models.Role;
import com.odc.Apiodkerp.Models.Statut;
import com.odc.Apiodkerp.Models.TypeActivite;
import com.odc.Apiodkerp.Models.Utilisateur;
import com.odc.Apiodkerp.Service.DroitService;
import com.odc.Apiodkerp.Service.EtatService;
import com.odc.Apiodkerp.Service.FormatEmailService;
import com.odc.Apiodkerp.Service.RoleService;
import com.odc.Apiodkerp.Service.StatusService;
import com.odc.Apiodkerp.Service.TypeActiviteService;
import com.odc.Apiodkerp.Service.UtilisateurService;

@SpringBootApplication
public class ApiodkerpApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ApiodkerpApplication.class, args);

		UtilisateurService utilisateurService = ctx.getBean(UtilisateurService.class);
		RoleService roleService = ctx.getBean(RoleService.class);
		EtatService etatService = ctx.getBean(EtatService.class);
		TypeActiviteService typeActiviteService = ctx.getBean(TypeActiviteService.class);
		FormatEmailService formatEmailService = ctx.getBean(FormatEmailService.class);
		StatusService statusService = ctx.getBean(StatusService.class);
		DroitService droitService = ctx.getBean(DroitService.class);

		// droit
		Droit Cactivite = new Droit();
		Cactivite.setId(1L);
		Cactivite.setLibelle("Create Actvite");
		Cactivite.setDescription("Creer une activité.");

		Droit Ractivite = new Droit();
		Ractivite.setId(2L);
		Ractivite.setLibelle("Read Actvite");
		Ractivite.setDescription("afficher une activité.");

		// aoup
		Droit Caoup = new Droit();
		Caoup.setId(4L);
		Caoup.setLibelle("Create AouP");
		Caoup.setDescription("Creer, lire, mettre à jour et suprimer un apprenant ou un participant.");

		Droit Raoup = new Droit();
		Raoup.setId(2L);
		Raoup.setLibelle("CRUD AouP");
		Raoup.setDescription("Creer, lire, mettre à jour et suprimer un apprenant ou un participant.");

		// ::::::::::::::::::::

		Droit designation = new Droit();
		designation.setId(3L);
		designation.setLibelle("CRUD Designation");
		designation.setDescription("Creer, lire, mettre à jour et suprimer une designation.");

		Droit entite = new Droit();
		entite.setId(4L);
		entite.setLibelle("CRUD Entite");
		entite.setDescription("Creer, lire, mettre à jour et suprimer une entite.");

		Droit etat = new Droit();
		etat.setId(5L);
		etat.setLibelle("CRUD Etat");
		etat.setDescription("Creer, lire, mettre à jour et suprimer un etat.");

		Droit formatemail = new Droit();
		formatemail.setId(6L);
		formatemail.setLibelle("CRUD FormatEmail");
		formatemail.setDescription("Creer, lire, mettre à jour et suprimer un FormatEmail.");

		Droit intervenant = new Droit();
		intervenant.setId(7L);
		intervenant.setLibelle("CRUD Intervenant");
		intervenant.setDescription("Creer, lire, mettre à jour et suprimer un intervenant externe.");

		Droit listepostulant = new Droit();
		listepostulant.setId(8L);
		listepostulant.setLibelle("CRUD ListePostulant");
		listepostulant.setDescription("Creer, lire, mettre à jour et suprimer une liste de postulant.");

		Droit postulant = new Droit();
		postulant.setId(9L);
		postulant.setLibelle("CRUD postulant");
		postulant.setDescription("Creer, lire, mettre à jour et suprimer un postulant.");

		Droit presence = new Droit();
		presence.setId(10L);
		presence.setLibelle("CRUD Presence");
		presence.setDescription("Creer, lire, mettre à jour et suprimer une presence.");

		Droit role = new Droit();
		role.setId(11L);
		role.setLibelle("CRUD Role");
		role.setDescription("Creer, lire, mettre à jour et suprimer une role.");

		Droit salle = new Droit();
		salle.setId(12L);
		salle.setLibelle("CRUD Salle");
		salle.setDescription("Creer, lire, mettre à jour et suprimer une salle.");

		Droit tache = new Droit();
		tache.setId(13L);
		tache.setLibelle("CRUD Tache");
		tache.setDescription("Creer, lire, mettre à jour et suprimer une tache.");

		Droit tirage = new Droit();
		tirage.setId(14L);
		tirage.setLibelle("CRUD Tirage");
		tirage.setDescription("Creer, lire, mettre à jour et suprimer un tirage.");

		Droit utilisateur = new Droit();
		utilisateur.setId(15L);
		utilisateur.setLibelle("CRUD Utilisateur");
		utilisateur.setDescription("Creer, lire, mettre à jour et suprimer un tirage.");

		Droit typeactivite = new Droit();
		typeactivite.setId(16L);
		typeactivite.setLibelle("CRUD TypeActivite");
		typeactivite.setDescription("Creer, lire, mettre à jour et suprimer un type d'activité.");

		droitService.Create(Cactivite);
		droitService.Create(Ractivite);
		// droitService.Create(Uactivite);
		// droitService.Create(Dactivite);

		droitService.Create(Caoup);
		droitService.Create(designation);
		droitService.Create(entite);
		droitService.Create(etat);
		droitService.Create(formatemail);
		droitService.Create(intervenant);
		droitService.Create(listepostulant);
		droitService.Create(postulant);
		droitService.Create(presence);
		droitService.Create(role);
		droitService.Create(salle);
		droitService.Create(tache);
		droitService.Create(tirage);
		droitService.Create(typeactivite);
		droitService.Create(utilisateur);

		// creation des roles
		Role user = new Role();

		user.setId(1L);
		user.setLibellerole("USER");

		Role responsable = new Role();

		responsable.setId(2L);
		responsable.setLibellerole("RESPONSABLE");

		Role admin = new Role();

		admin.setId(3L);
		admin.setLibellerole("ADMIN");

		roleService.create(user);
		roleService.create(responsable);
		roleService.create(admin);

		if (utilisateurService.getByEmail("ballo@gmail.com") == null
				&& utilisateurService.getByEmail("maiga@gmail.com") == null) {
			// creation des super administrateurs
			Utilisateur ballo = new Utilisateur();
			ballo.setActive(true);
			ballo.setNom("Ballo");
			ballo.setEmail("ballo@gmail.com");
			ballo.setRole(admin);
			ballo.setPrenom("Ibrahima");
			ballo.setGenre(Genre.Masculin);
			ballo.setPassword("bababallo#123#");
			ballo.setId(1L);
			ballo.setLogin("bababallo");

			Utilisateur abasse = new Utilisateur();
			abasse.setActive(true);
			abasse.setNom("Maiga");
			abasse.setEmail("maiga@gmail.com");
			abasse.setRole(admin);
			abasse.setPrenom("Abasse");
			abasse.setGenre(Genre.Masculin);
			abasse.setPassword("abassemaiga");
			abasse.setId(2L);
			abasse.setLogin("abasse");

			utilisateurService.creer(ballo);
			utilisateurService.creer(abasse);
		}

		// création des etats
		Etat encour = new Etat();

		encour.setId(1L);
		encour.setStatut("EN COUR");

		Etat avenir = new Etat();

		avenir.setId(2L);
		avenir.setStatut("A VENIR");

		Etat termine = new Etat();

		termine.setId(3L);
		termine.setStatut("TERMINE");

		etatService.Create(encour);
		etatService.Create(avenir);
		etatService.Create(termine);

		// creations des types activites

		TypeActivite talk = new TypeActivite();
		talk.setLibelle("Talk");
		talk.setId(1L);

		TypeActivite evenement = new TypeActivite();

		evenement.setLibelle("Evenement");
		evenement.setId(2L);

		TypeActivite formations = new TypeActivite();

		formations.setLibelle("Formations");
		formations.setId(3L);

		typeActiviteService.creer(talk);
		typeActiviteService.creer(evenement);
		typeActiviteService.creer(formations);

		/// format email
		FormatEmail formatOrang = new FormatEmail();
		formatOrang.setLibelle("@orangemali.com");
		formatEmailService.Create(formatOrang);

		// status
		Statut encours = new Statut();
		encours.setLibelle("ENCOUR");

		Statut terminee = new Statut();
		terminee.setLibelle("TERMINE");

		statusService.creer(encours);
		statusService.creer(terminee);

	}

}
