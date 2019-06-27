package org.opencds.cqf.qdm.fivepoint4.controller;

import org.opencds.cqf.qdm.fivepoint4.exception.ResourceNotFound;
import org.opencds.cqf.qdm.fivepoint4.model.*;
import org.opencds.cqf.qdm.fivepoint4.repository.PositiveImmunizationOrderRepository;
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
public class PositiveImmunizationOrderController implements Serializable
{
    private final PositiveImmunizationOrderRepository repository;

    @Autowired
    public PositiveImmunizationOrderController(PositiveImmunizationOrderRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/PositiveImmunizationOrder")
    public List<PositiveImmunizationOrder> getAll(@RequestParam(name = "patientId", required = false) String patientId)
    {
        if (patientId == null)
        {
            return repository.findAll();
        }
        else {
            PositiveImmunizationOrder exampleType = new PositiveImmunizationOrder();
            Id pId = new Id();
            pId.setValue(patientId);
            exampleType.setPatientId(pId);
            ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("patientId.value", ExampleMatcher.GenericPropertyMatchers.exact());
            Example<PositiveImmunizationOrder> example = Example.of(exampleType, matcher);

            return repository.findAll(example);
        }
    }

    @GetMapping("/PositiveImmunizationOrder/{id}")
    public @ResponseBody PositiveImmunizationOrder getById(@PathVariable(value = "id") String id)
    {
        return repository.findBySystemId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Read Failed: PositiveImmunizationOrder/%s not found", id),
                                new ResourceNotFound()
                        )
                );
    }

    @PostMapping("/PositiveImmunizationOrder")
    public PositiveImmunizationOrder create(@RequestBody @Valid PositiveImmunizationOrder positiveImmunizationOrder)
    {
        QdmValidator.validateResourceTypeAndName(positiveImmunizationOrder, positiveImmunizationOrder);
        return repository.save(positiveImmunizationOrder);
    }

    @PutMapping("/PositiveImmunizationOrder/{id}")
    public PositiveImmunizationOrder update(@PathVariable(value = "id") String id,
                                             @RequestBody @Valid PositiveImmunizationOrder positiveImmunizationOrder)
    {
        QdmValidator.validateResourceId(positiveImmunizationOrder.getId(), id);
        Optional<PositiveImmunizationOrder> update = repository.findById(id);
        if (update.isPresent())
        {
            PositiveImmunizationOrder updateResource = update.get();
            QdmValidator.validateResourceTypeAndName(positiveImmunizationOrder, updateResource);
            updateResource.copy(positiveImmunizationOrder);
            return repository.save(updateResource);
        }

        QdmValidator.validateResourceTypeAndName(positiveImmunizationOrder, positiveImmunizationOrder);
        return repository.save(positiveImmunizationOrder);
    }

    @DeleteMapping("/PositiveImmunizationOrder/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id)
    {
        PositiveImmunizationOrder pep =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Delete Failed: PositiveImmunizationOrder/%s not found", id),
                                        new ResourceNotFound()
                                )
                        );

        repository.delete(pep);

        return ResponseEntity.ok().build();
    }
}
