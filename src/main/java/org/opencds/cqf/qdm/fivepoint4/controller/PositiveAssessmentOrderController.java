package org.opencds.cqf.qdm.fivepoint4.controller;

import org.opencds.cqf.qdm.fivepoint4.exception.ResourceNotFound;
import org.opencds.cqf.qdm.fivepoint4.model.*;
import org.opencds.cqf.qdm.fivepoint4.repository.PositiveAssessmentOrderRepository;
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
public class PositiveAssessmentOrderController implements Serializable
{
    private final PositiveAssessmentOrderRepository repository;

    @Autowired
    public PositiveAssessmentOrderController(PositiveAssessmentOrderRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/PositiveAssessmentOrder")
    public List<PositiveAssessmentOrder> getAll(@RequestParam(name = "patientId", required = false) String patientId)
    {
        if (patientId == null)
        {
            return repository.findAll();
        }
        else {
            PositiveAssessmentOrder exampleType = new PositiveAssessmentOrder();
            Id pId = new Id();
            pId.setValue(patientId);
            exampleType.setPatientId(pId);
            ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("patientId.value", ExampleMatcher.GenericPropertyMatchers.exact());
            Example<PositiveAssessmentOrder> example = Example.of(exampleType, matcher);

            return repository.findAll(example);
        }
    }

    @GetMapping("/PositiveAssessmentOrder/{id}")
    public @ResponseBody
    PositiveAssessmentOrder getById(@PathVariable(value = "id") String id)
    {
        return repository.findBySystemId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Read Failed: PositiveAssessmentOrder/%s not found", id),
                                new ResourceNotFound()
                        )
                );
    }

    @PostMapping("/PositiveAssessmentOrder")
    public @ResponseBody PositiveAssessmentOrder create(@RequestBody @Valid PositiveAssessmentOrder positiveAssessmentOrder)
    {
        QdmValidator.validateResourceTypeAndName(positiveAssessmentOrder, positiveAssessmentOrder);
        return repository.save(positiveAssessmentOrder);
    }

    @PutMapping("/PositiveAssessmentOrder/{id}")
    public PositiveAssessmentOrder update(@PathVariable(value = "id") String id,
                          @RequestBody @Valid PositiveAssessmentOrder positiveAssessmentOrder)
    {
        QdmValidator.validateResourceId(positiveAssessmentOrder.getId(), id);
        Optional<PositiveAssessmentOrder> update = repository.findById(id);
        if (update.isPresent())
        {
            PositiveAssessmentOrder updateResource = update.get();
            QdmValidator.validateResourceTypeAndName(positiveAssessmentOrder, updateResource);
            updateResource.copy(positiveAssessmentOrder);
            return repository.save(updateResource);
        }

        QdmValidator.validateResourceTypeAndName(positiveAssessmentOrder, positiveAssessmentOrder);
        return repository.save(positiveAssessmentOrder);
    }

    @DeleteMapping("/PositiveAssessmentOrder/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id)
    {
        PositiveAssessmentOrder par =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Delete Failed: PositiveAssessmentOrder/%s not found", id),
                                        new ResourceNotFound()
                                )
                        );

        repository.delete(par);

        return ResponseEntity.ok().build();
    }
}
