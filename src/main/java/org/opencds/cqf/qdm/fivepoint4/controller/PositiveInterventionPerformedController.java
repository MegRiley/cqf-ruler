package org.opencds.cqf.qdm.fivepoint4.controller;

import org.opencds.cqf.qdm.fivepoint4.exception.ResourceNotFound;
import org.opencds.cqf.qdm.fivepoint4.model.*;
import org.opencds.cqf.qdm.fivepoint4.repository.PositiveInterventionPerformedRepository;
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
public class PositiveInterventionPerformedController implements Serializable
{
    private final PositiveInterventionPerformedRepository repository;

    @Autowired
    public PositiveInterventionPerformedController(PositiveInterventionPerformedRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/PositiveInterventionPerformed")
    public List<PositiveInterventionPerformed> getAll(@RequestParam(name = "patientId", required = false) String patientId)
    {
        if (patientId == null)
        {
            return repository.findAll();
        }
        else {
            PositiveInterventionPerformed exampleType = new PositiveInterventionPerformed();
            Id pId = new Id();
            pId.setValue(patientId);
            exampleType.setPatientId(pId);
            ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("patientId.value", ExampleMatcher.GenericPropertyMatchers.exact());
            Example<PositiveInterventionPerformed> example = Example.of(exampleType, matcher);

            return repository.findAll(example);
        }
    }

    @GetMapping("/PositiveInterventionPerformed/{id}")
    public @ResponseBody PositiveInterventionPerformed getById(@PathVariable(value = "id") String id)
    {
        return repository.findBySystemId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Read Failed: PositiveInterventionPerformed/%s not found", id),
                                new ResourceNotFound()
                        )
                );
    }

    @PostMapping("/PositiveInterventionPerformed")
    public PositiveInterventionPerformed create(@RequestBody @Valid PositiveInterventionPerformed positiveInterventionPerformed)
    {
        QdmValidator.validateResourceTypeAndName(positiveInterventionPerformed, positiveInterventionPerformed);
        return repository.save(positiveInterventionPerformed);
    }

    @PutMapping("/PositiveInterventionPerformed/{id}")
    public PositiveInterventionPerformed update(@PathVariable(value = "id") String id,
                                             @RequestBody @Valid PositiveInterventionPerformed positiveInterventionPerformed)
    {
        QdmValidator.validateResourceId(positiveInterventionPerformed.getId(), id);
        Optional<PositiveInterventionPerformed> update = repository.findById(id);
        if (update.isPresent())
        {
            PositiveInterventionPerformed updateResource = update.get();
            QdmValidator.validateResourceTypeAndName(positiveInterventionPerformed, updateResource);
            updateResource.copy(positiveInterventionPerformed);
            return repository.save(updateResource);
        }

        QdmValidator.validateResourceTypeAndName(positiveInterventionPerformed, positiveInterventionPerformed);
        return repository.save(positiveInterventionPerformed);
    }

    @DeleteMapping("/PositiveInterventionPerformed/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id)
    {
        PositiveInterventionPerformed pep =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Delete Failed: PositiveInterventionPerformed/%s not found", id),
                                        new ResourceNotFound()
                                )
                        );

        repository.delete(pep);

        return ResponseEntity.ok().build();
    }
}
