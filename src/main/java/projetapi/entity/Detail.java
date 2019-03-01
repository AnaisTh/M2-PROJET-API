package projetapi.entity;

import java.util.List;
import org.springframework.hateoas.core.Relation;


@Relation(collectionRelation="taches")
public class Detail extends Tache {

    private final List<?> participants;

    public Detail(Tache tache, List<?> participants2) {
        super(tache);
        this.participants = participants2;
    }

    public List<?> getParticipants() {
        return participants;
    }
}
