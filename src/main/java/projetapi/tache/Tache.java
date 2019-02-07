package projetapi.tache;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Tache {
	@Id
	private String id;
	private String nomtache;
	private String nomresponsable;
	private String participants; 
	private String datecreation;
	private String dateecheance;
	private String etat;
	
	
	//Constructeur pour JPA
	Tache(){
		
	}

	
	public Tache(String nomtache, String nomresponsable, String participants, String datecreation, String dateecheance,
			String etat) {
		super();
		this.nomtache = nomtache;
		this.nomresponsable = nomresponsable;
		this.participants = participants;
		this.datecreation = datecreation;
		this.dateecheance = dateecheance;
		this.etat = etat;
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


	public String getParticipants() {
		return participants;
	}


	public void setParticipants(String participants) {
		this.participants = participants;
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
