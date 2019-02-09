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

    public Participant() {
    }

    public Participant(String nom, String prenom) {
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
