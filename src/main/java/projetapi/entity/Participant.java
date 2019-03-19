package projetapi.entity;

import java.io.IOException;

import javax.persistence.Id;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Classe representant l'entite participant du service Participant
 * @author anais
 *
 */
public class Participant {

	/**
	 * Identifiant du participant
	 */
	@Id
	private String id;
	/**
	 * Nom du participant
	 */
	private String nom;
	/**
	 * Prenom du participant
	 */
	private String prenom;
	/**
	 * Identifiant de la tache a laquelle le participant est affecté
	 */
	private String tacheid;

	/**
	 * Constructeur vide du participant, necessaire pour JPA
	 */
	public Participant() {
		
	}

	public Participant(String nom, String prenom) {
		this.nom = nom;
		this.prenom = prenom;
	}
	public Participant(String nom, String prenom, String tacheid) {
		this(nom,prenom);
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
	
	/*
	public static Participant StringToParticipant(String json) throws JsonParseException, JsonMappingException, IOException {
		System.out.println(":!!!!!!!");
		System.out.println(json);
		ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Participant.class);
	}
	*/

}
