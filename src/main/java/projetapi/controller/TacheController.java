package projetapi.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import projetapi.entity.Participant;
import projetapi.service.ParticipantServiceProxy;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(value = "/taches", produces = MediaType.APPLICATION_JSON_VALUE)
public class TacheController {
	//Permet d'utiliser les requête de participant dans le contexte des tâches

	ParticipantServiceProxy participantServiceProxy;
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/participants")
	public ResponseEntity<?> getAllParticipants(){
		return participantServiceProxy.getAllParticipants();
	}

    @RequestMapping(method = RequestMethod.GET, value = "/participants/{participantId}")
    public ResponseEntity<?> getParticipant(@PathVariable("participantId") String id){
		return participantServiceProxy.getParticipant(id);
	}
    
    
    @RequestMapping(method = RequestMethod.POST, value = "/participants")
    public ResponseEntity<?> newParticipant(@RequestBody Participant participant){
    	return participantServiceProxy.newParticipant(participant);
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/participants/{participantId}")
    public ResponseEntity<?> deleteParticipant(@PathVariable("participantId") String id){
    	return participantServiceProxy.deleteParticipant(id);
    }
        
    @RequestMapping(method = RequestMethod.PUT, value = "/participants/{participantId}")
    ResponseEntity<?> updateParticipant(@PathVariable("participantId") String id, @RequestBody Participant participantNew){
    	return participantServiceProxy.updateParticipant(id, participantNew);
    }
}
