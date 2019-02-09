package projetapi.boundary;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import projetapi.entity.Participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParticipantClient {
    @Autowired
    ParticipantRestClient restClient;
    
    @HystrixCommand(fallbackMethod = "fallback")
    public Participant get(String id) {
        return restClient.get(id);
    }

    public Participant fallback(String id) {
        return new Participant("non disponible");
    }
    
}

