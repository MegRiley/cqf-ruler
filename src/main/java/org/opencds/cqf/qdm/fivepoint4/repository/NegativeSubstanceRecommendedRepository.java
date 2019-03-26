package org.opencds.cqf.qdm.fivepoint4.repository;

import org.opencds.cqf.qdm.fivepoint4.model.NegativeSubstanceRecommended;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Repository
public interface NegativeSubstanceRecommendedRepository extends JpaRepository<NegativeSubstanceRecommended, String>
{
    @Nonnull
    Optional<NegativeSubstanceRecommended> findBySystemId(@Nonnull String id);
}
