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


/**
 * Classe de connexion a l'API des participants et de gestion des services proposes 
 * @author anais
 *
 */
@FeignClient("http://projet-api-participant")
@RequestMapping(value = "/participants")
public interface ParticipantServiceProxy {

	/**
	 * Requete de recherche de l'ensemble des participants
	 * @return ResponseEntity
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/participants")
	ResponseEntity<?> getAllParticipants();

	
	/**
	 * Requete de recherche des participants d'une tache
	 * @param tacheId identifiant de la tache a prendre en compte
	 * @return ResponseEntity
	 */
	@RequestMapping(method = RequestMethod.GET, value = "tache/{tacheId}")
	ResponseEntity<?> getParticipantsByTache(@PathVariable("tacheId") String tacheId);

	
	/**
	 * Requete de recherche d'un participant d'une tache
	 * @param tacheId identifiant de la tache a prendre en compte
	 * @param participantId identifiant du participant recherché
	 * @return ResponseEntity
	 */
	@GetMapping(value = "/{participantId}/tache/{tacheId}")
	ResponseEntity<?> getParticipantByTacheAndId(@PathVariable("tacheId") String tacheId,
			@PathVariable("participantId") String participantId);

	
	/**
	 * Requete d'ajout d'un participant a une tache
	 * @param tacheid identifiant de la tache a laquelle ajouter un participant
	 * @param participant participant a ajouter
	 * @return ResponseEntity
	 */
	@PostMapping("/{tacheid}")
	Participant newParticipant(@PathVariable("tacheid") String tacheid, @RequestBody Participant participant);

	
	/**
	 * Requete de suppression d'un participant
	 * @param id identifiant du participant a supprimer
 	 * @return ResponseEntity
	 */
	@DeleteMapping(value = "/{participantId}")
	ResponseEntity<?> deleteParticipant(@PathVariable("participantId") String id);

}
