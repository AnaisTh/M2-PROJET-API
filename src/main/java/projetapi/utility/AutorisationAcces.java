package projetapi.utility;

import org.springframework.http.HttpStatus;

public enum AutorisationAcces {
	AUTORISE("Acces autorisé",HttpStatus.OK), 
	REFUSE("Acces refusé",HttpStatus.FORBIDDEN), 
	INCONNU("Tache inconnue",HttpStatus.NOT_FOUND);

	private String message;
	private HttpStatus httpStatus;

	
	AutorisationAcces(String message, HttpStatus httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	


}
