package projetapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import projetapi.entity.Tache;

@RepositoryRestResource(collectionResourceRel="taches", path="taches")
public interface TacheRepository extends JpaRepository<Tache, String> {
	
	List<Tache> findByEtat(String etat);
	
	List<Tache> findByNomresponsable(String nomresponsable);

	List<Tache> findByNomresponsableAndEtat(String responsable, String etat);
	
}