package projetapi.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import projetapi.entity.Participant;

//Proxy de connexion à l'API des participants

@FeignClient("http://projet-api-participant")
public interface ParticipantServiceProxy {

    @RequestMapping(method = RequestMethod.GET, value = "/participants/{participantId}")
    Participant getParticipant(@PathVariable("participantId") String id);
    
    @RequestMapping(method = RequestMethod.GET, value = "/participants")
    List<Participant> getAllParticipants();
    
    
    
    
    
    
}
