package org.opencds.cqf.qdm.fivepoint4.controller;

import org.opencds.cqf.qdm.fivepoint4.exception.ResourceNotFound;
import org.opencds.cqf.qdm.fivepoint4.model.*;
import org.opencds.cqf.qdm.fivepoint4.repository.NegativeInterventionRecommendedRepository;
import org.opencds.cqf.qdm.fivepoint4.validation.QdmValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
public class NegativeInterventionRecommendedController implements Serializable
{
    private final NegativeInterventionRecommendedRepository repository;

    @Autowired
    public NegativeInterventionRecommendedController(NegativeInterventionRecommendedRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/NegativeInterventionRecommended")
    public List<NegativeInterventionRecommended> getAll(@RequestParam(name = "patientId", required = false) String patientId)
    {
        if (patientId == null)
        {
            return repository.findAll();
        }
        else {
            NegativeInterventionRecommended exampleType = new NegativeInterventionRecommended();
            Id pId = new Id();
            pId.setValue(patientId);
            exampleType.setPatientId(pId);
            ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("patientId.value", ExampleMatcher.GenericPropertyMatchers.exact());
            Example<NegativeInterventionRecommended> example = Example.of(exampleType, matcher);

            return repository.findAll(example);
        }
    }

    @GetMapping("/NegativeInterventionRecommended/{id}")
    public @ResponseBody NegativeInterventionRecommended getById(@PathVariable(value = "id") String id)
    {
        return repository.findBySystemId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Read Failed: NegativeInterventionRecommended/%s not found", id),
                                new ResourceNotFound()
                        )
                );
    }

    @PostMapping("/NegativeInterventionRecommended")
    public @ResponseBody NegativeInterventionRecommended create(@RequestBody @Valid NegativeInterventionRecommended negativeInterventionRecommended)
    {
        QdmValidator.validateResourceTypeAndName(negativeInterventionRecommended, negativeInterventionRecommended);
        return repository.save(negativeInterventionRecommended);
    }

    @PutMapping("/NegativeInterventionRecommended/{id}")
    public NegativeInterventionRecommended update(@PathVariable(value = "id") String id,
                                             @RequestBody @Valid NegativeInterventionRecommended negativeInterventionRecommended)
    {
        QdmValidator.validateResourceId(negativeInterventionRecommended.getId(), id);
        Optional<NegativeInterventionRecommended> update = repository.findById(id);
        if (update.isPresent())
        {
            NegativeInterventionRecommended updateResource = update.get();
            QdmValidator.validateResourceTypeAndName(negativeInterventionRecommended, updateResource);
            updateResource.copy(negativeInterventionRecommended);
            return repository.save(updateResource);
        }

        QdmValidator.validateResourceTypeAndName(negativeInterventionRecommended, negativeInterventionRecommended);
        return repository.save(negativeInterventionRecommended);
    }

    @DeleteMapping("/NegativeInterventionRecommended/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id)
    {
        NegativeInterventionRecommended nep =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Delete Failed: NegativeInterventionRecommended/%s not found", id),
                                        new ResourceNotFound()
                                )
                        );

        repository.delete(nep);

        return ResponseEntity.ok().build();
    }
}
