package projetapi.participant;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Participant {
	@Id
	private String id;
	private String nom;
	private String prenom;
	
	//Constructeur pour JPA
	Participant(){
		
	}

	public Participant(String nom, String prenom) {
		super();
		this.nom = nom;
		this.prenom = prenom;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	


	
	
}
