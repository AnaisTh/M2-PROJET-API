package projetapi.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import projetapi.entity.Participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParticipantService {
    @Autowired
    ParticipantServiceProxy restClient;
    
    @HystrixCommand(fallbackMethod = "fallback")
    public Participant getParticipant(String id) {
        return restClient.getParticipant(id);
    }

    public Participant fallback(String id) {
        return new Participant("non disponible");
    }
    
}

