package projetapi;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import projetapi.participant.Participant;
import projetapi.participant.ParticipantRessource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProjetAPIApplicationTests {

//	@Test
//	public void contextLoads() {
//	}
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ParticipantRessource pr;
    
    @Before
    public void setupContext() {
    	pr.deleteAll();
    }
    
   @Test
   public void getOneparticipant() {
	   Participant participant = new Participant("nomA","prenomA");
	   participant.setId(UUID.randomUUID().toString());
       pr.save(participant);
       ResponseEntity<String> response = restTemplate.getForEntity("/participants/" + participant.getId(), String.class);
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
       assertThat(response.getBody()).contains("nomA");
       assertThat(response.getBody().contains("prenomA"));
   }
   
   @Test
   public void getAllparticipants() {
	   Participant participant = new Participant("nomA","prenomA");
	   participant.setId(UUID.randomUUID().toString());
       pr.save(participant);
	   
       Participant participant2 = new Participant("nomB","prenomB");
       participant2.setId(UUID.randomUUID().toString());
       pr.save(participant2);
	   
       ResponseEntity<String> response = restTemplate.getForEntity("/participants", String.class);

       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
       assertThat(response.getBody()).contains("prenomA");
       assertThat(response.getBody()).contains("nomB");
       
       List<String> liste = JsonPath.read(response.getBody(),"$..participants..nom");
       System.out.println(response.getBody());
       assertThat(liste).asList().hasSize(2);
      
       
   }
   
   
   @Test
   public void postAPI() throws Exception {
	   Participant participant = new Participant("nomA","prenomA");
	   
	   HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       HttpEntity<String> entity = new HttpEntity<>(this.toJsonString(participant), headers);
       ResponseEntity<?> response
               = restTemplate.postForEntity("/participants", entity, ResponseEntity.class);
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

       URI location = response.getHeaders().getLocation();
       response = restTemplate.getForEntity(location, String.class);
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
   }
    
     
   @Test
   public void putAPI() throws Exception {
	   Participant participant = new Participant("nomA","prenomA");
	   participant.setId(UUID.randomUUID().toString());
       pr.save(participant);
	   
       participant.setNom("nouveauNom");

       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
       HttpEntity<String> entity = new HttpEntity<>(this.toJsonString(participant), headers);
       
       restTemplate.put("/participants/" + participant.getId(), entity);

       ResponseEntity<String> response = restTemplate.getForEntity("/participants/" + participant.getId(), String.class);
       
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
       assertThat(response.getBody()).contains("nouveauNom");
       
   }
   
   @Test
   public void deleteAPI() throws Exception {
	   Participant participant = new Participant("nomA","prenomA");
	   participant.setId(UUID.randomUUID().toString());
       pr.save(participant);
       
       restTemplate.delete("/participants/" + participant.getId());
       ResponseEntity<?> response = restTemplate.getForEntity("/participants/"+participant.getId(), String.class);
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
   }
    
   
   @Test
   public void notFoundAPI() throws Exception {
       ResponseEntity<String> response = restTemplate.getForEntity("/participants/12", String.class);
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
   }
   
   
   private String toJsonString(Object o) throws Exception {
       ObjectMapper om = new ObjectMapper();
       return om.writeValueAsString(o);
   }
   

}
