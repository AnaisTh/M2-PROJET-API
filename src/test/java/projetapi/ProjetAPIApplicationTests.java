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

import projetapi.tache.Tache;
import projetapi.tache.TacheRessource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProjetAPIApplicationTests {

//	@Test
//	public void contextLoads() {
//	}
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TacheRessource tr;
    
    @Before
    public void setupContext() {
    	tr.deleteAll();
    }
    
   @Test
   public void getOneTache() {
	   Tache tache = new Tache("TacheA","MonsieurX", "ParticipantY", "dateCreationXXX","dateEcheanceYYY","etatAAA");
	   tache.setId(UUID.randomUUID().toString());
       tr.save(tache);
       ResponseEntity<String> response = restTemplate.getForEntity("/taches/" + tache.getId(), String.class);
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
       assertThat(response.getBody()).contains("TacheA");
       assertThat(response.getBody().contains("ParticipantY"));
   }
   
   @Test
   public void getAllTaches() {
	   Tache tacheA = new Tache("TacheA","MonsieurA", "ParticipantA", "dateCreationA","dateEcheanceA","etatA");
	   tacheA.setId(UUID.randomUUID().toString());
	   tr.save(tacheA);
	   
	   Tache tacheB = new Tache("TacheB","MonsieurB", "ParticipantB", "dateCreationB","dateEcheanceB","etatB");
	   tacheB.setId(UUID.randomUUID().toString());
	   tr.save(tacheB);
	   
	          ResponseEntity<String> response
               = restTemplate.getForEntity("/taches", String.class);

       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
       assertThat(response.getBody()).contains("TacheA");
       assertThat(response.getBody()).contains("TacheB");
       assertThat(response.getBody()).contains("ParticipantA");
       
       List<String> liste = JsonPath.read(response.getBody(),
               "$..taches..nomtache");
       assertThat(liste).asList().hasSize(2);
       
       List<String> liste2 = JsonPath.read(response.getBody(),
               "$..taches..participants");
       assertThat(liste2).asList().hasSize(2);
       
   }
   
   
   @Test
   public void postAPI() throws Exception {
	   Tache tache = new Tache("TacheA","MonsieurX", "ParticipantY", "dateCreationXXX","dateEcheanceYYY","etatAAA");
       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       HttpEntity<String> entity = new HttpEntity<>(this.toJsonString(tache), headers);
       ResponseEntity<?> response
               = restTemplate.postForEntity("/taches", entity, ResponseEntity.class);
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

       URI location = response.getHeaders().getLocation();
       response = restTemplate.getForEntity(location, String.class);
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
   }
    
     
   @Test
   public void putAPI() throws Exception {
	   Tache tache = new Tache("TacheA","MonsieurX", "ParticipantY", "dateCreationXXX","dateEcheanceYYY","etatAAA");
       tache.setId(UUID.randomUUID().toString());
       tr.save(tache);
       tache.setNomresponsable("nouveauResponsable");
       tache.setDateecheance("nouvelleDate");

       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
       HttpEntity<String> entity = new HttpEntity<>(this.toJsonString(tache), headers);
       
       restTemplate.put("/taches/" + tache.getId(), entity);

       ResponseEntity<String> response
               = restTemplate.getForEntity("/taches/" + tache.getId(), String.class);
       
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
       assertThat(response.getBody()).contains("nouveauResponsable");
       
       String code = JsonPath.read(response.getBody(), "$.dateecheance");
       assertThat(code).isEqualTo(tache.getDateecheance());
   }
   
   @Test
   public void deleteAPI() throws Exception {
	   Tache tache = new Tache("TacheA","MonsieurX", "ParticipantY", "dateCreationXXX","dateEcheanceYYY","etatAAA");
	   tache.setId(UUID.randomUUID().toString());
       tr.save(tache);
       restTemplate.delete("/taches/" + tache.getId());
       ResponseEntity<?> response = 
               restTemplate.getForEntity("/taches/"+tache.getId(), String.class);
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
   }
    
   
   @Test
   public void notFoundAPI() throws Exception {
       ResponseEntity<String> response
               = restTemplate.getForEntity("/taches/12", String.class);
       assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
   }
   
   
   private String toJsonString(Object o) throws Exception {
       ObjectMapper om = new ObjectMapper();
       return om.writeValueAsString(o);
   }
   

}
