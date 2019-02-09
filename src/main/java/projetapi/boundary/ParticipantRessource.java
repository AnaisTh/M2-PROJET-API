package projetapi.boundary;

import org.springframework.data.repository.CrudRepository;

import projetapi.entity.Participant;

public interface ParticipantRessource extends CrudRepository<Participant,String> {
    
}

