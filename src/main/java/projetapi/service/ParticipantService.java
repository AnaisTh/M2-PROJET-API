package projetapi.service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import projetapi.controller.TacheController;
import projetapi.entity.Participant;
import projetapi.entity.Tache;
import projetapi.utility.EtatTache;

/**
 * Classe permettant de gerer la partie service propre aux participants
 * @author anais
 *
 */
public class ParticipantService {
	
	/**
	 * Acces au service Participant
	 */
	ParticipantServiceProxy participantServiceProxy;

	/**
	 * Constructeur 
	 * @param participantServiceProxy connexion au service participant
	 */
	public ParticipantService(ParticipantServiceProxy participantServiceProxy) {
		this.participantServiceProxy = participantServiceProxy;
	}
	
	
	/**
	 * Requete permettant de recuperer l'ensemble des participants d'une tache
	 * @param tacheId tache recherchee
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> getParticipantsByTache(String tacheId){
		return participantServiceProxy.getParticipantsByTache(tacheId);
	}
	
	/**
	 * Requete permettant de recuperer un participant d'une tache
	 * @param tacheId tache recherchee
	 * @param participantId participant recherche
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> getParticipantByTache(String tacheId, String participantId){
		ResponseEntity<?> response;
		try {
			response = participantServiceProxy.getParticipantByTacheAndId(tacheId, participantId);
		} catch (feign.FeignException e) {
			response = new ResponseEntity<>("Participant inconnu pour cette tâche",HttpStatus.NOT_FOUND);
		}
		return response;
	}
	/**
	 * Requete d'ajout d'un participant à une tache
	 * @param tacheId identifiant de la tache pour laquelle ajouter un participant
	 * @param participant participant a ajouter a la tache
	 * @return ResponseEntity
	 */
	public ResponseEntity<?> newParticipantTache(String tacheId,Participant participant ){
		Participant saved = participantServiceProxy.newParticipant(tacheId, participant);
		HttpHeaders responseHeader = new HttpHeaders();
		responseHeader.setLocation(linkTo(TacheController.class).slash(tacheId).slash("participants").slash(saved.getId()).toUri());
		
		return new ResponseEntity<>(tacheId, responseHeader, HttpStatus.CREATED);
	}

	public ResponseEntity<?> deleteParticipantTache(String participantId) {
		return participantServiceProxy.deleteParticipant(participantId);
	}
	
	
}
