package com.softserve.itacademy.entity;

import com.softserve.itacademy.entity.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Accessors(chain = true)
@Table(name = "users")
public class User extends BasicEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean disabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_users_roles_user_id"))},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_roles_users_role_id"))}
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_courses",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__users__courses__user_id"))},
            inverseJoinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk__courses__users__course_id"))}
    )
    private Set<Course> courses = new HashSet<>();

    public void addGroup(Group group) {
        group.getUsers().add(this);
        this.groups.add(group);
    }


}
