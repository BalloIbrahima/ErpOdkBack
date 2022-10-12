package com.odc.Apiodkerp;

import java.util.Arrays;
import java.util.List;

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
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableScheduling
@SpringBootApplication
public class ApiodkerpApplication {
	// @Bean
	// public CorsFilter corsFilter() {
	// 	CorsConfiguration corsConfiguration = new CorsConfiguration();
	// 	corsConfiguration.setAllowCredentials(true);
	// 	corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:8100"));
	// 	corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
	// 			"Accept", "Authorization", "Origin, Accept", "X-Requested-With",
	// 			"Access-Control-Request-Method", "Access-Control-Request-Headers"));
	// 	corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
	// 			"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
	// 	corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	// 	UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
	// 	urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
	// 	return new CorsFilter(urlBasedCorsConfigurationSource);
	// }

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
		Cactivite.setLibelle("Create Activite");
		Cactivite.setDescription("Creer une activité.");

		Droit Ractivite = new Droit();
		Ractivite.setId(2L);
		Ractivite.setLibelle("Read Activite");
		Ractivite.setDescription("afficher une activité.");

		Droit Uactivite = new Droit();
		Uactivite.setId(3L);
		Uactivite.setLibelle("Update Activite");
		Uactivite.setDescription("Mettre à jour une activité.");

		Droit Dactivite = new Droit();
		Dactivite.setId(4L);
		Dactivite.setLibelle("Delete Activite");
		Dactivite.setDescription("Supprimer une activité.");

		// aoup
		Droit Caoup = new Droit();
		Caoup.setId(5L);
		Caoup.setLibelle("Create AouP");
		Caoup.setDescription("Creer un apprenant ou un participant.");

		Droit Raoup = new Droit();
		Raoup.setId(6L);
		Raoup.setLibelle("Read AouP");
		Raoup.setDescription("Afficher un apprenant ou un participant.");

		Droit Uaoup = new Droit();
		Uaoup.setId(7L);
		Uaoup.setLibelle("Update AouP");
		Uaoup.setDescription("mettre à jour un apprenant ou un participant.");

		Droit Daoup = new Droit();
		Daoup.setId(8L);
		Daoup.setLibelle("Delete AouP");
		Daoup.setDescription("Supprimer un apprenant ou un participant.");

		// ::::::::::::::::::::

		Droit Cdesignation = new Droit();
		Cdesignation.setId(9L);
		Cdesignation.setLibelle("Create Designation");
		Cdesignation.setDescription("Creer une designation.");

		Droit Rdesignation = new Droit();
		Rdesignation.setId(10L);
		Rdesignation.setLibelle("Read Designation");
		Rdesignation.setDescription("Lire une designation.");

		Droit Udesignation = new Droit();
		Udesignation.setId(11L);
		Udesignation.setLibelle("Update Designation");
		Udesignation.setDescription("Mettre à jour une designation.");

		Droit Ddesignation = new Droit();
		Ddesignation.setId(12L);
		Ddesignation.setLibelle("Delete Designation");
		Ddesignation.setDescription("Suprimer une designation.");

		Droit Centite = new Droit();
		Centite.setId(13L);
		Centite.setLibelle("Create Entite");
		Centite.setDescription("Creer une entite.");

		Droit Rentite = new Droit();
		Rentite.setId(14L);
		Rentite.setLibelle("Read Entite");
		Rentite.setDescription("Lire une entite.");

		Droit Uentite = new Droit();
		Uentite.setId(15L);
		Uentite.setLibelle("Update Entite");
		Uentite.setDescription("Mettre à jour une entite.");

		Droit Dentite = new Droit();
		Dentite.setId(16L);
		Dentite.setLibelle("Delete Entite");
		Dentite.setDescription("Suprimer une entite.");

		Droit Cetat = new Droit();
		Cetat.setId(17L);
		Cetat.setLibelle("Create Etat");
		Cetat.setDescription("Creer un etat.");

		Droit Retat = new Droit();
		Retat.setId(18L);
		Retat.setLibelle("Read Etat");
		Retat.setDescription("Lire un etat.");

		Droit Uetat = new Droit();
		Uetat.setId(19L);
		Uetat.setLibelle("Update Etat");
		Uetat.setDescription("Mettre à jour un etat.");

