package org.opencds.cqf.qdm.fivepoint4.repository;

import org.opencds.cqf.qdm.fivepoint4.model.PositiveDeviceRecommended;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Repository
public interface PositiveDeviceRecommendedRepository extends JpaRepository<PositiveDeviceRecommended, String>
{
    @Nonnull
    Optional<PositiveDeviceRecommended> findBySystemId(@Nonnull String id);
}
