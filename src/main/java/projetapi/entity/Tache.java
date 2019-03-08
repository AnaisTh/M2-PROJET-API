package projetapi.entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe representant l'entite tache du service Tache
 * @author anais
 *
 */
@Entity
public class Tache {

	/**
	 * Identifiant de la tache
	 */
	@Id
	private String id;
	/**
	 * Nom de la tache
	 */
	private String nomtache;
	/**
	 * Nom du responsable de la tache
	 */
	private String nomresponsable;
	/**
	 * Date de creation de la tache
	 */
	private LocalDate datecreation;
	/**
	 * Date d'echeance de la tache
	 */
	private LocalDate dateecheance;
	/**
	 * Etat courant de la tache
	 */
	private String etat;
	/**
	 * Liste des identifiants des participants de la tache
	 */
	@ElementCollection
	@JsonProperty("participants-id")
	private Set<String> participantsId;

	/**
	 * Map static permettant de stocker l'ensemble des états possibles d'une tache
	 */
	private static HashMap<Integer, String> listeEtats = new HashMap<Integer, String>();
	static {
		listeEtats.put(1, "CREEE");
		listeEtats.put(2, "EN COURS");
		listeEtats.put(3, "ACHEVEE");
		listeEtats.put(4, "ARCHIVEE");
	}

	/**
	 * Constructeur vide d'une tache necessaire pour JPA
	 */
	Tache() {

	}

	
	public Tache(String nomtache, String nomresponsable, Set<String> participantsId, LocalDate datecreation,
			LocalDate dateecheance, String etat) {
		super();
		this.nomtache = nomtache;
		this.nomresponsable = nomresponsable;
		this.participantsId = participantsId;
		this.datecreation = datecreation;
		this.dateecheance = dateecheance;
		this.etat = etat;
	}

	public Tache(Tache tache) {
		this.nomtache = tache.nomtache;
		this.nomresponsable = tache.nomresponsable;
		this.participantsId = tache.participantsId;
		this.datecreation = tache.datecreation;
		this.dateecheance = tache.dateecheance;
		this.etat = tache.etat;
		this.id = tache.getId();
	}

	// Getters et setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNomtache() {
		return nomtache;
	}

	public void setNomtache(String nomtache) {
		this.nomtache = nomtache;
	}

	public String getNomresponsable() {
		return nomresponsable;
	}

	public void setNomresponsable(String nomresponsable) {
		this.nomresponsable = nomresponsable;
	}

	public Set<String> getParticipantsId() {
		return participantsId;
	}

	public void setParticipants(Set<String> participantsId) {
		this.participantsId = participantsId;
	}

	public LocalDate getDatecreation() {
		return datecreation;
	}

	public void setDatecreation(LocalDate localDate) {
		this.datecreation = localDate;
	}

	public LocalDate getDateecheance() {
		return dateecheance;
	}

	public void setDateecheance(LocalDate dateecheance) {
		this.dateecheance = dateecheance;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public static HashMap<Integer, String> getListeEtats() {
		return listeEtats;
	}

	public void setParticipantsId(Set<String> participantsId) {
		this.participantsId = participantsId;
	}

}
