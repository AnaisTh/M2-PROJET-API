package projetapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projetapi.entity.Participant;
import projetapi.repository.ParticipantRepository;
import projetapi.service.ParticipantService;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = "/participants", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Participant.class)
public class ParticipantController {

   ParticipantService participantService;
   
   public ParticipantController(ParticipantService participantService) {
		super();
		this.participantService = participantService;
	}

    // GET all
  	@GetMapping
    public ResponseEntity<?> getAllParticipants() {
        return participantService.getAllParticipants();
    }

    // GET one
    @GetMapping(value = "/{participantId}")
    public ResponseEntity<?> getParticipant(@PathVariable("participantId") String id) {
        return participantService.getParticipantById(id);
    }
    
    // POST
    @PostMapping
    public ResponseEntity<?> newParticipant(@RequestBody Participant participant) {
    	return participantService.newParticipant(participant);
    }

    // DELETE
    @DeleteMapping(value = "/{participantId}")
    public ResponseEntity<?> deleteParticipant(@PathVariable("participantId") String id) {
        return participantService.delete(id);
    }
    
    //UPDATE
    @PutMapping("/{participantId}")
    protected ResponseEntity<?> updateParticipant(@PathVariable("participantId") String id, @RequestBody Participant participant){
    	return participantService.updateParticipant(id, participant);
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
