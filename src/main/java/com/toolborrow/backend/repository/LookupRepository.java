package com.toolborrow.backend.repository;

import com.toolborrow.backend.model.entity.Lookup;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LookupRepository extends JpaRepository<Lookup, Long> {

    @Query("""
        select s
        from Lookup s
        where s.code = :code
          and s.lookupType.code = :lookupTypeCode
        """)
    @NonNull
    Optional<Lookup> findByCodeAndLookupTypeCode(
        final @NonNull @Param("code") String code,
        final @NonNull @Param("lookupTypeCode") String lookupTypeCode
    );
}
