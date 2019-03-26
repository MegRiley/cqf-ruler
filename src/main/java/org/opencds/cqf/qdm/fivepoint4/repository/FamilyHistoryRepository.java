package org.opencds.cqf.qdm.fivepoint4.repository;

import org.opencds.cqf.qdm.fivepoint4.model.FamilyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Repository
public interface FamilyHistoryRepository extends JpaRepository<FamilyHistory, String>
{
    @Nonnull
    Optional<FamilyHistory> findBySystemId(@Nonnull String id);
}
