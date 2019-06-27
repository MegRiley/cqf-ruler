package org.opencds.cqf.qdm.fivepoint4.controller;

import org.opencds.cqf.qdm.fivepoint4.exception.ResourceNotFound;
import org.opencds.cqf.qdm.fivepoint4.model.*;
import org.opencds.cqf.qdm.fivepoint4.repository.PositiveEncounterPerformedRepository;
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
public class PositiveEncounterPerformedController implements Serializable
{
    private final PositiveEncounterPerformedRepository repository;

    @Autowired
    public PositiveEncounterPerformedController(PositiveEncounterPerformedRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/PositiveEncounterPerformed")
    public List<PositiveEncounterPerformed> getAll(@RequestParam(name = "patientId", required = false) String patientId)
    {
        if (patientId == null)
        {
            return repository.findAll();
        }
        else {
            PositiveEncounterPerformed exampleType = new PositiveEncounterPerformed();
            Id pId = new Id();
            pId.setValue(patientId);
            exampleType.setPatientId(pId);
            ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("patientId.value", ExampleMatcher.GenericPropertyMatchers.exact());
            Example<PositiveEncounterPerformed> example = Example.of(exampleType, matcher);

            return repository.findAll(example);
        }
    }

    @GetMapping("/PositiveEncounterPerformed/{id}")
    public @ResponseBody PositiveEncounterPerformed getById(@PathVariable(value = "id") String id)
    {
        return repository.findBySystemId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Read Failed: PositiveEncounterPerformed/%s not found", id),
                                new ResourceNotFound()
                        )
                );
    }

    @PostMapping("/PositiveEncounterPerformed")
    public PositiveEncounterPerformed create(@RequestBody @Valid PositiveEncounterPerformed positiveEncounterPerformed)
    {
        QdmValidator.validateResourceTypeAndName(positiveEncounterPerformed, positiveEncounterPerformed);
        return repository.save(positiveEncounterPerformed);
    }

    @PutMapping("/PositiveEncounterPerformed/{id}")
    public PositiveEncounterPerformed update(@PathVariable(value = "id") String id,
                                             @RequestBody @Valid PositiveEncounterPerformed positiveEncounterPerformed)
    {
        QdmValidator.validateResourceId(positiveEncounterPerformed.getId(), id);
        Optional<PositiveEncounterPerformed> update = repository.findById(id);
        if (update.isPresent())
        {
            PositiveEncounterPerformed updateResource = update.get();
            QdmValidator.validateResourceTypeAndName(positiveEncounterPerformed, updateResource);
            updateResource.copy(positiveEncounterPerformed);
            return repository.save(updateResource);
        }

        QdmValidator.validateResourceTypeAndName(positiveEncounterPerformed, positiveEncounterPerformed);
        return repository.save(positiveEncounterPerformed);
    }

    @DeleteMapping("/PositiveEncounterPerformed/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id)
    {
        PositiveEncounterPerformed pep =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Delete Failed: PositiveEncounterPerformed/%s not found", id),
                                        new ResourceNotFound()
                                )
                        );

        repository.delete(pep);

        return ResponseEntity.ok().build();
    }
}
