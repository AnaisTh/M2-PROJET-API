package projetapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import projetapi.entity.Tache;
/**
 * Classe representant le repository d'acces aux informations des taches
 * @author anais
 *
 */
@RepositoryRestResource(collectionResourceRel = "taches", path = "taches")
public interface TacheRepository extends JpaRepository<Tache, String> {

	/**
	 * Requete personnalisee d'acces aux taches selon leur etat
	 * @param etat etat a rechercher
	 * @return liste des taches
	 */
	List<Tache> findByEtat(String etat);

	/**
	 * Requete personnalisee d'acces aux taches selon leur responsable
	 * @param nomresponsable responsable a rechercher
	 * @return liste des taches
	 */
	List<Tache> findByNomresponsable(String nomresponsable);

	/**
	 * Requete personnalisee d'acces aux taches selon leur responsable et leur etat
	 * @param responsable responsable a rechercher
	 * @param etat etat a recherche
	 * @return liste des taches
	 */
	List<Tache> findByNomresponsableAndEtat(String responsable, String etat);

}