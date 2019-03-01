package projetapi.repository;

import org.springframework.data.repository.CrudRepository;

import projetapi.entity.Participant;

public interface ParticipantRepository extends CrudRepository<Participant,String> {
    
}

