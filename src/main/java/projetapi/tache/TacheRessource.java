package projetapi.tache;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="taches")
public interface TacheRessource extends CrudRepository<Tache,String> {

}
