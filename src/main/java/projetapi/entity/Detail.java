package projetapi.entity;

import java.util.List;
import org.springframework.hateoas.core.Relation;

@Relation(collectionRelation="taches")
public class Detail extends Tache {

    private final List<Participant> participants;

    public Detail(Tache tache, List<Participant> participants) {
        super(tache);
        this.participants = participants;
    }

    public List<Participant> getParticipants() {
        return participants;
    }
}
