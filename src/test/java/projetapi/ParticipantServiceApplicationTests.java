package projetapi;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.util.List;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import projetapi.entity.Participant;
import projetapi.repository.ParticipantRepository;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class ParticipantServiceApplicationTests {



	//Composant qui permet de faire des appels rest sur une API
	@Autowired
	private TestRestTemplate restTemplate;
			
	@Autowired
	private ParticipantRepository pr;
		
	@Before
	public void setupContext() {
		pr.deleteAll();
	}
		
	@Test
	public void getAllParticipant() {
		Participant participant = new Participant("Nom1", "Prenom1", "1");
		participant.setId(UUID.randomUUID().toString());
		pr.save(participant);
		
		Participant participant2 = new Participant("Nom1", "Prenom2", "1");
		participant2.setId(UUID.randomUUID().toString());
		pr.save(participant2);
		
		ResponseEntity<String> response = restTemplate.getForEntity("/participants", String.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Nom1");
		
		List<String> liste = JsonPath.read(response.getBody(),
                "$..participants..prenom");
        assertThat(liste).asList().hasSize(2);		
	}
	
	
		
	
	@Test
	public void getOneParticipant() {

		Participant participant = new Participant("Nom1", "Prenom1", "1");
		participant.setId(UUID.randomUUID().toString());
		pr.save(participant);
		
	    ResponseEntity<String> response = restTemplate.getForEntity("/participants/"+participant.getId(),String.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Nom1");
	}
		
	@Test
	public void getOneParticipantNotFound() {
		ResponseEntity<String> response = restTemplate.getForEntity("/participants/1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
		

	@Test
    public void postParticipant() throws Exception {

		Participant participant = new Participant("Nom1", "Prenom1", "1");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(this.toJsonString(participant), headers);
        
        ResponseEntity<?> response
                = restTemplate.postForEntity("/participants/"+participant.getTacheId(), entity, String.class);

        
        participant = StringToParticipant(response.getBody().toString());
        
        response = restTemplate.getForEntity("/participants/"+participant.getId(), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

	@Test
    public void deleteAPI() throws Exception {
		Participant participant = new Participant("Nom1", "Prenom1", "1");
		participant.setId(UUID.randomUUID().toString());
		pr.save(participant);
		
        restTemplate.delete("/participants/" + participant.getId());
        ResponseEntity<?> response = 
                restTemplate.getForEntity("/participants/"+participant.getId(), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
		
		
	private String toJsonString(Object o) throws Exception {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(o);
    }
	
	private Participant StringToParticipant(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Participant.class);
	}
}
