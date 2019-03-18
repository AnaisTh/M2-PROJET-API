package projetapi;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.jayway.jsonpath.JsonPath;

import projetapi.entity.Tache;
import projetapi.repository.TacheRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)	
public class TacheServiceApplicationTests {

	//Composant qui permet de faire des appels rest sur une API
	@Autowired
	private TestRestTemplate restTemplate;
	
	private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	
		
	@Autowired
	private TacheRepository tr;
	
	@Before
	public void setupContext() {
		tr.deleteAll();
		format.setLenient(false);
	}
	
	
	@Test
	public void getOneTacheRefuse() throws ParseException {
		Tache tache = new Tache("Projet", "Anaïs",format.parse("2021-03-02"), "1234");
		tache.setId(UUID.randomUUID().toString());
		tr.save(tache);
		
		final HttpHeaders headers = new HttpHeaders();
		headers.set("token", "hello");
	    //Create a new HttpEntity
	    final HttpEntity<String> entity = new HttpEntity<String>(headers);
	 	        
		ResponseEntity<String> response = restTemplate.exchange("/taches/"+tache.getId(), HttpMethod.GET,entity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
	
	@Test
	public void getOneTacheAccepte() throws ParseException {

		Tache tache = new Tache("Projet", "Anaïs",format.parse("2021-03-02"), "1234");
		tache.setId(UUID.randomUUID().toString());
		tr.save(tache);
		
		final HttpHeaders headers = new HttpHeaders();
		headers.set("token", "1234");
	    //Create a new HttpEntity
	    final HttpEntity<String> entity = new HttpEntity<String>(headers);
	 	   
	    ResponseEntity<String> response = restTemplate.exchange("/taches/"+tache.getId(), HttpMethod.GET,entity, String.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Anaïs");
		assertThat(response.getBody()).contains("2021-03-01");
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
	
	@Test
	public void getAllTache() throws ParseException {
		Tache tache1 = new Tache("Projet", "Anaïs",format.parse("2021-03-02"), "1234");
		tache1.setId(UUID.randomUUID().toString());
		Set<String> set1 = new HashSet<String>();set1.add("1");set1.add("2");
		tache1.setParticipantsId(set1);
		tr.save(tache1);
		
		Tache tache2 = new Tache("API", "Claire",format.parse("2021-03-02"), "5678");
		tache2.setId(UUID.randomUUID().toString());
		Set<String> set2 = new HashSet<String>();set2.add("3");set2.add("4");
		tache2.setParticipantsId(set2);
		tr.save(tache2);
		
		ResponseEntity<String> response = restTemplate.getForEntity("/taches", String.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Claire");
		
		List<String> liste = JsonPath.read(response.getBody(),
                "$..taches..nomtache");
        assertThat(liste).asList().hasSize(2);
        List<String> liste2 = JsonPath.read(response.getBody(),
                "$..taches..participants-id..*");
        assertThat(liste2).asList().hasSize(4);
		
	}

	
	@Test
    public void postAPI() throws Exception {
		Tache tache1 = new Tache("Projet", "Anaïs",format.parse("2021-03-02"), "1234");
		tache1.setId(UUID.randomUUID().toString());
		Set<String> set1 = new HashSet<String>();set1.add("1");set1.add("2");
		tache1.setParticipantsId(set1);

		System.out.println(tache1.toJsonString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(tache1.toJsonString(), headers);

        ResponseEntity<?> response= restTemplate.postForEntity("/taches", entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
	
	
	@Test
	public void DeleteOneTacheRefuse() throws ParseException {
		Tache tache = new Tache("Projet", "Anaïs",format.parse("2019-03-02"), "1234");
		tache.setId(UUID.randomUUID().toString());
		tr.save(tache);
		
		final HttpHeaders headers = new HttpHeaders();
		headers.set("token", "hello");
	    final HttpEntity<String> entity = new HttpEntity<String>(headers);
	 	        
		ResponseEntity<String> response = restTemplate.exchange("/taches/"+tache.getId(), HttpMethod.DELETE,entity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
		
	@Test
	public void deleteOneTacheAccepte() throws ParseException {

		Tache tache = new Tache("Projet", "Anaïs",format.parse("2019-03-02"), "1234");
		tache.setId(UUID.randomUUID().toString());
		tr.save(tache);
		
		final HttpHeaders headers = new HttpHeaders();
		headers.set("token", "1234");
	    final HttpEntity<String> entity = new HttpEntity<String>(headers);
	 	   
	    ResponseEntity<String> response = restTemplate.exchange("/taches/"+tache.getId(), HttpMethod.DELETE,entity, String.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		response = restTemplate.exchange("/taches/"+tache.getId(), HttpMethod.GET,entity, String.class);
		
		assertThat(response.getBody()).contains("ACHEVEE");
		
	}
		
	@Test
	public void DeleteOneTacheAbsente() {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("token", "1234");
	    //Create a new HttpEntity
	    final HttpEntity<String> entity = new HttpEntity<String>(headers);
	 	        
		ResponseEntity<String> response = restTemplate.exchange("/taches/1", HttpMethod.DELETE,entity, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	

	
	
	
}
