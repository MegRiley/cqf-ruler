package org.opencds.cqf.qdm.fivepoint4.controller;

import org.opencds.cqf.qdm.fivepoint4.exception.ResourceNotFound;
import org.opencds.cqf.qdm.fivepoint4.model.*;
import org.opencds.cqf.qdm.fivepoint4.repository.ProviderCareExperienceRepository;
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
public class ProviderCareExperienceController implements Serializable
{
    private final ProviderCareExperienceRepository repository;

    @Autowired
    public ProviderCareExperienceController(ProviderCareExperienceRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/ProviderCareExperience")
    public List<ProviderCareExperience> getAll(@RequestParam(name = "patientId", required = false) String patientId)
    {
        if (patientId == null)
        {
            return repository.findAll();
        }
        else {
            ProviderCareExperience exampleType = new ProviderCareExperience();
            Id pId = new Id();
            pId.setValue(patientId);
            exampleType.setPatientId(pId);
            ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("patientId.value", ExampleMatcher.GenericPropertyMatchers.exact());
            Example<ProviderCareExperience> example = Example.of(exampleType, matcher);

            return repository.findAll(example);
        }
    }

    @GetMapping("/ProviderCareExperience/{id}")
    public @ResponseBody
    ProviderCareExperience getById(@PathVariable(value = "id") String id)
    {
        return repository.findBySystemId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Read Failed: ProviderCareExperience/%s not found", id),
                                new ResourceNotFound()
                        )
                );
    }

    @PostMapping("/ProviderCareExperience")
    public @ResponseBody ProviderCareExperience create(@RequestBody @Valid ProviderCareExperience providerCareExperience)
    {
        QdmValidator.validateResourceTypeAndName(providerCareExperience, providerCareExperience);
        return repository.save(providerCareExperience);
    }

    @PutMapping("/ProviderCareExperience/{id}")
    public ProviderCareExperience update(@PathVariable(value = "id") String id,
                          @RequestBody @Valid ProviderCareExperience providerCareExperience)
    {
        QdmValidator.validateResourceId(providerCareExperience.getId(), id);
        Optional<ProviderCareExperience> update = repository.findById(id);
        if (update.isPresent())
        {
            ProviderCareExperience updateResource = update.get();
            QdmValidator.validateResourceTypeAndName(providerCareExperience, updateResource);
            updateResource.copy(providerCareExperience);
            return repository.save(updateResource);
        }

        QdmValidator.validateResourceTypeAndName(providerCareExperience, providerCareExperience);
        return repository.save(providerCareExperience);
    }

    @DeleteMapping("/ProviderCareExperience/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id)
    {
        ProviderCareExperience par =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Delete Failed: ProviderCareExperience/%s not found", id),
                                        new ResourceNotFound()
                                )
                        );

        repository.delete(par);

        return ResponseEntity.ok().build();
    }
}
