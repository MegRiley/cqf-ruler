package org.opencds.cqf.qdm.fivepoint4.controller;

import org.opencds.cqf.qdm.fivepoint4.exception.ResourceNotFound;
import org.opencds.cqf.qdm.fivepoint4.model.*;
import org.opencds.cqf.qdm.fivepoint4.repository.PositiveEncounterRecommendedRepository;
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
public class PositiveEncounterRecommendedController implements Serializable
{
    private final PositiveEncounterRecommendedRepository repository;

    @Autowired
    public PositiveEncounterRecommendedController(PositiveEncounterRecommendedRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/PositiveEncounterRecommended")
    public List<PositiveEncounterRecommended> getAll(@RequestParam(name = "patientId", required = false) String patientId)
    {
        if (patientId == null)
        {
            return repository.findAll();
        }
        else {
            PositiveEncounterRecommended exampleType = new PositiveEncounterRecommended();
            Id pId = new Id();
            pId.setValue(patientId);
            exampleType.setPatientId(pId);
            ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("patientId.value", ExampleMatcher.GenericPropertyMatchers.exact());
            Example<PositiveEncounterRecommended> example = Example.of(exampleType, matcher);

            return repository.findAll(example);
        }
    }

    @GetMapping("/PositiveEncounterRecommended/{id}")
    public @ResponseBody PositiveEncounterRecommended getById(@PathVariable(value = "id") String id)
    {
        return repository.findBySystemId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Read Failed: PositiveEncounterRecommended/%s not found", id),
                                new ResourceNotFound()
                        )
                );
    }

    @PostMapping("/PositiveEncounterRecommended")
    public PositiveEncounterRecommended create(@RequestBody @Valid PositiveEncounterRecommended positiveEncounterRecommended)
    {
        QdmValidator.validateResourceTypeAndName(positiveEncounterRecommended, positiveEncounterRecommended);
        return repository.save(positiveEncounterRecommended);
    }

    @PutMapping("/PositiveEncounterRecommended/{id}")
    public PositiveEncounterRecommended update(@PathVariable(value = "id") String id,
                                             @RequestBody @Valid PositiveEncounterRecommended positiveEncounterRecommended)
    {
        QdmValidator.validateResourceId(positiveEncounterRecommended.getId(), id);
        Optional<PositiveEncounterRecommended> update = repository.findById(id);
        if (update.isPresent())
        {
            PositiveEncounterRecommended updateResource = update.get();
            QdmValidator.validateResourceTypeAndName(positiveEncounterRecommended, updateResource);
            updateResource.copy(positiveEncounterRecommended);
            return repository.save(updateResource);
        }

        QdmValidator.validateResourceTypeAndName(positiveEncounterRecommended, positiveEncounterRecommended);
        return repository.save(positiveEncounterRecommended);
    }

    @DeleteMapping("/PositiveEncounterRecommended/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id)
    {
        PositiveEncounterRecommended pep =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Delete Failed: PositiveEncounterRecommended/%s not found", id),
                                        new ResourceNotFound()
                                )
                        );

        repository.delete(pep);

        return ResponseEntity.ok().build();
    }
}
