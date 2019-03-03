package projetapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Optional;
import java.util.Set;

import projetapi.entity.Participant;
import projetapi.entity.Tache;
import projetapi.repository.TacheRepository;
import projetapi.service.ParticipantServiceProxy;
import projetapi.service.TacheService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(value = "/taches", produces = MediaType.APPLICATION_JSON_VALUE)
public class TacheController {
	//Permet d'utiliser les requête de participant dans le contexte des tâches

	ParticipantServiceProxy participantServiceProxy;
	TacheService tacheService;

	public TacheController(ParticipantServiceProxy participantServiceProxy, TacheRepository tacheRepository) {
		this.participantServiceProxy = participantServiceProxy;
		this.tacheService = new TacheService(tacheRepository);
	}
	
	/*
	 * Fonctions propre aux tâches
	 */
	
	//GET des tâches
	@GetMapping
	public ResponseEntity<?> getAllTaches(){
		return tacheService.getAllTaches();
	}

	//GET d'une tâche
	@GetMapping(value = "/{tacheId}")
	public ResponseEntity<?> getTache(@PathVariable("tacheId") String id) {
		return tacheService.getTache(id);
	}
	
	//Ajout d'une tâche
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> saveTache(@RequestBody Tache tache) {
		return tacheService.saveTache(tache);
	}
	
	//Suppression d'une tâche
	@RequestMapping(method = RequestMethod.DELETE, value = "/{tacheId}")
	public ResponseEntity<?> deleteTache(@PathVariable("tacheId") String id) {
		return tacheService.deleteTache(id);
	}
	
	//Modification d'une tâche
	@RequestMapping(method = RequestMethod.PUT, value = "/{tacheId}")
	public ResponseEntity<?> updateTache(@RequestBody Tache tache, @PathVariable("tacheId") String id) {
		return tacheService.updateTache(tache, id);
	}
	
	/*
	 * Options de recherche de taches
	 */
	
	@RequestMapping(params = "statut")
    public ResponseEntity<?> getTacheByEtat (@RequestParam("statut") String statut) {
		return tacheService.getTacheByEtat(statut);
    }

	@RequestMapping(params = "responsable")
	public ResponseEntity<?> getTacheByNomresponsable(@RequestParam("responsable") String responsable){
		return tacheService.getTacheByNomresponsable(responsable);
	}

	
	/*
	 * Fonctions en lien avec les participants
	 */
	
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
	
	//Méthode ajoutant un participant à une tâche
	@RequestMapping(method = RequestMethod.POST, value = "{tacheId}/participants")
    protected ResponseEntity<?> newParticipantTache(@PathVariable("tacheId") String tacheId, @RequestBody Participant participant){
		Participant saved = participantServiceProxy.newParticipant(tacheId,participant);
		HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.setLocation(linkTo(TacheController.class).slash(tacheId).slash("participants").slash(saved.getId()).toUri());
        
        Optional<Tache> tacheOptional = tacheService.tacheRepository.findById(tacheId);
        if(tacheOptional.isPresent()) {
        	System.out.println("TROUVE");
        	Tache tache = tacheOptional.get();
        	Set<String> idParticipants = tache.getParticipantsId();
        	idParticipants.add(saved.getId());
        	tache.setParticipants(idParticipants);
        	tacheService.updateTache(tache, tacheId);
        }
        
        return new ResponseEntity<>(null, responseHeader, HttpStatus.CREATED);
    }
	


}
