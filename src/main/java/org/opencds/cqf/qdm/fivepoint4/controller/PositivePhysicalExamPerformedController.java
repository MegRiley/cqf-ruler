package org.opencds.cqf.qdm.fivepoint4.controller;

import org.opencds.cqf.qdm.fivepoint4.exception.ResourceNotFound;
import org.opencds.cqf.qdm.fivepoint4.model.*;
import org.opencds.cqf.qdm.fivepoint4.repository.PositivePhysicalExamPerformedRepository;
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
public class PositivePhysicalExamPerformedController implements Serializable
{
    private final PositivePhysicalExamPerformedRepository repository;

    @Autowired
    public PositivePhysicalExamPerformedController(PositivePhysicalExamPerformedRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/PositivePhysicalExamPerformed")
    public List<PositivePhysicalExamPerformed> getAll(@RequestParam(name = "patientId", required = false) String patientId)
    {
        if (patientId == null)
        {
            return repository.findAll();
        }
        else {
            PositivePhysicalExamPerformed exampleType = new PositivePhysicalExamPerformed();
            Id pId = new Id();
            pId.setValue(patientId);
            exampleType.setPatientId(pId);
            ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("patientId.value", ExampleMatcher.GenericPropertyMatchers.exact());
            Example<PositivePhysicalExamPerformed> example = Example.of(exampleType, matcher);

            return repository.findAll(example);
        }
    }

    @GetMapping("/PositivePhysicalExamPerformed/{id}")
    public @ResponseBody PositivePhysicalExamPerformed getById(@PathVariable(value = "id") String id)
    {
        return repository.findBySystemId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Read Failed: PositivePhysicalExamPerformed/%s not found", id),
                                new ResourceNotFound()
                        )
                );
    }

    @PostMapping("/PositivePhysicalExamPerformed")
    public PositivePhysicalExamPerformed create(@RequestBody @Valid PositivePhysicalExamPerformed positivePhysicalExamPerformed)
    {
        QdmValidator.validateResourceTypeAndName(positivePhysicalExamPerformed, positivePhysicalExamPerformed);
        return repository.save(positivePhysicalExamPerformed);
    }

    @PutMapping("/PositivePhysicalExamPerformed/{id}")
    public PositivePhysicalExamPerformed update(@PathVariable(value = "id") String id,
                                             @RequestBody @Valid PositivePhysicalExamPerformed positivePhysicalExamPerformed)
    {
        QdmValidator.validateResourceId(positivePhysicalExamPerformed.getId(), id);
        Optional<PositivePhysicalExamPerformed> update = repository.findById(id);
        if (update.isPresent())
        {
            PositivePhysicalExamPerformed updateResource = update.get();
            QdmValidator.validateResourceTypeAndName(positivePhysicalExamPerformed, updateResource);
            updateResource.copy(positivePhysicalExamPerformed);
            return repository.save(updateResource);
        }

        QdmValidator.validateResourceTypeAndName(positivePhysicalExamPerformed, positivePhysicalExamPerformed);
        return repository.save(positivePhysicalExamPerformed);
    }

    @DeleteMapping("/PositivePhysicalExamPerformed/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id)
    {
        PositivePhysicalExamPerformed pep =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Delete Failed: PositivePhysicalExamPerformed/%s not found", id),
                                        new ResourceNotFound()
                                )
                        );

        repository.delete(pep);

        return ResponseEntity.ok().build();
    }
}
