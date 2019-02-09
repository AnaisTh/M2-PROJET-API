package projetapi.boundary;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import projetapi.entity.Participant;

@FeignClient("http://projet-api-participant")
public interface ParticipantRestClient {

    @RequestMapping(method = RequestMethod.GET, value = "/participants/{id}")
    Participant get(@PathVariable("id") String id);
}
