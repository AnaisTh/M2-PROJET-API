package projetapi.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import projetapi.controller.TacheController;
import projetapi.entity.Tache;
import projetapi.repository.TacheRepository;
import projetapi.utility.AutorisationAcces;
import projetapi.utility.EtatTache;

/**
 * Classe permettant de gerer la partie service propre aux taches, de réaliser les différentes requêtes
 * @author anais
 *
 */
public class TacheService {

	/**
	 * Acces au repository des taches
	 */
	public TacheRepository tacheRepository;

	public TacheService(TacheRepository tacheRepository) {
		this.tacheRepository = tacheRepository;
	}

	/**
	 * Requete d'acces a l'ensemble des taches
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> getAllTaches() {
		Iterable<Tache> allTaches = tacheRepository.findAll();
		return new ResponseEntity<>(tacheToResource(allTaches), HttpStatus.OK);
	}
	
	/**
	 * Requete d'acces aux taches selon leur etat
	 * @param etat etat recherche
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> getTacheByEtat(String etat) {
		List<Tache> allTaches = tacheRepository.findByEtat(etat);
		return new ResponseEntity<>(tacheToResource(allTaches), HttpStatus.OK);
	}

	/**
	 * Requete d'acces aux taches selon leur responsable
	 * @param responsable responsable a prendre en compte
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> getTacheByNomresponsable(String responsable) {
		List<Tache> allTaches = tacheRepository.findByNomresponsable(responsable);
		return new ResponseEntity<>(tacheToResource(allTaches), HttpStatus.OK);
	}
	
	/**
	 * Requete d'acces aux taches selon leur responsable et leur etat
	 * @param etat etat a prendre en compte
	 * @param responsable responsable a prendre en compte
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> getTacheByEtatAndNomresponsable(String etat, String responsable) {
		List<Tache> allTaches = tacheRepository.findByNomresponsableAndEtat(responsable, etat);
		return new ResponseEntity<>(tacheToResource(allTaches), HttpStatus.OK);
	}

	/**
	 * Requete d'acces a une tache
	 * @param id identifiant de la tache a rechercher
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> getTache(@PathVariable("tacheId") String id) {
		return Optional.ofNullable(tacheRepository.findById(id)).filter(Optional::isPresent)
				.map(tache -> new ResponseEntity<>(tacheToResource(tache.get(), true), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	
	/**
	 * Requete de sauvegarde d'une tache dans le service
	 * @param tache tache a enregitrer
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> saveTache(@RequestBody Tache tache) {
		tache.setId(UUID.randomUUID().toString());
		tache.setDatecreation(new Date());
		tache.setTokenConnexion((UUID.randomUUID().toString() + UUID.randomUUID().toString()).substring(20, 40));
		tache.setParticipantsId(new HashSet<String>());
		
		// On vérifie que la date de fin est postérieure à la date de début
		if (tache.getDatecreation().compareTo(tache.getDateecheance()) >= 0) { 
			// Date de création supérieure à l'échéance
			return new ResponseEntity<>("La date d'échéance de la date doit être ultérieure à la date du jour",
					HttpStatus.BAD_REQUEST);
		} else {
			tache.setEtat(EtatTache.CREEE.getEtat()); // On instancie au 1er état, créé
			Tache saved = tacheRepository.save(tache);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setLocation(linkTo(TacheController.class).slash(saved.getId()).toUri());
			return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
		}
	}

	/**
	 * Requete de cloture d'une tache du service si elle est en cours ou créée, et de l'archiveée si elle est cloturée.
	 * @param id identifiant de la tache a cloturer
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> deleteTache(@PathVariable("tacheId") String id) {
		Optional<Tache> tacheOptional = tacheRepository.findById(id);
		if (tacheOptional.isPresent()) {
			// tacheRepository.delete(tache.get());  //On ne la delete pas vraiment mais uniquement un changement d'état
			Tache tache = tacheOptional.get();
			if(tache.getEtat().equals(EtatTache.CREEE.getEtat())|| tache.getEtat().equals(EtatTache.ENCOURS.getEtat())) {
				tache.setEtat(EtatTache.ACHEVEE.getEtat()); 
				tacheRepository.save(tache);
				return new ResponseEntity<>(tache,HttpStatus.OK); // OK et non pas NO_CONTENT comme un delete
			}
			else if(tache.getEtat().equals(EtatTache.ACHEVEE.getEtat())) {
				tache.setEtat(EtatTache.ARCHIVEE.getEtat()); 
				tacheRepository.save(tache);
				return new ResponseEntity<>(tache,HttpStatus.OK); // OK et non pas NO_CONTENT comme un delete
			}
			else {
				return new ResponseEntity<>("Impossible d'archivée la tâche. Celle-ci est deja archivée",HttpStatus.BAD_REQUEST); // OK et non pas NO_CONTENT comme un delete
			}
			
		}
		return new ResponseEntity<>("Aucune tâche à achever",HttpStatus.NO_CONTENT); // OK et non pas NO_CONTENT comme un delete
		
	}

	
	/**
	 * Methode permettant de mettre a jour la date de fin d'une tache
	 * @param nouvelleDate nouvelle date a prendre en compte
	 * @param id identifiant de la tache a modifier
	 * @return ResponseEntity
	 */

