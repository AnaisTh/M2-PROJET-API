package projetapi.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import projetapi.entity.Participant;

/*
 * Classe de connexion à l'API des participants et de gestion des services proposés 
 */
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

	//Ajout d'un participant à une tâche
	 @PostMapping("/{tacheid}")
	 Participant newParticipant(@PathVariable("tacheid") String tacheid, @RequestBody Participant participant);

	 //Suppression d'un participant d'une tâche
	 @DeleteMapping(value = "/{participantId}")
	 ResponseEntity<?> deleteParticipant(@PathVariable("participantId") String id);
	 
	    
}
