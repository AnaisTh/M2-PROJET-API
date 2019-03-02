package projetapi.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import projetapi.controller.TacheController;
import projetapi.entity.Tache;
import projetapi.repository.TacheRepository;

public class TacheService {

	TacheRepository tacheRepository;
	
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

		
		public ResponseEntity<?> saveTache(@RequestBody Tache tache) {
			tache.setId(UUID.randomUUID().toString());
			Tache saved = tacheRepository.save(tache);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setLocation(linkTo(TacheController.class).slash(saved.getId()).toUri());
			return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
		}


		public ResponseEntity<?> deleteIntervenant(@PathVariable("tacheId") String id) {
			Optional<Tache> tache = tacheRepository.findById(id);
			if (tache.isPresent()) {
				tacheRepository.delete(tache.get());
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		public ResponseEntity<?> updateInscription(@RequestBody Tache tache, @PathVariable("tacheId") String id) {
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

