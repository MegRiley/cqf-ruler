package org.opencds.cqf.qdm.fivepoint4.controller;

import org.opencds.cqf.qdm.fivepoint4.exception.ResourceNotFound;
import org.opencds.cqf.qdm.fivepoint4.model.*;
import org.opencds.cqf.qdm.fivepoint4.repository.NegativeMedicationDispensedRepository;
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
public class NegativeMedicationDispensedController implements Serializable
{
    private final NegativeMedicationDispensedRepository repository;

    @Autowired
    public NegativeMedicationDispensedController(NegativeMedicationDispensedRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/NegativeMedicationDispensed")
    public List<NegativeMedicationDispensed> getAll(@RequestParam(name = "patientId", required = false) String patientId)
    {
        if (patientId == null)
        {
            return repository.findAll();
        }
        else {
            NegativeMedicationDispensed exampleType = new NegativeMedicationDispensed();
            Id pId = new Id();
            pId.setValue(patientId);
            exampleType.setPatientId(pId);
            ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("patientId.value", ExampleMatcher.GenericPropertyMatchers.exact());
            Example<NegativeMedicationDispensed> example = Example.of(exampleType, matcher);

            return repository.findAll(example);
        }
    }

    @GetMapping("/NegativeMedicationDispensed/{id}")
    public @ResponseBody NegativeMedicationDispensed getById(@PathVariable(value = "id") String id)
    {
        return repository.findBySystemId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Read Failed: NegativeMedicationDispensed/%s not found", id),
                                new ResourceNotFound()
                        )
                );
    }

    @PostMapping("/NegativeMedicationDispensed")
    public @ResponseBody NegativeMedicationDispensed create(@RequestBody @Valid NegativeMedicationDispensed negativeMedicationDispensed)
    {
        QdmValidator.validateResourceTypeAndName(negativeMedicationDispensed, negativeMedicationDispensed);
        return repository.save(negativeMedicationDispensed);
    }

    @PutMapping("/NegativeMedicationDispensed/{id}")
    public NegativeMedicationDispensed update(@PathVariable(value = "id") String id,
                                             @RequestBody @Valid NegativeMedicationDispensed negativeMedicationDispensed)
    {
        QdmValidator.validateResourceId(negativeMedicationDispensed.getId(), id);
        Optional<NegativeMedicationDispensed> update = repository.findById(id);
        if (update.isPresent())
        {
            NegativeMedicationDispensed updateResource = update.get();
            QdmValidator.validateResourceTypeAndName(negativeMedicationDispensed, updateResource);
            updateResource.copy(negativeMedicationDispensed);
            return repository.save(updateResource);
        }

        QdmValidator.validateResourceTypeAndName(negativeMedicationDispensed, negativeMedicationDispensed);
        return repository.save(negativeMedicationDispensed);
    }

    @DeleteMapping("/NegativeMedicationDispensed/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id)
    {
        NegativeMedicationDispensed nep =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Delete Failed: NegativeMedicationDispensed/%s not found", id),
                                        new ResourceNotFound()
                                )
                        );

        repository.delete(nep);

        return ResponseEntity.ok().build();
    }
}
