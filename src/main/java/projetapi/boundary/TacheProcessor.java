package projetapi.boundary;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Service;

import projetapi.entity.Detail;
import projetapi.entity.Participant;
import projetapi.entity.Tache;

@Service
public class TacheProcessor implements ResourceProcessor<Resource<? extends Tache>> {

    @Autowired
    ParticipantClient client;

    @Override
    public Resource<Detail> process(Resource<? extends Tache> resource) {
        Tache tache = resource.getContent();
        List<Participant> participants = tache
                .getParticipantsId()
                .stream()
                .map(client::get)
                .collect(Collectors.toList());
        return new Resource<>(new Detail(tache, participants), resource.getLinks());
    }
}
