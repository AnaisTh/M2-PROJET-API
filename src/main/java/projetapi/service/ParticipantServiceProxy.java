package projetapi.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import projetapi.entity.Participant;

//Proxy de connexion à l'API des participants

@FeignClient("http://projet-api-participant")
@RequestMapping(value = "/participants")
public interface ParticipantServiceProxy {
	
	@RequestMapping(method = RequestMethod.GET, value = "/participants")
	ResponseEntity<?> getAllParticipants();

	//Get all participants of tache
	@RequestMapping(method = RequestMethod.GET, value = "tache/{tacheId}")
	ResponseEntity<?> getParticipantsByTache(@PathVariable("tacheId") String tacheId);
	
	//Get one participant of one tache
	@GetMapping(value = "/{participantId}/tache/{tacheId}")
    ResponseEntity<?> getParticipantByTacheAndId(@PathVariable("tacheId") String tacheId, @PathVariable("participantId") String participantId);

	//Ajout d'un participant	
	@PostMapping
    Participant newParticipant(@RequestBody Participant participant);
	
	/*
	@RequestMapping(method = RequestMethod.GET, value = "/participants")
	ResponseEntity<?> getAllParticipants();

    @RequestMapping(method = RequestMethod.GET, value = "/participants/{participantId}")
    ResponseEntity<?> getParticipant(@PathVariable("participantId") String id);

    @RequestMapping(method = RequestMethod.POST, value = "/participants")
    ResponseEntity<?> newParticipant(@RequestBody Participant participant);
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/participants/{participantId}")
    ResponseEntity<?> deleteParticipant(@PathVariable("participantId") String id);
        
    @RequestMapping(method = RequestMethod.PUT, value = "/participants/{participantId}")
    ResponseEntity<?> updateParticipant(@PathVariable("participantId") String id, @RequestBody Participant participantNew);
        

    */
    
    
}
