package com.softserve.itacademy.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "material_expirations")
@Entity
public class MaterialExpiration extends Expiration {
    @ManyToOne
    @JoinColumn(name = "material_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_expiration_material"))
    private Material material;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_expiration_group"))
    private Group group;
}
