package br.edu.ulbra.election.party.repository;

import br.edu.ulbra.election.party.model.Party;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRepository extends CrudRepository<Party, Long> {
}
