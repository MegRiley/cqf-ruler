package org.opencds.cqf.qdm.fivepoint4.controller;

import org.opencds.cqf.qdm.fivepoint4.exception.ResourceNotFound;
import org.opencds.cqf.qdm.fivepoint4.model.*;
import org.opencds.cqf.qdm.fivepoint4.repository.PositiveSubstanceOrderRepository;
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
public class PositiveSubstanceOrderController implements Serializable
{
    private final PositiveSubstanceOrderRepository repository;

    @Autowired
    public PositiveSubstanceOrderController(PositiveSubstanceOrderRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/PositiveSubstanceOrder")
    public List<PositiveSubstanceOrder> getAll(@RequestParam(name = "patientId", required = false) String patientId)
    {
        if (patientId == null)
        {
            return repository.findAll();
        }
        else {
            PositiveSubstanceOrder exampleType = new PositiveSubstanceOrder();
            Id pId = new Id();
            pId.setValue(patientId);
            exampleType.setPatientId(pId);
            ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("patientId.value", ExampleMatcher.GenericPropertyMatchers.exact());
            Example<PositiveSubstanceOrder> example = Example.of(exampleType, matcher);

            return repository.findAll(example);
        }
    }

    @GetMapping("/PositiveSubstanceOrder/{id}")
    public @ResponseBody PositiveSubstanceOrder getById(@PathVariable(value = "id") String id)
    {
        return repository.findBySystemId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Read Failed: PositiveSubstanceOrder/%s not found", id),
                                new ResourceNotFound()
                        )
                );
    }

    @PostMapping("/PositiveSubstanceOrder")
    public PositiveSubstanceOrder create(@RequestBody @Valid PositiveSubstanceOrder positiveSubstanceOrder)
    {
        QdmValidator.validateResourceTypeAndName(positiveSubstanceOrder, positiveSubstanceOrder);
        return repository.save(positiveSubstanceOrder);
    }

    @PutMapping("/PositiveSubstanceOrder/{id}")
    public PositiveSubstanceOrder update(@PathVariable(value = "id") String id,
                                             @RequestBody @Valid PositiveSubstanceOrder positiveSubstanceOrder)
    {
        QdmValidator.validateResourceId(positiveSubstanceOrder.getId(), id);
        Optional<PositiveSubstanceOrder> update = repository.findById(id);
        if (update.isPresent())
        {
            PositiveSubstanceOrder updateResource = update.get();
            QdmValidator.validateResourceTypeAndName(positiveSubstanceOrder, updateResource);
            updateResource.copy(positiveSubstanceOrder);
            return repository.save(updateResource);
        }

        QdmValidator.validateResourceTypeAndName(positiveSubstanceOrder, positiveSubstanceOrder);
        return repository.save(positiveSubstanceOrder);
    }

    @DeleteMapping("/PositiveSubstanceOrder/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id)
    {
        PositiveSubstanceOrder pep =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Delete Failed: PositiveSubstanceOrder/%s not found", id),
                                        new ResourceNotFound()
                                )
                        );

        repository.delete(pep);

        return ResponseEntity.ok().build();
    }
}
