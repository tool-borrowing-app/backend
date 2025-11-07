package com.toolborrow.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.toolborrow.backend.model.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "lookup_type")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LookupType extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "lookupType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Lookup> lookups;
}