		Droit Detat = new Droit();
		Detat.setId(20L);
		Detat.setLibelle("Delete Etat");
		Detat.setDescription("Suprimer un etat.");

		Droit Cformatemail = new Droit();
		Cformatemail.setId(21L);
		Cformatemail.setLibelle("Create FormatEmail");
		Cformatemail.setDescription("Creer un FormatEmail.");

		Droit Rformatemail = new Droit();
		Rformatemail.setId(22L);
		Rformatemail.setLibelle("Read FormatEmail");
		Rformatemail.setDescription("Lire un FormatEmail.");

		Droit Uformatemail = new Droit();
		Uformatemail.setId(23L);
		Uformatemail.setLibelle("Update FormatEmail");
		Uformatemail.setDescription("Mettre à jour un FormatEmail.");

		Droit Dformatemail = new Droit();
		Dformatemail.setId(24L);
		Dformatemail.setLibelle("Delete FormatEmail");
		Dformatemail.setDescription("Suprimer un FormatEmail.");

		Droit Cintervenant = new Droit();
		Cintervenant.setId(25L);
		Cintervenant.setLibelle("Create Intervenant");
		Cintervenant.setDescription("Creer un intervenant externe.");

		Droit Rintervenant = new Droit();
		Rintervenant.setId(26L);
		Rintervenant.setLibelle("Read Intervenant");
		Rintervenant.setDescription("Lire un intervenant externe.");

		Droit Uintervenant = new Droit();
		Uintervenant.setId(27L);
		Uintervenant.setLibelle("Update Intervenant");
		Uintervenant.setDescription("Mettre à jour un intervenant externe.");

		Droit Dintervenant = new Droit();
		Dintervenant.setId(28L);
		Dintervenant.setLibelle("Delete Intervenant");
		Dintervenant.setDescription("Suprimer un intervenant externe.");

		Droit Clistepostulant = new Droit();
		Clistepostulant.setId(29L);
		Clistepostulant.setLibelle("Create ListePostulant");
		Clistepostulant.setDescription("Creer une liste de postulant.");

		Droit Rlistepostulant = new Droit();
		Rlistepostulant.setId(30L);
		Rlistepostulant.setLibelle("Read ListePostulant");
		Rlistepostulant.setDescription("Lire une liste de postulant.");

		Droit Ulistepostulant = new Droit();
		Ulistepostulant.setId(31L);
		Ulistepostulant.setLibelle("Update ListePostulant");
		Ulistepostulant.setDescription("Mettre à jour une liste de postulant.");

		Droit Dlistepostulant = new Droit();
		Dlistepostulant.setId(32L);
		Dlistepostulant.setLibelle("Delete ListePostulant");
		Dlistepostulant.setDescription("Suprimer une liste de postulant.");

		Droit Cpostulant = new Droit();
		Cpostulant.setId(33L);
		Cpostulant.setLibelle("Create postulant");
		Cpostulant.setDescription("Creer un postulant.");

		Droit Rpostulant = new Droit();
		Rpostulant.setId(34L);
		Rpostulant.setLibelle("Read postulant");
		Rpostulant.setDescription("Lire un postulant.");

		Droit Upostulant = new Droit();
		Upostulant.setId(35L);
		Upostulant.setLibelle("Update postulant");
		Upostulant.setDescription("Mettre à jour un postulant.");

		Droit Dpostulant = new Droit();
		Dpostulant.setId(36L);
		Dpostulant.setLibelle("Delete postulant");
		Dpostulant.setDescription("Suprimer un postulant.");

		Droit Cpresence = new Droit();
		Cpresence.setId(37L);
		Cpresence.setLibelle("Create Presence");
		Cpresence.setDescription("Creer une presence.");

		Droit Rpresence = new Droit();
		Rpresence.setId(38L);
		Rpresence.setLibelle("Read Presence");
		Rpresence.setDescription("Lire une presence.");

		Droit Upresence = new Droit();
		Upresence.setId(39L);
		Upresence.setLibelle("Update Presence");
		Upresence.setDescription("Mettre à jour une presence.");

		Droit Dpresence = new Droit();
		Dpresence.setId(40L);
		Dpresence.setLibelle("Delete Presence");
		Dpresence.setDescription("Suprimer une presence.");

