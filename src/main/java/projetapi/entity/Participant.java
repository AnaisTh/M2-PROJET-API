package projetapi.entity;

import javax.persistence.Id;

public class Participant {
	
	@Id
    private String id;
    private String nom;
    private String prenom;
    private String tacheid;

   
	public Participant() {
    }

    public Participant(String nom, String prenom, String tacheid) {
        this.nom = nom;
        this.prenom = prenom;
        this.tacheid = tacheid;
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

	public String getTacheid() {
		return tacheid;
	}

	public void setTacheid(String tacheid) {
		this.tacheid = tacheid;
	}
    
	 public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

    
}
