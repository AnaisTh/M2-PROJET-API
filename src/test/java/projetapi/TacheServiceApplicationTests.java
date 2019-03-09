package projetapi;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import projetapi.entity.Tache;
import projetapi.repository.TacheRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)	
public class TacheServiceApplicationTests {

	//Composant qui permet de faire des appels rest sur une API
	@Autowired
	private TestRestTemplate restTemplate;
		
	@Autowired
	private TacheRepository tr;
	
	@Before
	public void setupContext() {
		tr.deleteAll();
	}
	
	
	@Test
	public void getOneTacheRefuse() {

		Tache tache = new Tache("NomTache", "NomResponsable", null, LocalDate.now(),LocalDate.of(2019, 03, 2), "créé", "1234");
		tache.setId("1");
		tr.save(tache);
		
		final HttpHeaders headers = new HttpHeaders();
		headers.set("token", "hello");
	    //Create a new HttpEntity
	    final HttpEntity<String> entity = new HttpEntity<String>(headers);
	 	        
		ResponseEntity<String> response = restTemplate.exchange("/taches/"+tache.getId(), HttpMethod.GET,entity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
	
	@Test
	public void getOneTacheAccepte() {

		Tache tache = new Tache("NomTache", "NomResponsable", null, LocalDate.now(),LocalDate.of(2019, 03, 2), "créé", "1234");
		tache.setId("1");
		tr.save(tache);
		
		final HttpHeaders headers = new HttpHeaders();
		headers.set("token", "1234");
	    //Create a new HttpEntity
	    final HttpEntity<String> entity = new HttpEntity<String>(headers);
	 	   
	    ResponseEntity<String> response = restTemplate.exchange("/taches/"+tache.getId(), HttpMethod.GET,entity, String.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("NomResponsable");
		assertThat(response.getBody()).contains("2019-03-02");
	}
	
	@Test
	public void getOneTacheAbsente() {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("token", "1234");
	    //Create a new HttpEntity
	    final HttpEntity<String> entity = new HttpEntity<String>(headers);
	 	        
		ResponseEntity<String> response = restTemplate.exchange("/taches/1", HttpMethod.GET,entity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

}
