package projetapi.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import projetapi.entity.Participant;
import projetapi.entity.Tache;
import projetapi.repository.TacheRepository;
import projetapi.service.ParticipantServiceProxy;
import projetapi.service.TacheService;
import projetapi.utility.AutorisationAcces;
import projetapi.utility.DateUtilitaire;
import projetapi.utility.EtatTache;

/**
 * Classe de controle du service Tache permettant d'exposer les ressources du service
 * @author anais
 *
 */
@RestController
@RequestMapping(value = "/taches", produces = MediaType.APPLICATION_JSON_VALUE)
public class TacheController {
	// Permet d'utiliser les requête de participant dans le contexte des tâches

	/**
	 * Acces au service Participant
	 */
	ParticipantServiceProxy participantServiceProxy;
	/**
	 * Acces au requêtes du service Tache
	 */
	TacheService tacheService;

	/**
	 * Constructeur du controlleur du service Tache
	 * @param participantServiceProxy service d'utilisation des requetes de l'API participant
	 * @param tacheRepository repository d'acces aux informations des taches
	 */
	public TacheController(ParticipantServiceProxy participantServiceProxy, TacheRepository tacheRepository) {
		this.participantServiceProxy = participantServiceProxy;
		this.tacheService = new TacheService(tacheRepository);
	}


	/**
	 * Requete qui retourne l'ensemble des taches
	 * @return ResponseEntity
	 */
	@GetMapping
	public ResponseEntity<?> getAllTaches() {
		return tacheService.getAllTaches();
	}
	

	/**
	 * Requete qui retourne une tache particuliere
	 * @param id identifiant de la tache
	 * @param token token de verification de l'acces a la tache
	 * @return ResponseEntity
	 */
	@GetMapping(value = "/{tacheId}")
	public ResponseEntity<?> getTache(@PathVariable("tacheId") String id,@RequestHeader(value="token") String token) {
		AutorisationAcces autorisation = tacheService.verificationAutorisationAcces(id,token);
		switch (autorisation) {
			case REFUSE : // Acces refuse
				return new ResponseEntity<>("Acces refusé",HttpStatus.FORBIDDEN);
			case AUTORISE : //Acces autorise
				break;
			case INCONNU : // Tache non trouvee
				return new ResponseEntity<>("Tache inconnue",HttpStatus.NOT_FOUND);
		}
		return tacheService.getTache(id);

	}
	
	/**
	 * Requete permettant de rechercher les taches selon leur etat
	 * @param statut statut a prendre en compte
	 * @return ResponseEntity
	 */
	@RequestMapping(params = "statut")
	public ResponseEntity<?> getTacheByEtat(@RequestParam("statut") String statut) {
		return tacheService.getTacheByEtat(statut);
	}

	/**
	 * Requete permettant de recherche les taches selon leur responsable
	 * @param responsable responsable à prendre en compte
	 * @return ResponseEntity
	 */
	@RequestMapping(params = "responsable")
	public ResponseEntity<?> getTacheByNomresponsable(@RequestParam("responsable") String responsable) {
		return tacheService.getTacheByNomresponsable(responsable);
	}
	

