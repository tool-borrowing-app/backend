package com.toolborrow.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.toolborrow.backend.model.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
    name = "lookup",
    uniqueConstraints = @UniqueConstraint(columnNames = {"lookup_type_id", "code"})
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Lookup extends BaseEntity {
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @JoinColumn(name = "lookup_type_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private LookupType lookupType;
}
