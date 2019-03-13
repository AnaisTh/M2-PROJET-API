package projetapi.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import projetapi.utility.EtatTache;

/**
 * Classe permettant de gerer la partie service propre aux taches
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
		tache.setDatecreation(LocalDate.now());
		tache.setTokenConnexion((UUID.randomUUID().toString() + UUID.randomUUID().toString()).substring(20, 40));
		
		// On vérifie que la date de fin est postérieure à la date de début
		if (tache.getDatecreation().compareTo(tache.getDateecheance()) >= 0) { // Date de création supérieure à
																				// l'échéance
			return new ResponseEntity<>("La date d'échéance de la date doit être ultérieure à la date du jour",
					HttpStatus.BAD_REQUEST);
		} else {
			tache.setEtat(EtatTache.CREEE.getEtat()); // On instancie au 1er état, créé
			Tache saved = tacheRepository.save(tache);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setLocation(linkTo(TacheController.class).slash(saved.getId()).toUri());
			return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
		}
	}

	/**
	 * Requete de cloture d'une tache du service
	 * @param id identifiant de la tache a cloturer
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> deleteTache(@PathVariable("tacheId") String id) {
		Optional<Tache> tacheOptional = tacheRepository.findById(id);
		if (tacheOptional.isPresent()) {
			// tacheRepository.delete(tache.get()); //On ne la delete pas vraiment mais
			// uniquement un changement d'état
			Tache tache = tacheOptional.get();
			tache.setEtat(EtatTache.ACHEVEE.getEtat()); // On enregistre l'état 3, achevée
			tacheRepository.save(tache);
		}
		return new ResponseEntity<>(HttpStatus.OK); // OK et non pas NO_CONTENT comme un delete
	}

	/**
	 * Requete de mise a jour d'une tache du service
	 * @param tache nouvelle tache a enregistrer
	 * @param id identifiant de la tache a modifier
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> updateTache(@RequestBody Tache tache, @PathVariable("tacheId") String id) {
		Optional<Tache> body = Optional.ofNullable(tache);
		if (!body.isPresent()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if (!tacheRepository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		tache.setId(id);
		Tache result = tacheRepository.save(tache);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Conversion d'une liste de tache sous forme de ressources
	 * @param taches liste de taches a convertir
	 * @return ensemble de tache sous forme de resource
	 */
	private Resources<Resource<Tache>> tacheToResource(Iterable<Tache> taches) {
		Link selfLink = linkTo(methodOn(TacheController.class).getAllTaches()).withSelfRel();
		List<Resource<Tache>> tacheRessources = new ArrayList();
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

	public int verificationAutorisationAcces(String tacheId, String token) {
		Optional<Tache> tacheOptional = tacheRepository.findById(tacheId);
		if (tacheOptional.isPresent()) {
			Tache tache = tacheOptional.get();
			System.out.println(tache.getTokenconnexion() + " " + token);
			if(tache.getTokenconnexion().equals(token)) {
				return 1;
			}
			else { return 0; }
		}
		else {
			return -1; 
		}
		
	}
}
