package org.opencds.cqf.qdm.fivepoint4.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class NegativeLaboratoryTestOrder extends LaboratoryTestOrder implements Serializable
{
    @JsonIgnore
    @Override
    public String getName()
    {
        return "NegativeLaboratoryTestOrder";
    }

    @Override
    public void copy(BaseType other)
    {
        super.copy(other);
    }
}
