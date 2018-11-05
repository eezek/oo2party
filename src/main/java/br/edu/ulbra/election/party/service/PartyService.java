package br.edu.ulbra.election.party.service;

import br.edu.ulbra.election.party.input.v1.PartyInput;
import br.edu.ulbra.election.party.model.Party;
import br.edu.ulbra.election.party.output.v1.GenericOutput;
import br.edu.ulbra.election.party.output.v1.PartyOutput;
import br.edu.ulbra.election.party.repository.PartyRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

@Service
@AllArgsConstructor
public class PartyService {

    private PartyRepository partyRepository;

    private ModelMapper modelMapper;

    private static final String MESSAGE_NOT_FOUND = "Not found";

    public List<PartyOutput> getAll(){
        Type partyOutputListType = new TypeToken<List<PartyOutput>>() {
        }.getType();
        return modelMapper.map(partyRepository.findAll(), partyOutputListType);
    }

    public PartyOutput getById(Long partyId) {
        return modelMapper.map(byId(partyId), PartyOutput.class);
    }

    public PartyOutput create(PartyInput partyInput) {
        Party party = modelMapper.map(partyInput, Party.class);
        party = partyRepository.save(party);
        return modelMapper.map(party, PartyOutput.class);
    }

    public PartyOutput update(Long partyId, PartyInput partyInput) {

        Party party = byId(partyId);

        party.setCode(partyInput.getCode());
        party.setName(partyInput.getName());
        party.setNumber(partyInput.getNumber());
        party = partyRepository.save(party);
        return modelMapper.map(party, PartyOutput.class);
    }

    public GenericOutput delete(Long partyId) {

        partyRepository.delete(byId(partyId));

        return new GenericOutput("Party deleted");

    }

    private Party byId(Long partyId){
        return partyRepository.findById(partyId).orElseThrow(()-> new EntityNotFoundException(MESSAGE_NOT_FOUND));
    }

}