	/**
	 * Requete d'ajout d'une tache au service
	 * @param tache tache a ajouter
	 * @return ResponseEntity 
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> saveTache(@RequestBody Tache tache) {
		return tacheService.saveTache(tache);
	}

	/**
	 * Requete qui change l'etat d'une tache pour l'achever
	 * @param id identifiant de la tache
	 * @token token qui autorise l'acces a la tache pour sa modification
	 * @return ResponseEntity
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{tacheId}")
	public ResponseEntity<?> deleteTache(@PathVariable("tacheId") String id, @RequestHeader(value="token") String token) {
		AutorisationAcces autorisation = tacheService.verificationAutorisationAcces(id,token);
		switch (autorisation) {
			case REFUSE : // Acces refuse
				return new ResponseEntity<>("Acces refusé",HttpStatus.FORBIDDEN);
			case AUTORISE : //Acces autorise
				break;
			case INCONNU : // Tache non trouvee
				return new ResponseEntity<>("Tache inconnue",HttpStatus.NOT_FOUND);
		}
		return tacheService.deleteTache(id);
	}

	/**
	 * Requete qui permet de modifier la date de fin d'une tache
	 * @param id id de la tache à modifier 
	 * @param dateFin nouvelle date de fin a ajouter
	 * @param token token d'acces a la tache
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{tacheId}", params="dateFin")
	public ResponseEntity<?> updateTacheDateFin(@PathVariable("tacheId") String id,@RequestParam("dateFin") String dateFin,
			@RequestHeader(value="token") String token) {
		AutorisationAcces autorisation = tacheService.verificationAutorisationAcces(id,token);
		switch (autorisation) {
			case REFUSE : // Acces refuse
				return new ResponseEntity<>("Acces refusé",HttpStatus.FORBIDDEN);
			case AUTORISE : //Acces autorise
				break;
			case INCONNU : // Tache non trouvee
				return new ResponseEntity<>("Tache inconnue",HttpStatus.NOT_FOUND);
		}
		if(!DateUtilitaire.verifDate(dateFin)) {
			return new ResponseEntity<>("Format de date invalide. Celui-ci doit être dd-MM-yyyy",HttpStatus.BAD_REQUEST);
		}
		return tacheService.updateTacheDateFin(DateUtilitaire.convertDate(dateFin), id);
	}
	
	

	
	
	/**
	 * Requete de recherche de tous les participants d'une tache
	 * @param tacheId identifiant de la tache a rechercher
	 * @return ResponseEntity
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{tacheId}/participants")
	public ResponseEntity<?> getParticipantsByTache(@PathVariable("tacheId") String tacheId,@RequestHeader(value="token") String token) {
		AutorisationAcces autorisation = tacheService.verificationAutorisationAcces(tacheId,token);
		switch (autorisation) {
			case REFUSE : // Acces refuse
				return new ResponseEntity<>("Acces refusé",HttpStatus.FORBIDDEN);
			case AUTORISE : //Acces autorise
				break;
			case INCONNU : // Tache non trouvee
				return new ResponseEntity<>("Tache inconnue",HttpStatus.NOT_FOUND);
		}
		return participantServiceProxy.getParticipantsByTache(tacheId);
	}

	/**
	 * Requete de recherche d'un participant d'une tache
	 * @param tacheId identifiant de la tache a rechercher
	 * @param participantId identifiant du participant a rechercher
	 * @return ResponseEntity
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{tacheId}/participants/{participantId}")
	public ResponseEntity<?> getParticipantByTache(@PathVariable("tacheId") String tacheId,
			@PathVariable("participantId") String participantId,@RequestHeader(value="token") String token) {
		AutorisationAcces autorisation = tacheService.verificationAutorisationAcces(tacheId,token);
		switch (autorisation) {
			case REFUSE : // Acces refuse
				return new ResponseEntity<>("Acces refusé",HttpStatus.FORBIDDEN);
			case AUTORISE : //Acces autorise
				break;
			case INCONNU : // Tache non trouvee
				return new ResponseEntity<>("Tache inconnue",HttpStatus.NOT_FOUND);
		}
		
		ResponseEntity<?> response;
		try {
			response = participantServiceProxy.getParticipantByTacheAndId(tacheId, participantId);
		} catch (feign.FeignException e) {
			response = new ResponseEntity<>("Participant inconnu pour cette tâche",HttpStatus.NOT_FOUND);
		}
		
		return response;
	}


	/**
	 * Requete d'ajout d'un participant à une tache
	 * @param tacheId identifiant de la tache pour laquelle ajouter un participant
	 * @param participant participant a ajouter a la tache
	 * @return ResponseEntity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{tacheId}/participants")
	protected ResponseEntity<?> newParticipantTache(@PathVariable("tacheId") String tacheId, @RequestBody Participant participant,
			@RequestHeader(value="token") String token) {
		
		AutorisationAcces autorisation = tacheService.verificationAutorisationAcces(tacheId,token);
		switch (autorisation) {
			case REFUSE : // Acces refuse
				return new ResponseEntity<>("Acces refusé",HttpStatus.FORBIDDEN);
			case AUTORISE : //Acces autorise
				break;
			case INCONNU : // Tache non trouvee
				return new ResponseEntity<>("Tache inconnue",HttpStatus.NOT_FOUND);
		}
		
		Participant saved = participantServiceProxy.newParticipant(tacheId, participant);
		HttpHeaders responseHeader = new HttpHeaders();
		responseHeader.setLocation(linkTo(TacheController.class).slash(tacheId).slash("participants").slash(saved.getId()).toUri());

		Tache tache = tacheService.tacheRepository.getOne(tacheId);
		Set<String> idParticipants = tache.getParticipantsId();
		idParticipants.add(saved.getId());
		tache.setParticipantsId(idParticipants);
		tache.setEtat(EtatTache.ENCOURS.getEtat()); 
		// On ajoute un participant donc on s'assure que la tache est dans l'état 2 EN COURS
		tacheService.updateTache(tache, tacheId);
		
		return new ResponseEntity<>(null, responseHeader, HttpStatus.CREATED);
	}

	
	/**
	 * Requete de suppression d'un participant d'une tache
	 * @param tacheId identifiant de la tache pour laquelle supprimer un participant
	 * @param participantId identiffiant du participant a retirer
	 * @return ResponseEntity
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "{tacheId}/participants/{participantId}")
	protected ResponseEntity<?> deleteParticipantTache(@PathVariable("tacheId") String tacheId,
			@PathVariable("participantId") String participantId,
			@RequestHeader(value="token") String token) {
		
		AutorisationAcces autorisation = tacheService.verificationAutorisationAcces(tacheId,token);
		switch (autorisation) {
			case REFUSE : // Acces refuse
				return new ResponseEntity<>("Acces refusé",HttpStatus.FORBIDDEN);
			case AUTORISE : //Acces autorise
				break;
			case INCONNU : // Tache non trouvee
				return new ResponseEntity<>("Tache inconnue",HttpStatus.NOT_FOUND);
		}
		
		ResponseEntity<?> response;

		Tache tache = tacheService.tacheRepository.getOne(tacheId);
		// On s'assure tout d'abord que la tâche ne soit pas achevée
		if (tache.getEtat().equals(EtatTache.ACHEVEE.getEtat())) {
			response = new ResponseEntity<>("Impossible de modifier une tâche achevée", HttpStatus.BAD_REQUEST);
		} else {
			// On vérifie ensuite le nombre de participant
			Set<String> idParticipants = tache.getParticipantsId();

			if (idParticipants.size() > 1) { // Il reste plus d'un partcipant donc on peut supprimer
				response = participantServiceProxy.deleteParticipant(participantId);
				idParticipants.remove(participantId);
				tache.setParticipantsId(idParticipants);
				tacheService.updateTache(tache, tacheId);
				response = new ResponseEntity<>(HttpStatus.OK);
			} else { // Interdication de supprimer le dernier participant puisque la tâche est en
						// cours
				response = new ResponseEntity<>("Interdiction de supprimer le dernier participant de cette tâche",
						HttpStatus.BAD_REQUEST);
			}
		}
		return response;

	}

}
