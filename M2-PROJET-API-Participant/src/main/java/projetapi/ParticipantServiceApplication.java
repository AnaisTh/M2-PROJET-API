package projetapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ParticipantServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParticipantServiceApplication.class, args);
	}
}
