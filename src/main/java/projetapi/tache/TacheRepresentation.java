package projetapi.tache;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/taches", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Tache.class)
public class TacheRepresentation {
	
	//Injections de dépendances
	private final TacheRessource tr;

	public TacheRepresentation(TacheRessource tr) {
		this.tr = tr;
	}
	
	
	@GetMapping
	public ResponseEntity<?> getAllTaches(){
		Iterable<Tache> allTaches = tr.findAll();
		return new ResponseEntity<>(tacheToResource(allTaches),HttpStatus.OK);
	}
	
	@GetMapping(value = "/{tacheId}")
	public ResponseEntity<?> getTache(@PathVariable("tacheId") String id) {
	    return Optional.ofNullable(tr.findById(id))
	            .filter(Optional::isPresent)
	            .map(tache -> new ResponseEntity<>(tacheToResource(tache.get(), true), HttpStatus.OK))
	            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	 
	@PostMapping
	    public ResponseEntity<?> saveTache(@RequestBody Tache tache) {
		 	tache.setId(UUID.randomUUID().toString());
		 	Tache saved = tr.save(tache);
	        HttpHeaders responseHeaders = new HttpHeaders();
	        responseHeaders.setLocation(linkTo(TacheRepresentation.class).slash(saved.getId()).toUri());
	        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{tacheId}")
	public ResponseEntity<?> deleteIntervenant(@PathVariable("tacheId") String id) {
		Optional<Tache> tache = tr.findById(id);
		if (tache.isPresent()) {
			tr.delete(tache.get());
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	 
	
	@PutMapping(value = "/{tacheId}")
    public ResponseEntity<?> updateInscription(@RequestBody Tache tache,
            @PathVariable("tacheId") String id) {
        Optional<Tache> body = Optional.ofNullable( tache);
        if (!body.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!tr.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        tache.setId(id);
        Tache result = tr.save(tache);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
	
	
	private Resources<Resource<Tache>> tacheToResource(Iterable<Tache> taches) {
        Link selfLink = linkTo(TacheRepresentation.class).withSelfRel();
        List<Resource<Tache>> tacheResources = new ArrayList();
        taches.forEach(tache
                -> tacheResources.add(tacheToResource(tache, false)));
        return new Resources<>(tacheResources, selfLink);
    }

    private Resource<Tache> tacheToResource(Tache tache, Boolean collection) {
        Link selfLink = linkTo(TacheRepresentation.class)
                .slash(tache.getId())
                .withSelfRel();
        if (collection) {
            Link collectionLink = linkTo(TacheRepresentation.class).withRel("collection");
            return new Resource<>(tache, selfLink, collectionLink);
        } else {
            return new Resource<>(tache, selfLink);
        }
    }
	
	
	

}