	public ResponseEntity<?> updateTacheDateFin(Date nouvelleDate, String id){
		if (!tacheRepository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else {
			Tache tache = tacheRepository.getOne(id);
			
			if(tache.getEtat().equals(EtatTache.ACHEVEE.getEtat()) || tache.getEtat().equals(EtatTache.ARCHIVEE.getEtat())) {
				return new ResponseEntity<>("Impossible de modifier une tâche achevée ou archivée", HttpStatus.BAD_REQUEST);
			}
			else if(nouvelleDate.before(tache.getDatecreation()) || nouvelleDate.before(new Date())) {
				return new ResponseEntity<>("La date est invalide. Impossible d'avoir une date de "
						+ "fin antérieure à la date de création ou à la date du jour",HttpStatus.BAD_REQUEST);
			}
			else {
				tache.setDateecheance(nouvelleDate);
				tacheRepository.save(tache).getDateecheance();
				return new ResponseEntity<>(tache,HttpStatus.OK);
			}
			
		}
	}
	
	/**
	 * Méthode permettant de verifier si l'acces a une tache est autorise
	 * @param tacheId
	 * @param token
	 * @return une autorisation d'accès
	 */
	public AutorisationAcces verificationAutorisationAcces(String tacheId, String token) {
		Optional<Tache> tacheOptional = tacheRepository.findById(tacheId);
		if (tacheOptional.isPresent()) {
			Tache tache = tacheOptional.get();
			if(tache.getTokenconnexion().equals(token)) {
				return AutorisationAcces.AUTORISE;
			}
			else { return AutorisationAcces.REFUSE; }
		}
		else {
			return AutorisationAcces.INCONNU; 
		}
		
		
	}
	/**
	 * Methode permettant l'ajout d'une reference d'un participant a une tache
	 * @param tacheId tache concernee
	 * @param participantId id du participant a ajouter
	 */
	public void ajoutParticipantTache(String tacheId, String participantId) {
		Tache tache = tacheRepository.getOne(tacheId);
		Set<String> idParticipants = tache.getParticipantsId();
		idParticipants.add(participantId);
		tache.setParticipantsId(idParticipants);
		tache.setEtat(EtatTache.ENCOURS.getEtat()); 
		tacheRepository.save(tache);

	}
	
	public void retraitParticipantTache(String tacheId, String participantId) {
		Tache tache = tacheRepository.getOne(tacheId);
		Set<String> idParticipants = tache.getParticipantsId();
		idParticipants.remove(participantId);
		tache.setParticipantsId(idParticipants);
		tacheRepository.save(tache);
	}

	/**
	 * Conversion d'une liste de tache sous forme de ressources
	 * @param taches liste de taches a convertir
	 * @return ensemble de tache sous forme de resource
	 */
	private Resources<Resource<Tache>> tacheToResource(Iterable<Tache> taches) {
		Link selfLink = linkTo(methodOn(TacheController.class).getAllTaches()).withSelfRel();
		List<Resource<Tache>> tacheRessources = new ArrayList<Resource<Tache>>();
		taches.forEach(tache -> tacheRessources.add(tacheToResource(tache, false)));
		return new Resources<>(tacheRessources, selfLink);
	}

	/**
	 * Conversin d'une tache sous forme de ressource
	 * @param tache tache a convertire
	 * @param collection precise s'il s'agit d'une collection
	 * @return tache sous forme de ressource
	 */
	private Resource<Tache> tacheToResource(Tache tache, Boolean collection) {
		Link selfLink = linkTo(TacheController.class).slash(tache.getId()).withSelfRel();
		if (collection) {
			Link collectionLink = linkTo(methodOn(TacheController.class).getAllTaches()).withSelfRel();
			return new Resource<>(tache, selfLink, collectionLink);
		} else {
			return new Resource<>(tache, selfLink);
		}
	}

	

	
}