		Droit Crole = new Droit();
		Crole.setId(41L);
		Crole.setLibelle("Create Role");
		Crole.setDescription("Creer une role.");

		Droit Rrole = new Droit();
		Rrole.setId(42L);
		Rrole.setLibelle("Read Role");
		Rrole.setDescription("Lire une role.");

		Droit Urole = new Droit();
		Urole.setId(43L);
		Urole.setLibelle("Update Role");
		Urole.setDescription("Mettre à jour une role.");

		Droit Drole = new Droit();
		Drole.setId(44L);
		Drole.setLibelle("Delete Role");
		Drole.setDescription("Suprimer une role.");

		Droit Csalle = new Droit();
		Csalle.setId(45L);
		Csalle.setLibelle("Create Salle");
		Csalle.setDescription("Creer une salle.");

		Droit Rsalle = new Droit();
		Rsalle.setId(46L);
		Rsalle.setLibelle("Read Salle");
		Rsalle.setDescription("Lire une salle.");

		Droit Usalle = new Droit();
		Usalle.setId(47L);
		Usalle.setLibelle("Update Salle");
		Usalle.setDescription("Mettre à jour une salle.");

		Droit Dsalle = new Droit();
		Dsalle.setId(48L);
		Dsalle.setLibelle("Delete Salle");
		Dsalle.setDescription("Suprimer une salle.");

		Droit Ctache = new Droit();
		Ctache.setId(49L);
		Ctache.setLibelle("Create Tache");
		Ctache.setDescription("Creer une tache.");

		Droit Rtache = new Droit();
		Rtache.setId(50L);
		Rtache.setLibelle("Read Tache");
		Rtache.setDescription("Lire une tache.");

		Droit Utache = new Droit();
		Utache.setId(51L);
		Utache.setLibelle("Update Tache");
		Utache.setDescription("Mettre à jour une tache.");

		Droit Dtache = new Droit();
		Dtache.setId(52L);
		Dtache.setLibelle("Delete Tache");
		Dtache.setDescription("Suprimer une tache.");

		Droit Ctirage = new Droit();
		Ctirage.setId(53L);
		Ctirage.setLibelle("Create Tirage");
		Ctirage.setDescription("Creer un tirage.");

		Droit Rtirage = new Droit();
		Rtirage.setId(54L);
		Rtirage.setLibelle("Read Tirage");
		Rtirage.setDescription("Lire un tirage.");

		Droit Utirage = new Droit();
		Utirage.setId(55L);
		Utirage.setLibelle("Update Tirage");
		Utirage.setDescription("Mettre à jour un tirage.");

		Droit Dtirage = new Droit();
		Dtirage.setId(56L);
		Dtirage.setLibelle("Delete Tirage");
		Dtirage.setDescription("Suprimer un tirage.");

		Droit Cutilisateur = new Droit();
		Cutilisateur.setId(57L);
		Cutilisateur.setLibelle("Create Utilisateur");
		Cutilisateur.setDescription("Creer un utilisateur.");

		Droit Rutilisateur = new Droit();
		Rutilisateur.setId(58L);
		Rutilisateur.setLibelle("Read Utilisateur");
		Rutilisateur.setDescription("Lire un utilisateur.");

		Droit Uutilisateur = new Droit();
		Uutilisateur.setId(59L);
		Uutilisateur.setLibelle("Update Utilisateur");
		Uutilisateur.setDescription("Mettre à jour un utilisateur.");

		Droit Dutilisateur = new Droit();
		Dutilisateur.setId(60L);
		Dutilisateur.setLibelle("Delete Utilisateur");
		Dutilisateur.setDescription("Suprimer un utilisateur.");

		Droit Ctypeactivite = new Droit();
		Ctypeactivite.setId(61L);
		Ctypeactivite.setLibelle("Create TypeActivite");
		Ctypeactivite.setDescription("Creer un type d'activité.");

		Droit Rtypeactivite = new Droit();
		Rtypeactivite.setId(62L);
		Rtypeactivite.setLibelle("Read TypeActivite");
		Rtypeactivite.setDescription("Lire un type d'activité.");

		Droit Utypeactivite = new Droit();
		Utypeactivite.setId(63L);
		Utypeactivite.setLibelle("Update TypeActivite");
		Utypeactivite.setDescription("Mettre à jour un type d'activité.");

