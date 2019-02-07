package projetapi.participant;

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
@RequestMapping(value = "/participants", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Participant.class)
public class ParticipantRepresentation {
	
	//Injections de dépendances
	private final ParticipantRessource pr;

	public ParticipantRepresentation(ParticipantRessource pr) {
		this.pr = pr;
	}
	
	
	@GetMapping
	public ResponseEntity<?> getAllParticipants(){
		Iterable<Participant> allParticipants = pr.findAll();
		return new ResponseEntity<>(participantToResource(allParticipants),HttpStatus.OK);
	}
	
	@GetMapping(value = "/{participantId}")
	public ResponseEntity<?> getparticipant(@PathVariable("participantId") String id) {
	    return Optional.ofNullable(pr.findById(id))
	            .filter(Optional::isPresent)
	            .map(participant -> new ResponseEntity<>(participantToResource(participant.get(), true), HttpStatus.OK))
	            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	 
	@PostMapping
	    public ResponseEntity<?> saveparticipant(@RequestBody Participant participant) {
		 	participant.setId(UUID.randomUUID().toString());
		 	Participant saved = pr.save(participant);
	        HttpHeaders responseHeaders = new HttpHeaders();
	        responseHeaders.setLocation(linkTo(ParticipantRepresentation.class).slash(saved.getId()).toUri());
	        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{participantId}")
	public ResponseEntity<?> deleteIntervenant(@PathVariable("participantId") String id) {
		Optional<Participant> participant = pr.findById(id);
		if (participant.isPresent()) {
			pr.delete(participant.get());
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	 
	
	@PutMapping(value = "/{participantId}")
    public ResponseEntity<?> updateInscription(@RequestBody Participant participant,
            @PathVariable("participantId") String id) {
        Optional<Participant> body = Optional.ofNullable( participant);
        if (!body.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!pr.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        participant.setId(id);
        Participant result = pr.save(participant);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
	
	
	private Resources<Resource<Participant>> participantToResource(Iterable<Participant> participants) {
        Link selfLink = linkTo(ParticipantRepresentation.class).withSelfRel();
        List<Resource<Participant>> participantResources = new ArrayList();
        participants.forEach(participant
                -> participantResources.add(participantToResource(participant, false)));
        return new Resources<>(participantResources, selfLink);
    }

    private Resource<Participant> participantToResource(Participant participant, Boolean collection) {
        Link selfLink = linkTo(ParticipantRepresentation.class)
                .slash(participant.getId())
                .withSelfRel();
        if (collection) {
            Link collectionLink = linkTo(ParticipantRepresentation.class).withRel("collection");
            return new Resource<>(participant, selfLink, collectionLink);
        } else {
            return new Resource<>(participant, selfLink);
        }
    }
	
	
	

}
