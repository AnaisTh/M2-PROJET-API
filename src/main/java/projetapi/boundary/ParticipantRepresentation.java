package projetapi.boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projetapi.entity.Participant;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = "/participants", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Participant.class)
public class ParticipantRepresentation {

    private final ParticipantRessource ir;

    public ParticipantRepresentation(ParticipantRessource ir) {
        this.ir = ir;
    }

    // GET all
    @GetMapping
    public ResponseEntity<?> getAllParticipants() {
        Iterable<Participant> allItervenants = ir.findAll();
        return new ResponseEntity<>(participantToResource(allItervenants), HttpStatus.OK);
    }

    // GET one
    @GetMapping(value = "/{participantId}")
    public ResponseEntity<?> getParticipant(@PathVariable("participantId") String id) {
        return Optional.ofNullable(ir.findById(id))
                .filter(Optional::isPresent)
                .map(i -> new ResponseEntity<>(participantToResource(i.get(), true), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST
    @PostMapping
    public ResponseEntity<?> newParticipant(@RequestBody Participant participant) {
        participant.setId(UUID.randomUUID().toString());
        Participant saved = ir.save(participant);
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.setLocation(linkTo(ParticipantRepresentation.class).slash(saved.getId()).toUri());
        return new ResponseEntity<>(null, responseHeader, HttpStatus.CREATED);
    }

    // DELETE
    @DeleteMapping(value = "/{participantId}")
    public ResponseEntity<?> deleteParticipant(@PathVariable("participantId") String id) {
        Optional<Participant> participant = ir.findById(id);
        if (participant.isPresent()) {
            ir.delete(participant.get());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Resources<Resource<Participant>> participantToResource(Iterable<Participant> participants) {
        Link selfLink = linkTo(methodOn(ParticipantRepresentation.class).getAllParticipants()).withSelfRel();
        List<Resource<Participant>> participantRessources = new ArrayList();
        participants.forEach(participant
                -> participantRessources.add(participantToResource(participant, false)));
        return new Resources<>(participantRessources, selfLink);
    }

    private Resource<Participant> participantToResource(Participant participant, Boolean collection) {
        Link selfLink = linkTo(ParticipantRepresentation.class)
                .slash(participant.getId())
                .withSelfRel();
        if (collection) {
            Link collectionLink = linkTo(methodOn(ParticipantRepresentation.class).getAllParticipants())
                    .withSelfRel();
            return new Resource<>(participant, selfLink, collectionLink);
        } else {
            return new Resource<>(participant, selfLink);
        }
    }
}
