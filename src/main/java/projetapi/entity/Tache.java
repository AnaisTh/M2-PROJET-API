package projetapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tache {

	@Id
	private String id;
	private String nomtache;
	private String nomresponsable;
	private String datecreation;
	private String dateecheance;
	private String etat;
	@ElementCollection
    @JsonProperty("participants-id")
    private Set<String> participantsId;
	
	
	//Constructeur pour JPA
	Tache(){
		
	}

	
	public Tache(String nomtache, String nomresponsable, Set<String> participantsId, String datecreation, String dateecheance,
			String etat) {
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



	//Getters et setters
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

	public String getDatecreation() {
		return datecreation;
	}

	public void setDatecreation(String datecreation) {
		this.datecreation = datecreation;
	}

	public String getDateecheance() {
		return dateecheance;
	}

	public void setDateecheance(String dateecheance) {
		this.dateecheance = dateecheance;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}
	

    
}
