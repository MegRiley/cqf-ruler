package org.opencds.cqf.qdm.fivepoint4.controller;

import org.opencds.cqf.qdm.fivepoint4.exception.ResourceNotFound;
import org.opencds.cqf.qdm.fivepoint4.model.*;
import org.opencds.cqf.qdm.fivepoint4.repository.NegativeImmunizationAdministeredRepository;
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
public class NegativeImmunizationAdministeredController implements Serializable
{
    private final NegativeImmunizationAdministeredRepository repository;

    @Autowired
    public NegativeImmunizationAdministeredController(NegativeImmunizationAdministeredRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/NegativeImmunizationAdministered")
    public List<NegativeImmunizationAdministered> getAll(@RequestParam(name = "patientId", required = false) String patientId)
    {
        if (patientId == null)
        {
            return repository.findAll();
        }
        else {
            NegativeImmunizationAdministered exampleType = new NegativeImmunizationAdministered();
            Id pId = new Id();
            pId.setValue(patientId);
            exampleType.setPatientId(pId);
            ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("patientId.value", ExampleMatcher.GenericPropertyMatchers.exact());
            Example<NegativeImmunizationAdministered> example = Example.of(exampleType, matcher);

            return repository.findAll(example);
        }
    }

    @GetMapping("/NegativeImmunizationAdministered/{id}")
    public @ResponseBody NegativeImmunizationAdministered getById(@PathVariable(value = "id") String id)
    {
        return repository.findBySystemId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Read Failed: NegativeImmunizationAdministered/%s not found", id),
                                new ResourceNotFound()
                        )
                );
    }

    @PostMapping("/NegativeImmunizationAdministered")
    public @ResponseBody NegativeImmunizationAdministered create(@RequestBody @Valid NegativeImmunizationAdministered negativeImmunizationAdministered)
    {
        QdmValidator.validateResourceTypeAndName(negativeImmunizationAdministered, negativeImmunizationAdministered);
        return repository.save(negativeImmunizationAdministered);
    }

    @PutMapping("/NegativeImmunizationAdministered/{id}")
    public NegativeImmunizationAdministered update(@PathVariable(value = "id") String id,
                                             @RequestBody @Valid NegativeImmunizationAdministered negativeImmunizationAdministered)
    {
        QdmValidator.validateResourceId(negativeImmunizationAdministered.getId(), id);
        Optional<NegativeImmunizationAdministered> update = repository.findById(id);
        if (update.isPresent())
        {
            NegativeImmunizationAdministered updateResource = update.get();
            QdmValidator.validateResourceTypeAndName(negativeImmunizationAdministered, updateResource);
            updateResource.copy(negativeImmunizationAdministered);
            return repository.save(updateResource);
        }

        QdmValidator.validateResourceTypeAndName(negativeImmunizationAdministered, negativeImmunizationAdministered);
        return repository.save(negativeImmunizationAdministered);
    }

    @DeleteMapping("/NegativeImmunizationAdministered/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id)
    {
        NegativeImmunizationAdministered nep =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Delete Failed: NegativeImmunizationAdministered/%s not found", id),
                                        new ResourceNotFound()
                                )
                        );

        repository.delete(nep);

        return ResponseEntity.ok().build();
    }
}
