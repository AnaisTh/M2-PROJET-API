package projetapi.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projetapi.entity.Participant;
import projetapi.repository.ParticipantRepository;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = "/participants", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Participant.class)
public class ParticipantController {

	ParticipantRepository participantRepository;
   
 
   public ParticipantController(ParticipantRepository repository) {
		this.participantRepository =repository;
	}
	

    // GET all participants
  	@GetMapping
    public ResponseEntity<?> getAllParticipants() {
  		Iterable<Participant> allParticipants = participantRepository.findAll();
        return new ResponseEntity<>(participantToResource(allParticipants), HttpStatus.OK);
    }

    // GET one participant
    @GetMapping(value = "/{participantId}")
    public ResponseEntity<?> getParticipant(@PathVariable("participantId") String id) {
    	 return Optional.ofNullable(participantRepository.findById(id))
	                .filter(Optional::isPresent)
	                .map(i -> new ResponseEntity<>(participantToResource(i.get(), true), HttpStatus.OK))
	                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
    
    // GET all participants of tache
    @GetMapping(value = "tache/{tacheId}")
    public ResponseEntity<?> getParticipantsByTache(@PathVariable("tacheId") String tacheId){
    	Iterable<Participant> allParticipants = participantRepository.findByTacheid(tacheId);
    	return new ResponseEntity<>(participantToResource(allParticipants), HttpStatus.OK);
    }
    
    //GET one participant of on tache
    @GetMapping(value = "/{participantId}/tache/{tacheId}")
    public ResponseEntity<?> getParticipantByTacheAndId(@PathVariable("tacheId") String tacheId, @PathVariable("participantId") String participantId) {
    	return Optional.ofNullable(participantRepository.findByTacheidAndId(tacheId, participantId))
                .filter(Optional::isPresent)
                .map(i -> new ResponseEntity<>(participantToResource(i.get(), true), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    
    // POST
    @PostMapping
    public Participant newParticipant(@RequestBody Participant participant) {
    	participant.setId(UUID.randomUUID().toString());
        Participant saved = participantRepository.save(participant);
        return saved;
        
    
    }

    // DELETE
    @DeleteMapping(value = "/{participantId}")
    public ResponseEntity<?> deleteParticipant(@PathVariable("participantId") String id) {
    	Optional<Participant> participant = participantRepository.findById(id);
        if (participant.isPresent()) {
        	participantRepository.delete(participant.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    //UPDATE
    @PutMapping("/{participantId}")
    protected ResponseEntity<?> updateParticipant(@PathVariable("participantId") String id, @RequestBody Participant participantNew){
    	Optional<Participant> participant = participantRepository.findById(id);
		if(!participant.isPresent()) {
			new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		participantNew.setId(id);
        Participant saved = participantRepository.save(participantNew);
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.setLocation(linkTo(ParticipantController.class).slash(saved.getId()).toUri());
        return new ResponseEntity<>(null, responseHeader, HttpStatus.NO_CONTENT);
	}
    
    
    
    
    
    private Resources<Resource<Participant>> participantToResource(Iterable<Participant> participants) {
        Link selfLink = linkTo(methodOn(ParticipantController.class).getAllParticipants()).withSelfRel();
        List<Resource<Participant>> participantRessources = new ArrayList();
        participants.forEach(participant
                -> participantRessources.add(participantToResource(participant, false)));
        return new Resources<>(participantRessources, selfLink);
    }
    
    private Resource<Participant> participantToResource(Participant participant, Boolean collection) {
        Link selfLink = linkTo(ParticipantController.class)
                .slash(participant.getId())
                .withSelfRel();
        if (collection) {
            Link collectionLink = linkTo(methodOn(ParticipantController.class).getAllParticipants())
                    .withSelfRel();
            return new Resource<>(participant, selfLink, collectionLink);
        } else {
            return new Resource<>(participant, selfLink);
        }
    }
	
    
    
     
}
