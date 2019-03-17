package projetapi.utility;

public enum AutorisationAcces {
	AUTORISE("Acces autorisé"), REFUSE("Acces refusé"), INCONNU("Tache inconnue");

	private String acces;

	// Constructeur
	AutorisationAcces(String acces) {
		this.acces = acces;
	}

	public String getAcces() {
		return acces;
	}

}
