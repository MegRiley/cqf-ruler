package org.opencds.cqf.qdm.conversion.model;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.dstu3.model.ResourceType;

@ResourceDef(name="PositiveSubstanceRecommended", profile="TODO")
public class PositiveSubstanceRecommended extends SubstanceRecommended {
    @Override
    public PositiveSubstanceRecommended copy() {
        PositiveSubstanceRecommended retVal = new PositiveSubstanceRecommended();
        super.copyValues(retVal);

        retVal.authorDatetime = authorDatetime;
        retVal.reason = reason;
        retVal.dosage = dosage;
        retVal.supply = supply;
        retVal.frequency = frequency;
        retVal.method = method;
        retVal.refills = refills;
        retVal.route = route;
        retVal.negationRationale = negationRationale;

        return retVal;
    }

    @Override
    public ResourceType getResourceType() {
        return null;
    }

    @Override
    public String getResourceName() {
        return "PositiveSubstanceRecommended";
    }
}
