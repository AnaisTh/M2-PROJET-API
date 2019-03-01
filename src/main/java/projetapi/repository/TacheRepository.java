package projetapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import projetapi.entity.Tache;

@RepositoryRestResource(collectionResourceRel="taches", path="taches")
public interface TacheRepository extends CrudRepository<Tache, String> {
}