		Droit Dtypeactivite = new Droit();
		Dtypeactivite.setId(64L);
		Dtypeactivite.setLibelle("Delete TypeActivite");
		Dtypeactivite.setDescription("Suprimer un type d'activité.");

		droitService.Create(Cactivite);
		droitService.Create(Ractivite);
		droitService.Create(Uactivite);
		droitService.Create(Dactivite);

		droitService.Create(Caoup);
		droitService.Create(Raoup);
		droitService.Create(Uaoup);
		droitService.Create(Daoup);

		droitService.Create(Cdesignation);
		droitService.Create(Rdesignation);
		droitService.Create(Udesignation);
		droitService.Create(Ddesignation);

		droitService.Create(Centite);
		droitService.Create(Rentite);
		droitService.Create(Uentite);
		droitService.Create(Dentite);

		droitService.Create(Cetat);
		droitService.Create(Retat);
		droitService.Create(Uetat);
		droitService.Create(Detat);

		droitService.Create(Cformatemail);
		droitService.Create(Rformatemail);
		droitService.Create(Uformatemail);
		droitService.Create(Dformatemail);

		droitService.Create(Cintervenant);
		droitService.Create(Rintervenant);
		droitService.Create(Uintervenant);
		droitService.Create(Dintervenant);

		droitService.Create(Clistepostulant);
		droitService.Create(Rlistepostulant);
		droitService.Create(Ulistepostulant);
		droitService.Create(Dlistepostulant);

		droitService.Create(Cpostulant);
		droitService.Create(Rpostulant);
		droitService.Create(Upostulant);
		droitService.Create(Dpostulant);

		droitService.Create(Cpresence);
		droitService.Create(Rpresence);
		droitService.Create(Upresence);
		droitService.Create(Dpresence);

		droitService.Create(Crole);
		droitService.Create(Rrole);
		droitService.Create(Urole);
		droitService.Create(Drole);

		droitService.Create(Csalle);
		droitService.Create(Rsalle);
		droitService.Create(Usalle);
		droitService.Create(Dsalle);

		droitService.Create(Ctache);
		droitService.Create(Rtache);
		droitService.Create(Utache);
		droitService.Create(Dtache);

		droitService.Create(Ctirage);
		droitService.Create(Rtirage);
		droitService.Create(Utirage);
		droitService.Create(Dtirage);

		droitService.Create(Ctypeactivite);
		droitService.Create(Rtypeactivite);
		droitService.Create(Utypeactivite);
		droitService.Create(Dtypeactivite);

		droitService.Create(Cutilisateur);
		droitService.Create(Rutilisateur);
		droitService.Create(Uutilisateur);
		droitService.Create(Dutilisateur);

		// creation des roles
		List<Droit> droits = droitService.GetAll();

		Role user = new Role();

		user.setId(1L);
		user.setLibellerole("USER");
		for (Droit droit : droits) {
			user.getDroits().add(droit);
		}

		Role responsable = new Role();

		for (Droit droit : droits) {
			responsable.getDroits().add(droit);
		}
		responsable.setId(2L);
		responsable.setLibellerole("RESPONSABLE");

		Role admin = new Role();

		admin.setId(3L);
		admin.setLibellerole("ADMIN");
		for (Droit droit : droits) {
			admin.getDroits().add(droit);
		}

		roleService.create(user);
		roleService.create(responsable);
		roleService.create(admin);

		if (utilisateurService.getByEmail("ballo@gmail.com") == null
				&& utilisateurService.getByEmail("maiga@gmail.com") == null) {
			// creation des super administrateurs
			Utilisateur ballo = new Utilisateur();
			ballo.setActive(true);
			ballo.setNom("Ballo");
			ballo.setEmail("ibrahimaballo01@gmail.com");
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
		formatOrang.setId(1L);
		formatOrang.setLibelle("@orangemali.com");
		formatEmailService.Create(formatOrang);

		// status
		Statut encours = new Statut();
		encours.setId(1L);
		encours.setLibelle("ENCOUR");

		Statut terminee = new Statut();
		terminee.setId(2L);
		terminee.setLibelle("TERMINE");

		statusService.creer(encours);
		statusService.creer(terminee);

	}

}
