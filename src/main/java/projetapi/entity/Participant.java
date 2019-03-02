package projetapi.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Participant implements Serializable {

    @Id
    private String id;
    private String nom;
    private String prenom;
    private String tacheid;

    public Participant() {
    }

    public Participant(String nom, String prenom, String tacheId) {
        this.nom = nom;
        this.prenom = prenom;
        this.tacheid = tacheId;
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

	public String getTacheId() {
		return tacheid;
	}

	public void setTacheId(String tacheId) {
		this.tacheid = tacheId;
	}

    
    
}
