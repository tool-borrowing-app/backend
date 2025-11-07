package com.toolborrow.backend.model.entity;

import com.toolborrow.backend.model.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tool")
public class Tool extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
}
