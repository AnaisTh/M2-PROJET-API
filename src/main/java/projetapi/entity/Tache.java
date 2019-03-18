package projetapi.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Future;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import projetapi.utility.EtatTache;

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
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date datecreation;
	/**
	 * Date d'echeance de la tache
	 */
	@JsonFormat(pattern="yyyy-MM-dd") @Future
	private Date dateecheance;
	/**
	 * Etat courant de la tache
	 */
	private String etat;
	/**
	 * Token permettant l'acces aux informations de la tache
	 */
	private String tokenconnexion;
	/**
	 * Liste des identifiants des participants de la tache
	 */
	@ElementCollection
	@JsonProperty("participants-id")
	private Set<String> participantsId;

	

	/**
	 * Constructeur vide d'une tache necessaire pour JPA
	 */
	Tache() {

	}

	
	public Tache(String nomtache, String nomresponsable, Set<String> participantsId, Date datecreation,
			Date dateecheance, String etat, String tokenconnexion) {
		super();
		this.nomtache = nomtache;
		this.nomresponsable = nomresponsable;
		this.participantsId = participantsId;
		this.datecreation = datecreation;
		this.dateecheance = dateecheance;
		this.etat = etat;
		this.tokenconnexion = tokenconnexion;
	}
	
	public Tache(String nomtache, String nomresponsable,
			Date dateecheance, String tokenconnexion) {
		super();
		this.nomtache = nomtache;
		this.nomresponsable = nomresponsable;
		this.participantsId = new HashSet<String>();
		this.datecreation = new Date();
		this.dateecheance = dateecheance;
		this.etat = EtatTache.CREEE.getEtat();
		this.tokenconnexion = tokenconnexion;
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


	public Date getDatecreation() {
		return datecreation;
	}

	public void setDatecreation(Date localDate) {
		this.datecreation = localDate;
	}

	public Date getDateecheance() {
		return dateecheance;
	}

	public void setDateecheance(Date dateecheance) {
		this.dateecheance = dateecheance;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public void setParticipantsId(Set<String> participantsId) {
		this.participantsId = participantsId;
	}
	
	public String getTokenconnexion() {
		return this.tokenconnexion;
	}
	
	public void setTokenConnexion(String tokenconnexion) {
		this.tokenconnexion= tokenconnexion;
	}

	 public String toJsonString() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(fmt);
        return objectMapper.writeValueAsString(this);
	}
}

