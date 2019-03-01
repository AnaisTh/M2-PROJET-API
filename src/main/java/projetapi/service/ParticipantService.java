package projetapi.service;

import org.springframework.http.ResponseEntity;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import projetapi.controller.ParticipantController;
import projetapi.entity.Participant;
import projetapi.repository.ParticipantRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class ParticipantService {

	
	ParticipantRepository participantRepository;
	
	public ParticipantService(ParticipantRepository repository) {
		this.participantRepository = repository;
	}
	
	public ResponseEntity<?> getAllParticipants() {
    	Iterable<Participant> allParticipants = participantRepository.findAll();
        return new ResponseEntity<>(participantToResource(allParticipants), HttpStatus.OK);
    }
	
	public ResponseEntity<?> getParticipantById(String id){
		 return Optional.ofNullable(participantRepository.findById(id))
	                .filter(Optional::isPresent)
	                .map(i -> new ResponseEntity<>(participantToResource(i.get(), true), HttpStatus.OK))
	                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	public ResponseEntity<?> newParticipant(Participant participant) {
    	participant.setId(UUID.randomUUID().toString());
        Participant saved = participantRepository.save(participant);
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.setLocation(linkTo(ParticipantController.class).slash(saved.getId()).toUri());
        return new ResponseEntity<>(null, responseHeader, HttpStatus.CREATED);
    }
	
	
		
	private Resources<Resource<Participant>> participantToResource(Iterable<Participant> participants) {
        Link selfLink = linkTo(methodOn(ParticipantController.class).getAllParticipants()).withSelfRel();
        List<Resource<Participant>> participantRessources = new ArrayList();
        participants.forEach(participant
                -> participantRessources.add(participantToResource(participant, false)));
        return new Resources<>(participantRessources, selfLink);
    }
	
	public ResponseEntity<?> delete(String id) {
    	Optional<Participant> participant = participantRepository.findById(id);
        if (participant.isPresent()) {
        	participantRepository.delete(participant.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	

	public ResponseEntity<?> updateParticipant(String id, Participant participantNew) {
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
