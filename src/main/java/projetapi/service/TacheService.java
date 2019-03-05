package projetapi.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import java.time.LocalDate;
import java.util.ArrayList;
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


/*
 * Classe permettant de gérer la partie service propre aux tâches
 */
public class TacheService {

	public TacheRepository tacheRepository;
	
	public TacheService(TacheRepository tacheRepository) {
		this.tacheRepository = tacheRepository;
	}

	public ResponseEntity<?> getAllTaches(){
		Iterable<Tache> allTaches = tacheRepository.findAll();
		return new ResponseEntity<>(tacheToResource(allTaches),HttpStatus.OK);
	}

	public ResponseEntity<?> getTache(@PathVariable("tacheId") String id) {
	    return Optional.ofNullable(tacheRepository.findById(id))
	            .filter(Optional::isPresent)
	            .map(tache -> new ResponseEntity<>(tacheToResource(tache.get(), true), HttpStatus.OK))
	            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	public ResponseEntity<?> getTacheByEtat(String etat) {
		List<Tache> allTaches = tacheRepository.findByEtat(etat);
		return new ResponseEntity<>(tacheToResource(allTaches),HttpStatus.OK);
	}

	public ResponseEntity<?> getTacheByNomresponsable(String responsable) {
		List<Tache> allTaches = tacheRepository.findByNomresponsable(responsable);
		return new ResponseEntity<>(tacheToResource(allTaches),HttpStatus.OK);
	}
	
	public ResponseEntity<?> getTacheByEtatAndNomresponsable(String etat, String responsable) {
		List<Tache> allTaches = tacheRepository.findByNomresponsableAndEtat(responsable, etat);
		return new ResponseEntity<>(tacheToResource(allTaches),HttpStatus.OK);
	}
	
	
	public ResponseEntity<?> saveTache(@RequestBody Tache tache) {
		tache.setId(UUID.randomUUID().toString());
		tache.setDatecreation(LocalDate.now());
		//On vérifie que la date de fin est postérieure à la date de début
		if(tache.getDatecreation().compareTo(tache.getDateecheance()) >= 0) { //Date de création supérieure à l'échéance
			return new ResponseEntity<>("La date d'échéance de la date doit être ultérieure à la date du jour", HttpStatus.BAD_REQUEST);
		}
		else {
			tache.setEtat(Tache.getListeEtats().get(1)); // On instancie au 1er état, créé
			Tache saved = tacheRepository.save(tache);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setLocation(linkTo(TacheController.class).slash(saved.getId()).toUri());
			return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
		}
	}


	public ResponseEntity<?> deleteTache(@PathVariable("tacheId") String id) {
		Optional<Tache> tacheOptional = tacheRepository.findById(id);
		if (tacheOptional.isPresent()) {
			//tacheRepository.delete(tache.get()); //On ne la delete pas vraiment mais uniquement un changement d'état
			Tache tache = tacheOptional.get();
			tache.setEtat(Tache.getListeEtats().get(3)); // On enregistre l'état 3, achevée
			tacheRepository.save(tache);
		}
		return new ResponseEntity<>(HttpStatus.OK); //OK et non pas NO_CONTENT comme un delete
	}
	
	public ResponseEntity<?> updateTache(@RequestBody Tache tache, @PathVariable("tacheId") String id) {
		Optional<Tache> body = Optional.ofNullable( tache);
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

	private Resources<Resource<Tache>> tacheToResource(Iterable<Tache> taches) {
        Link selfLink = linkTo(methodOn(TacheController.class).getAllTaches()).withSelfRel();
        List<Resource<Tache>> tacheRessources = new ArrayList();
        taches.forEach(tache
                -> tacheRessources.add(tacheToResource(tache, false)));
        return new Resources<>(tacheRessources, selfLink);
    }
    
    private Resource<Tache> tacheToResource(Tache tache, Boolean collection) {
        Link selfLink = linkTo(TacheController.class)
                .slash(tache.getId())
                .withSelfRel();
        if (collection) {
            Link collectionLink = linkTo(methodOn(TacheController.class).getAllTaches())
                    .withSelfRel();
            return new Resource<>(tache, selfLink, collectionLink);
        } else {
            return new Resource<>(tache, selfLink);
        }
    }

	

	
}

