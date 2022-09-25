package com.odc.Apiodkerp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.odc.Apiodkerp.Enum.Genre;
import com.odc.Apiodkerp.Models.Etat;
import com.odc.Apiodkerp.Models.Role;
import com.odc.Apiodkerp.Models.TypeActivite;
import com.odc.Apiodkerp.Models.Utilisateur;
import com.odc.Apiodkerp.Service.EtatService;
import com.odc.Apiodkerp.Service.RoleService;
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

	}

}
