package org.opencds.cqf.qdm.fivepoint4.controller;

import org.opencds.cqf.qdm.fivepoint4.exception.ResourceNotFound;
import org.opencds.cqf.qdm.fivepoint4.model.*;
import org.opencds.cqf.qdm.fivepoint4.repository.NegativePhysicalExamRecommendedRepository;
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
public class NegativePhysicalExamRecommendedController implements Serializable
{
    private final NegativePhysicalExamRecommendedRepository repository;

    @Autowired
    public NegativePhysicalExamRecommendedController(NegativePhysicalExamRecommendedRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/NegativePhysicalExamRecommended")
    public List<NegativePhysicalExamRecommended> getAll(@RequestParam(name = "patientId", required = false) String patientId)
    {
        if (patientId == null)
        {
            return repository.findAll();
        }
        else {
            NegativePhysicalExamRecommended exampleType = new NegativePhysicalExamRecommended();
            Id pId = new Id();
            pId.setValue(patientId);
            exampleType.setPatientId(pId);
            ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("patientId.value", ExampleMatcher.GenericPropertyMatchers.exact());
            Example<NegativePhysicalExamRecommended> example = Example.of(exampleType, matcher);

            return repository.findAll(example);
        }
    }

    @GetMapping("/NegativePhysicalExamRecommended/{id}")
    public @ResponseBody NegativePhysicalExamRecommended getById(@PathVariable(value = "id") String id)
    {
        return repository.findBySystemId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Read Failed: NegativePhysicalExamRecommended/%s not found", id),
                                new ResourceNotFound()
                        )
                );
    }

    @PostMapping("/NegativePhysicalExamRecommended")
    public @ResponseBody NegativePhysicalExamRecommended create(@RequestBody @Valid NegativePhysicalExamRecommended negativePhysicalExamRecommended)
    {
        QdmValidator.validateResourceTypeAndName(negativePhysicalExamRecommended, negativePhysicalExamRecommended);
        return repository.save(negativePhysicalExamRecommended);
    }

    @PutMapping("/NegativePhysicalExamRecommended/{id}")
    public NegativePhysicalExamRecommended update(@PathVariable(value = "id") String id,
                                             @RequestBody @Valid NegativePhysicalExamRecommended negativePhysicalExamRecommended)
    {
        QdmValidator.validateResourceId(negativePhysicalExamRecommended.getId(), id);
        Optional<NegativePhysicalExamRecommended> update = repository.findById(id);
        if (update.isPresent())
        {
            NegativePhysicalExamRecommended updateResource = update.get();
            QdmValidator.validateResourceTypeAndName(negativePhysicalExamRecommended, updateResource);
            updateResource.copy(negativePhysicalExamRecommended);
            return repository.save(updateResource);
        }

        QdmValidator.validateResourceTypeAndName(negativePhysicalExamRecommended, negativePhysicalExamRecommended);
        return repository.save(negativePhysicalExamRecommended);
    }

    @DeleteMapping("/NegativePhysicalExamRecommended/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id)
    {
        NegativePhysicalExamRecommended nep =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Delete Failed: NegativePhysicalExamRecommended/%s not found", id),
                                        new ResourceNotFound()
                                )
                        );

        repository.delete(nep);

        return ResponseEntity.ok().build();
    }
}
