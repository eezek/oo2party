package br.edu.ulbra.election.party.service;

import br.edu.ulbra.election.party.client.CandidateClientService;
import br.edu.ulbra.election.party.exception.GenericOutputException;
import br.edu.ulbra.election.party.input.v1.PartyInput;
import br.edu.ulbra.election.party.model.Party;
import br.edu.ulbra.election.party.output.v1.CandidateOutput;
import br.edu.ulbra.election.party.output.v1.GenericOutput;
import br.edu.ulbra.election.party.output.v1.PartyOutput;
import br.edu.ulbra.election.party.repository.PartyRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.dao.DataIntegrityViolationException;
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
    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private final CandidateClientService candidateClientService;


    public List<PartyOutput> getAll(){
        Type partyOutputListType = new TypeToken<List<PartyOutput>>() {
        }.getType();
        return modelMapper.map(partyRepository.findAll(), partyOutputListType);
    }

    public PartyOutput getById(Long partyId) {
        return modelMapper.map(byId(partyId), PartyOutput.class);
    }

    public PartyOutput create(PartyInput partyInput) {
        validateInput(partyInput, false);
        Party party = modelMapper.map(partyInput, Party.class);
        try{
            party = partyRepository.save(party);
        }catch (Exception e)
        {
            if(e instanceof DataIntegrityViolationException)
                throw new GenericOutputException("Party code or number already exists.");
        }

        return modelMapper.map(party, PartyOutput.class);
    }

    public PartyOutput update(Long partyId, PartyInput partyInput) {
        if (partyId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        validateInput(partyInput, true);

        Party party = byId(partyId);
        party.setCode(partyInput.getCode());
        party.setName(partyInput.getName());
        party.setNumber(partyInput.getNumber());
        try{
            party = partyRepository.save(party);
        }catch (Exception e)
        {
            if(e instanceof DataIntegrityViolationException)
                throw new GenericOutputException("Party code or number already exists.");
        }
        return modelMapper.map(party, PartyOutput.class);
    }

    public GenericOutput delete(Long partyId) {
        validateDelete(partyId);
        partyRepository.delete(byId(partyId));
        return new GenericOutput("Party deleted");

    }

    private Party byId(Long partyId){
        return partyRepository.findById(partyId).orElseThrow(()-> new EntityNotFoundException(MESSAGE_NOT_FOUND));
    }

    private void validateInput(PartyInput partyInput, boolean isUpdate){
        if(partyInput.getNumber()<10 || partyInput.getNumber()>99)
            throw new GenericOutputException("The number must have two digits");
    }

    private void validateDelete(Long partyId)
    {
        try{
            List<CandidateOutput> candidates = candidateClientService.getCandidates();
            for (CandidateOutput candidate : candidates) {
                if(candidate.getPartyOutput().getId() == partyId)
                    throw new GenericOutputException("Not authorized");
            }

        }catch (FeignException e)
        {
            throw new GenericOutputException("Candidate request error");
        }
    }
}
