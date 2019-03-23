package projetapi.utility;

public enum EtatTache {
	CREEE("CREEE"),
	ENCOURS("EN COURS"),
	ACHEVEE("ACHEVEE"),
	ARCHIVEE("ARCHIVEE");
	
	private String etat;
	
	public String getEtat() {
		return etat;
	}

	EtatTache(String etat){
		this.etat = etat;
	}
}
