package br.edu.ulbra.election.party.client;

import br.edu.ulbra.election.party.output.v1.CandidateOutput;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service

public class CandidateClientService {

    private final CandidateService candidateClient;

    @Autowired
    public CandidateClientService(CandidateService candidateClient) {
        this.candidateClient = candidateClient;
    }

    public List<CandidateOutput> getCandidates()
    {
        return this.candidateClient.getCandidates();
    }

    @FeignClient(value="candidate-service", url="${url.candidate-service}")
    private interface CandidateService{

        @GetMapping("/v1/candidate/")
        List<CandidateOutput> getCandidates();
    }
}
