package org.opencds.cqf.qdm.fivepoint4.controller;

import org.opencds.cqf.qdm.fivepoint4.exception.ResourceNotFound;
import org.opencds.cqf.qdm.fivepoint4.model.*;
import org.opencds.cqf.qdm.fivepoint4.repository.NegativeLaboratoryTestRecommendedRepository;
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
public class NegativeLaboratoryTestRecommendedController implements Serializable
{
    private final NegativeLaboratoryTestRecommendedRepository repository;

    @Autowired
    public NegativeLaboratoryTestRecommendedController(NegativeLaboratoryTestRecommendedRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/NegativeLaboratoryTestRecommended")
    public List<NegativeLaboratoryTestRecommended> getAll(@RequestParam(name = "patientId", required = false) String patientId)
    {
        if (patientId == null)
        {
            return repository.findAll();
        }
        else {
            NegativeLaboratoryTestRecommended exampleType = new NegativeLaboratoryTestRecommended();
            Id pId = new Id();
            pId.setValue(patientId);
            exampleType.setPatientId(pId);
            ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("patientId.value", ExampleMatcher.GenericPropertyMatchers.exact());
            Example<NegativeLaboratoryTestRecommended> example = Example.of(exampleType, matcher);

            return repository.findAll(example);
        }
    }

    @GetMapping("/NegativeLaboratoryTestRecommended/{id}")
    public @ResponseBody NegativeLaboratoryTestRecommended getById(@PathVariable(value = "id") String id)
    {
        return repository.findBySystemId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Read Failed: NegativeLaboratoryTestRecommended/%s not found", id),
                                new ResourceNotFound()
                        )
                );
    }

    @PostMapping("/NegativeLaboratoryTestRecommended")
    public @ResponseBody NegativeLaboratoryTestRecommended create(@RequestBody @Valid NegativeLaboratoryTestRecommended negativeLaboratoryTestRecommended)
    {
        QdmValidator.validateResourceTypeAndName(negativeLaboratoryTestRecommended, negativeLaboratoryTestRecommended);
        return repository.save(negativeLaboratoryTestRecommended);
    }

    @PutMapping("/NegativeLaboratoryTestRecommended/{id}")
    public NegativeLaboratoryTestRecommended update(@PathVariable(value = "id") String id,
                                             @RequestBody @Valid NegativeLaboratoryTestRecommended negativeLaboratoryTestRecommended)
    {
        QdmValidator.validateResourceId(negativeLaboratoryTestRecommended.getId(), id);
        Optional<NegativeLaboratoryTestRecommended> update = repository.findById(id);
        if (update.isPresent())
        {
            NegativeLaboratoryTestRecommended updateResource = update.get();
            QdmValidator.validateResourceTypeAndName(negativeLaboratoryTestRecommended, updateResource);
            updateResource.copy(negativeLaboratoryTestRecommended);
            return repository.save(updateResource);
        }

        QdmValidator.validateResourceTypeAndName(negativeLaboratoryTestRecommended, negativeLaboratoryTestRecommended);
        return repository.save(negativeLaboratoryTestRecommended);
    }

    @DeleteMapping("/NegativeLaboratoryTestRecommended/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id)
    {
        NegativeLaboratoryTestRecommended nep =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Delete Failed: NegativeLaboratoryTestRecommended/%s not found", id),
                                        new ResourceNotFound()
                                )
                        );

        repository.delete(nep);

        return ResponseEntity.ok().build();
    }
}
