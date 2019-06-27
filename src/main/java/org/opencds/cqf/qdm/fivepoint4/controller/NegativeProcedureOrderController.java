package org.opencds.cqf.qdm.fivepoint4.controller;

import org.opencds.cqf.qdm.fivepoint4.exception.ResourceNotFound;
import org.opencds.cqf.qdm.fivepoint4.model.*;
import org.opencds.cqf.qdm.fivepoint4.repository.NegativeProcedureOrderRepository;
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
public class NegativeProcedureOrderController implements Serializable
{
    private final NegativeProcedureOrderRepository repository;

    @Autowired
    public NegativeProcedureOrderController(NegativeProcedureOrderRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/NegativeProcedureOrder")
    public List<NegativeProcedureOrder> getAll(@RequestParam(name = "patientId", required = false) String patientId)
    {
        if (patientId == null)
        {
            return repository.findAll();
        }
        else {
            NegativeProcedureOrder exampleType = new NegativeProcedureOrder();
            Id pId = new Id();
            pId.setValue(patientId);
            exampleType.setPatientId(pId);
            ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("patientId.value", ExampleMatcher.GenericPropertyMatchers.exact());
            Example<NegativeProcedureOrder> example = Example.of(exampleType, matcher);

            return repository.findAll(example);
        }
    }

    @GetMapping("/NegativeProcedureOrder/{id}")
    public @ResponseBody NegativeProcedureOrder getById(@PathVariable(value = "id") String id)
    {
        return repository.findBySystemId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Read Failed: NegativeProcedureOrder/%s not found", id),
                                new ResourceNotFound()
                        )
                );
    }

    @PostMapping("/NegativeProcedureOrder")
    public @ResponseBody NegativeProcedureOrder create(@RequestBody @Valid NegativeProcedureOrder negativeProcedureOrder)
    {
        QdmValidator.validateResourceTypeAndName(negativeProcedureOrder, negativeProcedureOrder);
        return repository.save(negativeProcedureOrder);
    }

    @PutMapping("/NegativeProcedureOrder/{id}")
    public NegativeProcedureOrder update(@PathVariable(value = "id") String id,
                                             @RequestBody @Valid NegativeProcedureOrder negativeProcedureOrder)
    {
        QdmValidator.validateResourceId(negativeProcedureOrder.getId(), id);
        Optional<NegativeProcedureOrder> update = repository.findById(id);
        if (update.isPresent())
        {
            NegativeProcedureOrder updateResource = update.get();
            QdmValidator.validateResourceTypeAndName(negativeProcedureOrder, updateResource);
            updateResource.copy(negativeProcedureOrder);
            return repository.save(updateResource);
        }

        QdmValidator.validateResourceTypeAndName(negativeProcedureOrder, negativeProcedureOrder);
        return repository.save(negativeProcedureOrder);
    }

    @DeleteMapping("/NegativeProcedureOrder/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id)
    {
        NegativeProcedureOrder nep =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Delete Failed: NegativeProcedureOrder/%s not found", id),
                                        new ResourceNotFound()
                                )
                        );

        repository.delete(nep);

        return ResponseEntity.ok().build();
    }
}
