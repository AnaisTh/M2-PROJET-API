package projetapi.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import projetapi.service.ParticipantServiceProxy;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(value = "/taches", produces = MediaType.APPLICATION_JSON_VALUE)
public class TacheController {
	//Permet d'utiliser les requête de participant dans le contexte des tâches

	ParticipantServiceProxy participantServiceProxy;
	

	public TacheController(ParticipantServiceProxy participantServiceProxy) {
		this.participantServiceProxy = participantServiceProxy;
	}
	
	//Tous les participants d'une tache
	@RequestMapping(method = RequestMethod.GET, value = "{tacheId}/participants")
	public ResponseEntity<?> getParticipantsByTache(@PathVariable("tacheId") String tacheId){
		return participantServiceProxy.getParticipantsByTache(tacheId);
	}
	
	//Un participant d'une tache
	@RequestMapping(method = RequestMethod.GET, value = "{tacheId}/participants/{participantId}")
	public ResponseEntity<?> getParticipantByTache(@PathVariable("tacheId") String tacheId, @PathVariable("participantId") String participantId){
		return participantServiceProxy.getParticipantByTacheAndId(tacheId,participantId);
	}
	
	/*
	
	// Recuperation des participants de chaque tache

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

    */

}
