package projetapi.entity;

import java.util.Set;

import javax.persistence.ElementCollection;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Participant {
    private String nom;
    private String prenom;

    public Participant() {
    }

    public Participant(String nom, String prenom, String commune, Long codepostal) {
        this.nom = nom;
        this.prenom = prenom;
    }
    
    public Participant(String nom) {
        this.nom = nom;
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
