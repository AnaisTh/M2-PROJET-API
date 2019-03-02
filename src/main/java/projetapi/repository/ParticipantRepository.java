package projetapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import projetapi.entity.Participant;

public interface ParticipantRepository extends CrudRepository<Participant,String> {
    
	List<Participant> findByTacheId(String tacheId);
	
	Optional<Participant> findByTacheIdAndId(String tacheId, String id);

	Optional<Participant> findById(String id);
}

