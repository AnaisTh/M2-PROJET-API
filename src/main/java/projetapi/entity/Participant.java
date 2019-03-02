package projetapi.entity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Set;

@Entity
public class Participant implements Serializable {

    @Id
    private String id;
    private String nom;
    private String prenom;
    @ElementCollection
    @JsonProperty("tache-id")
    private Set<String> tacheId;

    public Participant() {
    }

    public Participant(String nom, String prenom, Set<String> tacheId) {
        this.nom = nom;
        this.prenom = prenom;
        this.tacheId = tacheId;
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

	public Set<String> getTacheId() {
		return tacheId;
	}

	public void setTacheId(Set<String> tacheId) {
		this.tacheId = tacheId;
	}

    
    
}
