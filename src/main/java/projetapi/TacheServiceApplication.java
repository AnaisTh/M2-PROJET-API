package projetapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Classe de demarrage du service
 * @author anais
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients // Pour scanner les services se déclarant Feign
@EnableCircuitBreaker
public class TacheServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TacheServiceApplication.class, args);
	}
}
